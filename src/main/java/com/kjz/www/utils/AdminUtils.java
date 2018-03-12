package com.kjz.www.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.kjz.www.admin.service.IAdminService;
import com.kjz.www.admin.vo.AdminVo;
import com.kjz.www.common.Config;
import com.kjz.www.utils.vo.AdminCookie;


@Component("adminUtils")
public class AdminUtils {

	private String cookieName = "admin";

	@Resource
	protected IAdminService adminService;

	public AdminCookie getLoginAdmin(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		AdminVo adminVo = this.getAdminFromSession(request, response, session);
		if (adminVo == null) {
			return null;
		}
		AdminCookie adminCookie = new AdminCookie();
		BeanUtils.copyProperties(adminVo, adminCookie);
		return adminCookie;
	}

	public AdminVo getAdminFromSession(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		if (session.getAttribute(this.cookieName) == null) {
			return this.loginFromCookie(response, request);
		}
		return (AdminVo) session.getAttribute(this.cookieName);
	}

	public void putAdminInSession(HttpServletRequest request, HttpServletResponse response, HttpSession session, Object adminObj) {
		AdminVo adminVo = new AdminVo();
		BeanUtils.copyProperties(adminObj, adminVo);
		this.savaCookie(response, adminVo, "");
		session.setAttribute(this.cookieName, adminVo);
	}

	private void savaCookie(HttpServletResponse response, AdminVo adminVo, String remenber) {
//		String passwordInSession = userVo.getPassword().substring(0, 16);
		AdminCookie adminCookie = new AdminCookie();
		BeanUtils.copyProperties(adminVo, adminCookie);
//		userCookie.setPassword(passwordInSession);
		String adminVoCookieJson = JSON.toJSONString(adminCookie);
		try {
			adminVoCookieJson = URLEncoder.encode(adminVoCookieJson, "utf-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println("AdminService|savaCookie|URLEncoder.encode|e=" + e);
			e.printStackTrace();
		}
		this.addCookie(response, adminVoCookieJson, remenber);
	}

	private void addCookie(HttpServletResponse response, String str, String remenber) {
		this.addCookie(response, this.cookieName, str, remenber);
	}

	public void addCookie(HttpServletResponse response, String name, String str, String remenber) {
		Cookie adminCookie = new Cookie(name, str);
		if ("yes".equals(remenber)) {
			adminCookie.setMaxAge(30 * 24 * 60 * 60); // 存活期为一个月 30*24*60*60
		}
		adminCookie.setPath("/");
		response.addCookie(adminCookie);
	}

	public String getFromCookie(HttpServletRequest request, String name) {
		if (request == null || request.getCookies() == null) {
			return null;
		}
		try {
			Cookie[] cookies = request.getCookies();
			Cookie cookie;
			for (int i = 0; i < cookies.length; i++) {
				cookie = cookies[i];
				if (name.equals(cookie.getName())) { // 获取键
					return cookie.getValue().toString(); // 获取值
				}
			}
		} catch (Exception e) {
			System.out.println("AdminService|getFromCookie|Exception|" + e);
		}
		return null;
	}

	public AdminVo loginFromCookie(HttpServletResponse response, HttpServletRequest request) {
		AdminCookie adminVoCookie = this.getAdminFromCookie(request);
		if (adminVoCookie == null) {
			this.addCookie(response, "", "");
			return null;
		}
		AdminVo adminVo = this.adminService.getById(adminVoCookie.getAdminAccount());
		if (adminVo == null) {
			return null;
		}
		if (adminVoCookie.getPassword().equals(adminVo.getAdminPassword().substring(0, 16))) {
			this.putAdminInSession(request, response, request.getSession(), adminVo);
			return adminVo;
		}
		return null;
	}

	/**
	 * 从cookie获得用户名和密码
	 * 
	 * @param request
	 * @return
	 */
	private AdminCookie getAdminFromCookie(HttpServletRequest request) {
		AdminCookie adminVoCookie = null;
		if (request == null || request.getCookies() == null) {
			return null;
		}
		String adminVoCookieJson = this.getFromCookie(request, this.cookieName);
		if (adminVoCookieJson == null) {
			return null;
		}
		try {
			adminVoCookieJson = URLDecoder.decode(adminVoCookieJson, "utf-8");
			String regex = "^[{]+.+[}]$";
			if (adminVoCookieJson.matches(regex)) {
				adminVoCookie = JSON.parseObject(adminVoCookieJson, AdminCookie.class);
			}
		} catch (UnsupportedEncodingException e) {
			System.out.println("AdminService|getAdminFromCookie|Exception|" + e);
			e.printStackTrace();
		}
		return adminVoCookie;
	}
 
	/*
	public String getSalt() {
		int number = Config.userSaltNumber;
		String sourceCharacter = Config.userSaltCharacter;
		return this.getRandomCharacter(sourceCharacter, number);
	}
	*/
	public String getGraphicVerificationCode() {
		int number = Config.verificationCodeNumber;
		String sourceCharacter = Config.verificationCodeCharacter;
		return this.getRandomCharacter(sourceCharacter, number);
	}

	public String getVerificationCode() {
		int number = Config.verificationCodeNumber;
		String sourceCharacter = Config.verificationCodeCharacter;
		return this.getRandomCharacter(sourceCharacter, number);
	}

	@SuppressWarnings("unchecked")
	public boolean isVerificationCodeRight(HttpSession session, String mobile, String verificationCode) {
		if (verificationCode != null && session != null && session.getAttribute(Config.verificationCodeMapInSession) != null) {
			Map<String, String> verificationCodeMap = (Map<String, String>) session.getAttribute(Config.verificationCodeMapInSession);
			String verificationCodeInSession = verificationCodeMap.get(Config.verificationCodeInSession);
			String mobileInSession = verificationCodeMap.get(Config.mobileInSession);
			return (verificationCode.equals(verificationCodeInSession) && mobile.equals(mobileInSession));
		}
		return false;
	}

	private String getRandomCharacter(String sourceCharacter, int number) {
		long time = System.currentTimeMillis();
		int sourceCharacterLength = sourceCharacter.length();
		long num = time % sourceCharacterLength;
		char s;
		StringBuffer buf = new StringBuffer();
		Integer index = (int) (num % sourceCharacterLength);
		int size = number + 1;
		for (int i = 1; i < size; i++) {
			s = sourceCharacter.toCharArray()[index];
			buf.append(s);
			index = (int) ((num * i + i) % sourceCharacterLength);
		}
		return buf.toString();
	}
}
