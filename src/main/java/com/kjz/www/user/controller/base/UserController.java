
package com.kjz.www.user.controller.base;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aliyun.oss.OSSClient;
import com.kjz.www.utils.MD5Utils;
import com.kjz.www.utils.OSSUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

import java.math.BigDecimal;

import com.kjz.www.common.WebResponse;
import com.kjz.www.user.service.IUserService;
import com.kjz.www.user.domain.User;
import com.kjz.www.user.vo.UserVo;
import com.kjz.www.user.vo.UserVoFont;
import com.kjz.www.utils.UserUtils;
import com.kjz.www.utils.vo.UserCookie;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	protected WebResponse webResponse;

	@Resource
	protected UserUtils userUtils;

	@Resource
	protected MD5Utils md5Utils;

	@Resource
	protected IUserService userService;

	@RequestMapping(value = "/addOrEditUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse addOrEditUser(HttpServletRequest request, HttpServletResponse response, HttpSession session, String userId, @RequestParam(required = false) String nickname, @RequestParam(required = false) String password, @RequestParam(required = false) String email, @RequestParam(required = false) String phoneNumber, @RequestParam(required = false) String salt, @RequestParam(required = false) String level, @RequestParam(required = false) String headImg, @RequestParam(required = false) String tbStatus) {
		if (userId == null || userId.length() == 0) {
			return this.addUser(request, response, session, nickname, password, email, phoneNumber, salt, level, headImg);
		} else {
			return this.editUser(request, response, session, userId, nickname, password, email, phoneNumber, salt, level, headImg, tbStatus);
		}
	}
	//登录用户
    @RequestMapping(value = "/login", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse login(HttpServletRequest request, HttpServletResponse response, HttpSession session,String phoneNumber,String password) {
        String statusMsg = "";
        Integer statusCode = 200;
        LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
        condition.put("phone_number='" + phoneNumber + "'", "" );
        UserVo userVo = this.userService.getOne(condition);
        Object data = null;
        data=userVo;
        Map<String, String> resultMap = new HashMap<String, String>();
        if (phoneNumber.length() > 100 || password.length() > 100 ) {
            statusMsg = " 参数长度过长错误！！！";
            statusCode = 201;
            return webResponse.getWebResponse(statusCode, statusMsg, data);
        }
        if(userVo!=null) {
        	String pwd=this.md5Utils.generate(password,userVo.getSalt());

            if(this.md5Utils.verify(password,pwd)) {
                statusMsg = "登录成功！！！";
                this.userUtils.putUserInSession(request,response,session,userVo);
            }
        }else {
            statusMsg = "登录失败";
        }
        return webResponse.getWebResponse(statusCode, statusMsg, data);
    }

    //禁用用户
	@RequestMapping(value = "/forbid", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse forbid(HttpServletRequest request, HttpServletResponse response, HttpSession session,String userId) {
		String statusMsg = "";
		Integer statusCode = 200;
		Object data = null;
		if (userId==null ) {
			statusMsg = " 请求参数为空！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		UserVo userVo = this.userService.getById(Integer.parseInt(userId));
		//修改用户的tb_status的状态为禁用
		if(userVo!=null) {
			userVo.setTbStatus("禁用");
			User user = new User();
			BeanUtils.copyProperties(userVo, user);
			this.userService.update(user);
		}else {
			statusMsg = "没有查询到该用户！";
		}
		return webResponse.getWebResponse(statusCode, statusMsg, data);
	}

	//注销用户
	@RequestMapping(value = "/cancel", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse cancel(HttpServletRequest request, HttpServletResponse response, HttpSession session,String nickname,String password) {
		String statusMsg = "";
		Integer statusCode = 200;
		Object data = null;
		statusMsg="注销成功！";
		session.removeAttribute("user");
		return webResponse.getWebResponse(statusCode, statusMsg, data);
	}

	//增加用户
    @RequestMapping(value = "/addUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse addUser(HttpServletRequest request, HttpServletResponse response, HttpSession session, String nickname, String password, String email, String phoneNumber, String salt, String level, String headImg) {
		Object data = null;
		String statusMsg = "";
		Integer statusCode = 200;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("nickname", nickname);
		paramMap.put("password", password);
		paramMap.put("email", email);
		paramMap.put("phoneNumber", phoneNumber);
		paramMap.put("salt", salt);
		paramMap.put("level", level);
		paramMap.put("headImg", headImg);
		data = paramMap;
		if (nickname == null || "".equals(nickname.trim()) || password == null || "".equals(password.trim())) {
			statusMsg = " 参数为空错误！！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		if (nickname.length() > 100 || password.length() > 100) {
			statusMsg = " 参数长度过长错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
        condition.put("phone_number='"+phoneNumber+ "'", "");
        if(this.userService.getOne(condition)!=null){
        	statusMsg = " 此手机号已被注册";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
        }
		String tbStatus = "normal";
		User user = new User();

		boolean isAdd = true;
		return this.addOrEditUser(request, response, session, data, user,nickname,password,email,phoneNumber,salt,level,headImg,tbStatus, isAdd);
	}


	@RequestMapping(value = "/editUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse editUser(HttpServletRequest request, HttpServletResponse response, HttpSession session, String userId, @RequestParam(required = false) String nickname, @RequestParam(required = false) String password, @RequestParam(required = false) String email, @RequestParam(required = false) String phoneNumber, @RequestParam(required = false) String salt, @RequestParam(required = false) String level, @RequestParam(required = false) String headImg, @RequestParam(required = false) String tbStatus) {
		Object data = null;
		String statusMsg = "";
		Integer statusCode = 200;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userId", userId);
		paramMap.put("nickname", nickname);
		paramMap.put("password", password);
		paramMap.put("email", email);
		paramMap.put("phoneNumber", phoneNumber);
		paramMap.put("salt", salt);
		paramMap.put("level", level);
		paramMap.put("headImg", headImg);
		paramMap.put("tbStatus", tbStatus);
		data = paramMap;
		if (userId == null || "".equals(userId.trim())) {
			statusMsg = "未获得主键参数错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		Integer userIdNumeri = userId.matches("^[0-9]*$") ? Integer.parseInt(userId) : 0;
		if (userIdNumeri == 0) {
			statusMsg = "主键不为数字错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		UserVo userVo = this.userService.getById(userIdNumeri);
		User user = new User();
		BeanUtils.copyProperties(userVo, user);
		UserCookie userCookie = this.userUtils.getLoginUser(request, response, session);
		if (userCookie == null) {
			statusMsg = "请登录！";
			statusCode = 201;
			data = statusMsg;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}

		boolean isAdd = false;
		return this.addOrEditUser(request, response, session, data, user,nickname,password,email,phoneNumber,salt,level,headImg,tbStatus, isAdd);
	}


private WebResponse addOrEditUser(HttpServletRequest request, HttpServletResponse response, HttpSession session, Object data, User user, String nickname, String password, String email, String phoneNumber, String salt, String level, String headImg, String tbStatus, boolean isAdd) {
		String statusMsg = "";
		Integer statusCode = 200;
		if (nickname != null && !("".equals(nickname.trim()))) {
			if(nickname.length() > 100) {
				statusMsg = " 参数长度过长错误,nickname";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			user.setNickname(nickname);
		}
		if (email != null && !("".equals(email.trim()))) {
			if(email.length() > 100) {
				statusMsg = " 参数长度过长错误,email";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			user.setEmail(email);
		}
		if (phoneNumber != null && !("".equals(phoneNumber.trim()))) {
			if(phoneNumber.length() > 50) {
				statusMsg = " 参数长度过长错误,phoneNumber";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			user.setPhoneNumber(phoneNumber);
		}
		//生成一个随机数盐值
		Random r = new Random();
		StringBuilder sb = new StringBuilder(16);
		sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
		int len = sb.length();
		if (len < 16) {
			for (int i = 0; i < 16 - len; i++) {
				sb.append("0");
			}
		}
		salt = sb.toString();
		if (salt != null && !("".equals(salt.trim()))) {
			if(salt.length() > 50) {
				statusMsg = " 参数长度过长错误,salt";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			user.setSalt(salt);
		}
		if (password != null && !("".equals(password.trim()))) {
			if(password.length() > 100) {
				statusMsg = " 参数长度过长错误,password";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			}
			//MD5加密密码
			String pwd=this.md5Utils.md5Hex(password+salt);
			//加密后翻入
			user.setPassword(pwd);
		}
		if (level != null && !("".equals(level.trim()))) {
			if(level.length() > 50) {
				statusMsg = " 参数长度过长错误,level";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			user.setLevel(level);
		}
		if (headImg != null && !("".equals(headImg.trim()))) {
			if(headImg.length() > 50) {
				statusMsg = " 参数长度过长错误,headImg";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			user.setHeadImg(headImg);
		}
		if (tbStatus != null && !("".equals(tbStatus.trim()))) {
			if(tbStatus.length() > 50) {
				statusMsg = " 参数长度过长错误,tbStatus";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			user.setTbStatus(tbStatus);
		}
		if (isAdd) {
			this.userService.insert(user);
			if (user.getUserId() > 0) {
				statusMsg = "成功插入！！！";
			} else {
				statusCode = 202;
				statusMsg = "insert false";
			} 
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		int num = this.userService.update(user);
		if (num > 0) {
			statusMsg = "成功修改！！！";
		} else {
			statusCode = 202;
			statusMsg = "update false";
		}
		return webResponse.getWebResponse(statusCode, statusMsg, data);
	}


	@RequestMapping(value = "/getUserById", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getUserById(String userId) {
		Object data = userId;
		Integer statusCode = 200;
		String statusMsg = "";
		if (userId == null || userId.length() == 0 || userId.length() > 11) {
			statusMsg = "参数为空或参数过长错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		Integer userIdNumNumeri = userId.matches("^[0-9]*$") ? Integer.parseInt(userId) : 0;
		if (userIdNumNumeri == 0 ) {
			statusMsg = "参数数字型错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		UserVo userVo = this.userService.getById(userIdNumNumeri);
		if (userVo != null && userVo.getUserId() > 0) {
			data = userVo;
			statusMsg = "获取单条数据成功！！！";
		} else {
			statusCode = 202;
			statusMsg = "no record!!!";
		}
		return webResponse.getWebResponse(statusCode, statusMsg, data);
	}


	@RequestMapping(value = "/getOneUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getOneUser(@RequestParam(defaultValue = "正常", required = false) String tbStatus) {
		LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
		condition.put("tb_status='" + tbStatus + "'", "");
		UserVo userVo = this.userService.getOne(condition);
		Object data = null;
		String statusMsg = "";
		if (userVo != null && userVo.getUserId() > 0) {
			data = userVo;
			statusMsg = "根据条件获取单条数据成功！！！";
		} else {
			statusMsg = "no record!!!";
		}
		return webResponse.getWebResponse(statusMsg, data);
	}

	@RequestMapping(value = "/getUserList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getUserList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
		@RequestParam(defaultValue = "1", required = false) Integer pageNo,  
		@RequestParam(defaultValue = "10", required = false) Integer pageSize, 
		@RequestParam(defaultValue = "正常", required = false) String tbStatus, 
		@RequestParam(required = false) String keyword, 
		@RequestParam(defaultValue = "user_id", required = false) String order,
		@RequestParam(defaultValue = "desc", required = false) String desc ) {
		Object data = null;
		String statusMsg = "";
		int statusCode = 200;
		LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
		if (tbStatus != null && tbStatus.length() > 0) {
			condition.put("tb_status='" + tbStatus + "'", "and");
		}
		if (keyword != null && keyword.length() > 0) {
			StringBuffer buf = new StringBuffer();
			buf.append("(");
			buf.append("nickname like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("password like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("email like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("phone_number like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("salt like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("level like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("head_img like '%").append(keyword).append("%'");
			buf.append(")");
			condition.put(buf.toString(), "and");
		}
		String field = null;
		if (condition.size() > 0) {
			condition.put(condition.entrySet().iterator().next().getKey(), "");
		}
		int count = this.userService.getCount(condition, field);
		if (order != null && order.length() > 0 & "desc".equals(desc)) {
			order = order + " desc";
		}
		List<UserVo> list = this.userService.getList(condition, pageNo, pageSize, order, field);
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("total", count);
		int size = list.size();
		if (size > 0) {
			List<UserVoFont> listFont = new ArrayList<UserVoFont>();
			UserVo vo;
			UserVoFont voFont = new UserVoFont(); 
			for (int i = 0; i < size; i++) {
				vo = list.get(i);
				BeanUtils.copyProperties(vo, voFont);
				listFont.add(voFont);
				voFont = new UserVoFont();
			}
			map.put("list", listFont);
			data = map;
			statusMsg = "根据条件获取分页数据成功！！！";
		} else {
			map.put("list", list);
			data = map;
			statusCode = 202;
			statusMsg = "no record!!!";
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		return webResponse.getWebResponse(statusCode, statusMsg, data);
	}

	@RequestMapping(value = "/getAdminUserList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getAdminUserList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
		@RequestParam(defaultValue = "1", required = false) Integer pageNo,  
		@RequestParam(defaultValue = "10", required = false) Integer pageSize, 
		@RequestParam(defaultValue = "正常", required = false) String tbStatus, 
		@RequestParam(required = false) String keyword, 
		@RequestParam(defaultValue = "user_id", required = false) String order,
		@RequestParam(defaultValue = "desc", required = false) String desc ) {
		Object data = null;
		String statusMsg = "";
		int statusCode = 200;
		LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
		UserCookie userCookie = this.userUtils.getLoginUser(request, response, session);
		if (userCookie == null) {
			statusMsg = "请登录！";
			statusCode = 201;
			data = statusMsg;
			return JSON.toJSONString(data);
		}

		if (tbStatus != null && tbStatus.length() > 0) {
			condition.put("tb_status='" + tbStatus + "'", "and");
		}
		if (keyword != null && keyword.length() > 0) {
			StringBuffer buf = new StringBuffer();
			buf.append("(");
			buf.append("nickname like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("password like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("email like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("phone_number like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("salt like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("level like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("head_img like '%").append(keyword).append("%'");
			buf.append(")");
			condition.put(buf.toString(), "and");
		}
		String field = null;
		if (condition.size() > 0) {
			condition.put(condition.entrySet().iterator().next().getKey(), "");
		}
		int count = this.userService.getCount(condition, field);
		if (order != null && order.length() > 0 & "desc".equals(desc)) {
			order = order + " desc";
		}
		List<UserVo> list = this.userService.getList(condition, pageNo, pageSize, order, field);
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("total", count);
		int size = list.size();
		if (size > 0) {
			map.put("list", list);
			data = map;
			statusMsg = "根据条件获取分页数据成功！！！";
		} else {
			map.put("list", list);
			data = map;
			statusCode = 202;
			statusMsg = "no record!!!";
		}
		return JSON.toJSONString(data);
	}

	@RequestMapping(value = "/delUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse delUser(int id) {
		int num = this.userService.delBySign(id);;
		Object data = null;
		String statusMsg = "";
		if (num > 0) {
			statusMsg = "成功删除！！！";
		} else {
			statusMsg = "no record!!!";
		}
		return webResponse.getWebResponse(statusMsg, data);
	}

}

