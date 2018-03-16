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
import com.kjz.www.common.Config;
import com.kjz.www.user.service.IUserService;
import com.kjz.www.user.vo.UserVo;
import com.kjz.www.utils.vo.UserCookie;

@Component("userUtils")
public class UserUtils {

	private String cookieName = "user";

	@Resource
	protected IUserService userService;

	public UserCookie getLoginUser(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		UserVo userVo = this.getUserFromSession(request, response, session);
		if (userVo == null) {
			return null;
		}
		UserCookie userCookie = new UserCookie();
		BeanUtils.copyProperties(userVo, userCookie);
		return userCookie;
	}

	public UserVo getUserFromSession(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		if (session.getAttribute(this.cookieName) == null) {
			return this.loginFromCookie(response, request);
		}
		return (UserVo) session.getAttribute(this.cookieName);
	}

	public void putUserInSession(HttpServletRequest request, HttpServletResponse response, HttpSession session, Object userObj) {
		UserVo userVo = new UserVo();
		BeanUtils.copyProperties(userObj, userVo);
		this.savaCookie(response, userVo, "");
		session.setAttribute(this.cookieName, userVo);
	}

	private void savaCookie(HttpServletResponse response, UserVo userVo, String remenber) {
//		String passwordInSession = userVo.getPassword().substring(0, 16);
		UserCookie userCookie = new UserCookie();
		BeanUtils.copyProperties(userVo, userCookie);
//		userCookie.setPassword(passwordInSession);
		String userVoCookieJson = JSON.toJSONString(userCookie);
		try {
			userVoCookieJson = URLEncoder.encode(userVoCookieJson, "utf-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println("UserService|savaCookie|URLEncoder.encode|e=" + e);
			e.printStackTrace();
		}
		this.addCookie(response, userVoCookieJson, remenber);
	}

	private void addCookie(HttpServletResponse response, String str, String remenber) {
		this.addCookie(response, this.cookieName, str, remenber);
	}

	public void addCookie(HttpServletResponse response, String name, String str, String remenber) {
		Cookie userCookie = new Cookie(name, str);
		if ("yes".equals(remenber)) {
			userCookie.setMaxAge(30 * 24 * 60 * 60); // 存活期为一个月 30*24*60*60
		}
		userCookie.setPath("/");
		response.addCookie(userCookie);
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
			System.out.println("UserService|getFromCookie|Exception|" + e);
		}
		return null;
	}

	public UserVo loginFromCookie(HttpServletResponse response, HttpServletRequest request) {
		UserCookie userVoCookie = this.getUserFromCookie(request);
		if (userVoCookie == null) {
			this.addCookie(response, "", "");
			return null;
		}
		UserVo userVo = this.userService.getById(userVoCookie.getUserId());
		if (userVo == null) {
			return null;
		}
		if (userVoCookie.getPassword().equals(userVo.getPassword().substring(0, 16))) {
			this.putUserInSession(request, response, request.getSession(), userVo);
			return userVo;
		}
		return null;
	}

	/**
	 * 从cookie获得用户名和密码
	 * 
	 * @param request
	 * @return
	 */
	private UserCookie getUserFromCookie(HttpServletRequest request) {
		UserCookie userVoCookie = null;
		if (request == null || request.getCookies() == null) {
			return null;
		}
		String userVoCookieJson = this.getFromCookie(request, this.cookieName);
		if (userVoCookieJson == null) {
			return null;
		}
		try {
			userVoCookieJson = URLDecoder.decode(userVoCookieJson, "utf-8");
			String regex = "^[{]+.+[}]$";
			if (userVoCookieJson.matches(regex)) {
				userVoCookie = JSON.parseObject(userVoCookieJson, UserCookie.class);
			}
		} catch (UnsupportedEncodingException e) {
			System.out.println("UserService|getUserFromCookie|Exception|" + e);
			e.printStackTrace();
		}
		return userVoCookie;
	}

	public String getSalt() {
		int number = Config.userSaltNumber;
		String sourceCharacter = Config.userSaltCharacter;
		return this.getRandomCharacter(sourceCharacter, number);
	}

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
	
	//通过userId查询用户状态,正常为true
	public Boolean getUserStateById(Integer userId){
		UserVo userVo=this.userService.getById(userId);
		String tbStatus=userVo.getTbStatus();
		if(tbStatus.equals("正常")){
			return true;
		}	
		return false;
	}
	
	
}
