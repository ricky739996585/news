
package com.kjz.www.admin.controller.base;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

import java.math.BigDecimal;

import com.kjz.www.common.WebResponse;
import com.kjz.www.admin.service.IAdminService;
import com.kjz.www.admin.domain.Admin;
import com.kjz.www.admin.vo.AdminVo;
import com.kjz.www.admin.vo.AdminVoFont;
import com.kjz.www.utils.AdminUtils;
import com.kjz.www.utils.MD5Utils;
import com.kjz.www.utils.vo.AdminCookie;


@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	protected WebResponse webResponse;

	@Resource
	protected AdminUtils adminUtils;
	
	@Resource
	protected MD5Utils md5Utils;

	@Resource
	protected IAdminService adminService;

	@RequestMapping(value = "/addOrEditAdmin", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse addOrEditAdmin(HttpServletRequest request, HttpServletResponse response, HttpSession session, String adminId, @RequestParam(required = false) String adminPassword, @RequestParam(required = false) String adminAccount, @RequestParam(required = false) String adminName, @RequestParam(required = false) String adminSex, @RequestParam(required = false) String adminIp, @RequestParam(required = false) String adminRegisterTime, @RequestParam(required = false) String adminLastLogin, @RequestParam(required = false) String adminLoginTimes, @RequestParam(required = false) String adminLevel, @RequestParam(required = false) String tbStatus) {
		if (adminId == null || adminId.length() == 0) {
			return this.addAdmin(request, response, session, adminPassword, adminAccount, adminName, adminSex, adminIp, adminRegisterTime, adminLastLogin, adminLoginTimes, adminLevel);
		} else {
			return this.editAdmin(request, response, session, adminId, adminPassword, adminAccount, adminName, adminSex, adminIp, adminRegisterTime, adminLastLogin, adminLoginTimes, adminLevel, tbStatus);
		}
	}

	//增加管理员
	@RequestMapping(value = "/addAdmin", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse addAdmin(HttpServletRequest request, HttpServletResponse response, HttpSession session, String adminPassword, String adminAccount, String adminName, String adminSex, String adminIp, String adminRegisterTime, String adminLastLogin, String adminLoginTimes, String adminLevel) {
		Object data = null;
		String statusMsg = "";
		Integer statusCode = 200;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("adminPassword", adminPassword);
		paramMap.put("adminAccount", adminAccount);
		paramMap.put("adminName", adminName);
		paramMap.put("adminSex", adminSex);
		paramMap.put("adminIp", adminIp);
		paramMap.put("adminRegisterTime", adminRegisterTime);
		paramMap.put("adminLastLogin", adminLastLogin);
		paramMap.put("adminLoginTimes", adminLoginTimes);
		paramMap.put("adminLevel", adminLevel);
		data = paramMap;
		if (adminPassword == null || "".equals(adminPassword.trim()) || adminAccount == null || "".equals(adminAccount.trim()) || adminName == null || "".equals(adminName.trim()) || adminSex == null || "".equals(adminSex.trim()) || adminIp == null || "".equals(adminIp.trim()) || adminRegisterTime == null || "".equals(adminRegisterTime.trim()) || adminLastLogin == null || "".equals(adminLastLogin.trim()) || adminLoginTimes == null || "".equals(adminLoginTimes.trim()) || adminLevel == null || "".equals(adminLevel.trim())) {
			statusMsg = " 参数为空错误！！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		if (adminPassword.length() > 100 || adminAccount.length() > 100 || adminName.length() > 100 || adminSex.length() > 10 || adminIp.length() > 100 || adminRegisterTime.length() > 19 || adminLastLogin.length() > 19 || adminLevel.length() > 100) {
			statusMsg = " 参数长度过长错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
        condition.put("admin_account='"+adminAccount+ "'", "");
        AdminVo adminVo = this.adminService.getOne(condition);//查询是否已有此账号
		if(adminVo!=null){
			statusMsg = " 此账号已被占用";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		String tbStatus = "normal";
		Admin admin = new Admin();


		boolean isAdd = true;
		return this.addOrEditAdmin(request, response, session, data, admin,adminPassword,adminAccount,adminName,adminSex,adminIp,adminRegisterTime,adminLastLogin,adminLoginTimes,adminLevel,tbStatus, isAdd);
	}


	@RequestMapping(value = "/editAdmin", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse editAdmin(HttpServletRequest request, HttpServletResponse response, HttpSession session, String adminId, @RequestParam(required = false) String adminPassword, @RequestParam(required = false) String adminAccount, @RequestParam(required = false) String adminName, @RequestParam(required = false) String adminSex, @RequestParam(required = false) String adminIp, @RequestParam(required = false) String adminRegisterTime, @RequestParam(required = false) String adminLastLogin, @RequestParam(required = false) String adminLoginTimes, @RequestParam(required = false) String adminLevel, @RequestParam(required = false) String tbStatus) {
		Object data = null;
		String statusMsg = "";
		Integer statusCode = 200;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("adminId", adminId);
		paramMap.put("adminPassword", adminPassword);
		paramMap.put("adminAccount", adminAccount);
		paramMap.put("adminName", adminName);
		paramMap.put("adminSex", adminSex);
		paramMap.put("adminIp", adminIp);
		paramMap.put("adminRegisterTime", adminRegisterTime);
		paramMap.put("adminLastLogin", adminLastLogin);
		paramMap.put("adminLoginTimes", adminLoginTimes);
		paramMap.put("adminLevel", adminLevel);
		paramMap.put("tbStatus", tbStatus);
		data = paramMap;
		if (adminId == null || "".equals(adminId.trim())) {
			statusMsg = "未获得主键参数错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		Integer adminIdNumeri = adminId.matches("^[0-9]*$") ? Integer.parseInt(adminId) : 0;
		if (adminIdNumeri == 0) {
			statusMsg = "主键不为数字错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		AdminVo adminVo = this.adminService.getById(adminIdNumeri);
		Admin admin = new Admin();
		BeanUtils.copyProperties(adminVo, admin);
		AdminCookie adminCookie = this.adminUtils.getLoginAdmin(request, response, session);
		if (adminCookie == null) {
			statusMsg = "请登录！";
			statusCode = 201;
			data = statusMsg;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}

		boolean isAdd = false;
		return this.addOrEditAdmin(request, response, session, data, admin,adminPassword,adminAccount,adminName,adminSex,adminIp,adminRegisterTime,adminLastLogin,adminLoginTimes,adminLevel,tbStatus, isAdd);
	}


private WebResponse addOrEditAdmin(HttpServletRequest request, HttpServletResponse response, HttpSession session, Object data, Admin admin, String adminPassword, String adminAccount, String adminName, String adminSex, String adminIp, String adminRegisterTime, String adminLastLogin, String adminLoginTimes, String adminLevel, String tbStatus, boolean isAdd) {
		String statusMsg = "";
		Integer statusCode = 200;
		if (adminPassword != null && !("".equals(adminPassword.trim()))) {
			if(adminPassword.length() > 100) {
				statusMsg = " 参数长度过长错误,adminPassword";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			//加密密码
			String pwd=this.md5Utils.md5Hex(adminPassword);
			admin.setAdminPassword(pwd);
		}
		if (adminAccount != null && !("".equals(adminAccount.trim()))) {
			if(adminAccount.length() > 100) {
				statusMsg = " 参数长度过长错误,adminAccount";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			admin.setAdminAccount(adminAccount);
		}
		if (adminName != null && !("".equals(adminName.trim()))) {
			if(adminName.length() > 100) {
				statusMsg = " 参数长度过长错误,adminName";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			admin.setAdminName(adminName);
		}
		if (adminSex != null && !("".equals(adminSex.trim()))) {
			if(adminSex.length() > 10) {
				statusMsg = " 参数长度过长错误,adminSex";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			admin.setAdminSex(adminSex);
		}
		if (adminIp != null && !("".equals(adminIp.trim()))) {
			if(adminIp.length() > 100) {
				statusMsg = " 参数长度过长错误,adminIp";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			admin.setAdminIp(adminIp);
		}
		if (adminRegisterTime != null && !("".equals(adminRegisterTime.trim()))) {
			if(adminRegisterTime.length() > 19) {
				statusMsg = " 参数长度过长错误,adminRegisterTime";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			admin.setAdminRegisterTime(adminRegisterTime);
		}
		if (adminLastLogin != null && !("".equals(adminLastLogin.trim()))) {
			if(adminLastLogin.length() > 19) {
				statusMsg = " 参数长度过长错误,adminLastLogin";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			admin.setAdminLastLogin(adminLastLogin);
		}
		Integer adminLoginTimesNumeri = 0;
		if (adminLoginTimes != null && !("".equals(adminLoginTimes.trim()))) {
			if (!adminLoginTimes.matches("^[0-9]*$")) {
				statusMsg = " 参数数字型错误！！！不能为0,adminLoginTimes";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			}
			adminLoginTimesNumeri = Integer.parseInt(adminLoginTimes);
			admin.setAdminLoginTimes(adminLoginTimesNumeri);
		}
		if (adminLevel != null && !("".equals(adminLevel.trim()))) {
			if(adminLevel.length() > 100) {
				statusMsg = " 参数长度过长错误,adminLevel";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			admin.setAdminLevel(adminLevel);
		}
		if (tbStatus != null && !("".equals(tbStatus.trim()))) {
			if(tbStatus.length() > 50) {
				statusMsg = " 参数长度过长错误,tbStatus";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			admin.setTbStatus(tbStatus);
		}
		if (isAdd) {
			this.adminService.insert(admin);
			if (admin.getAdminId() > 0) {
				statusMsg = "成功插入！！！";
			} else {
				statusCode = 202;
				statusMsg = "insert false";
			} 
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		int num = this.adminService.update(admin);
		if (num > 0) {
			statusMsg = "成功修改！！！";
		} else {
			statusCode = 202;
			statusMsg = "update false";
		}
		return webResponse.getWebResponse(statusCode, statusMsg, data);
	}


	@RequestMapping(value = "/getAdminById", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getAdminById(String adminId) {
		Object data = adminId;
		Integer statusCode = 200;
		String statusMsg = "";
		if (adminId == null || adminId.length() == 0 || adminId.length() > 11) {
			statusMsg = "参数为空或参数过长错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		Integer adminIdNumNumeri = adminId.matches("^[0-9]*$") ? Integer.parseInt(adminId) : 0;
		if (adminIdNumNumeri == 0 ) {
			statusMsg = "参数数字型错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		AdminVo adminVo = this.adminService.getById(adminIdNumNumeri);
		if (adminVo != null && adminVo.getAdminId() > 0) {
			data = adminVo;
			statusMsg = "获取单条数据成功！！！";
		} else {
			statusCode = 202;
			statusMsg = "no record!!!";
		}
		return webResponse.getWebResponse(statusCode, statusMsg, data);
	}


	@RequestMapping(value = "/getOneAdmin", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getOneAdmin(@RequestParam(defaultValue = "正常", required = false) String tbStatus) {
		LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
		condition.put("tb_status='" + tbStatus + "'", "");
		AdminVo adminVo = this.adminService.getOne(condition);
		Object data = null;
		String statusMsg = "";
		if (adminVo != null && adminVo.getAdminId() > 0) {
			data = adminVo;
			statusMsg = "根据条件获取单条数据成功！！！";
		} else {
			statusMsg = "no record!!!";
		}
		return webResponse.getWebResponse(statusMsg, data);
	}

	@RequestMapping(value = "/getAdminList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getAdminList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
		@RequestParam(defaultValue = "1", required = false) Integer pageNo,  
		@RequestParam(defaultValue = "10", required = false) Integer pageSize, 
		@RequestParam(defaultValue = "正常", required = false) String tbStatus, 
		@RequestParam(required = false) String keyword, 
		@RequestParam(defaultValue = "admin_id", required = false) String order,
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
			buf.append("admin_password like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("admin_account like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("admin_name like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("admin_sex like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("admin_ip like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("admin_register_time like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("admin_last_login like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("admin_level like '%").append(keyword).append("%'");
			buf.append(")");
			condition.put(buf.toString(), "and");
		}
		String field = null;
		if (condition.size() > 0) {
			condition.put(condition.entrySet().iterator().next().getKey(), "");
		}
		int count = this.adminService.getCount(condition, field);
		if (order != null && order.length() > 0 & "desc".equals(desc)) {
			order = order + " desc";
		}
		List<AdminVo> list = this.adminService.getList(condition, pageNo, pageSize, order, field);
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("total", count);
		int size = list.size();
		if (size > 0) {
			List<AdminVoFont> listFont = new ArrayList<AdminVoFont>();
			AdminVo vo;
			AdminVoFont voFont = new AdminVoFont(); 
			for (int i = 0; i < size; i++) {
				vo = list.get(i);
				BeanUtils.copyProperties(vo, voFont);
				listFont.add(voFont);
				voFont = new AdminVoFont();
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

	@RequestMapping(value = "/getAdminAdminList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getAdminAdminList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
		@RequestParam(defaultValue = "1", required = false) Integer pageNo,  
		@RequestParam(defaultValue = "10", required = false) Integer pageSize, 
		@RequestParam(defaultValue = "正常", required = false) String tbStatus, 
		@RequestParam(required = false) String keyword, 
		@RequestParam(defaultValue = "admin_id", required = false) String order,
		@RequestParam(defaultValue = "desc", required = false) String desc ) {
		Object data = null;
		String statusMsg = "";
		int statusCode = 200;
		LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
		AdminCookie adminCookie = this.adminUtils.getLoginAdmin(request, response, session);
		if (adminCookie == null) {
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
			buf.append("admin_password like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("admin_account like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("admin_name like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("admin_sex like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("admin_ip like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("admin_register_time like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("admin_last_login like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("admin_level like '%").append(keyword).append("%'");
			buf.append(")");
			condition.put(buf.toString(), "and");
		}
		String field = null;
		if (condition.size() > 0) {
			condition.put(condition.entrySet().iterator().next().getKey(), "");
		}
		int count = this.adminService.getCount(condition, field);
		if (order != null && order.length() > 0 & "desc".equals(desc)) {
			order = order + " desc";
		}
		List<AdminVo> list = this.adminService.getList(condition, pageNo, pageSize, order, field);
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

	@RequestMapping(value = "/delAdmin", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse delAdmin(int id) {
		int num = this.adminService.delBySign(id);;
		Object data = null;
		String statusMsg = "";
		if (num > 0) {
			statusMsg = "成功删除！！！";
		} else {
			statusMsg = "no record!!!";
		}
		return webResponse.getWebResponse(statusMsg, data);
	}

	//管理员登陆
    @RequestMapping(value = "/login", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse login(HttpServletRequest request, HttpServletResponse response, HttpSession session,String adminaccount,String password) throws Exception{
        String statusMsg = "";
        Integer statusCode = 200;
        LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
        condition.put("admin_account='"+adminaccount+ "'", "");
        AdminVo adminVo = this.adminService.getOne(condition);
        Object data = null;
        data=adminVo;
        //Map<String, String> resultMap = new HashMap<String, String>();
        if (adminaccount.length() > 100 || password.length() > 100 ) {
            statusMsg = " 参数长度过长错误！！！";
            statusCode = 201;
            return webResponse.getWebResponse(statusCode, statusMsg, data);
        }
        //若数据库有此管理员
        if(adminVo!=null) {
        	
        	String pwd=this.md5Utils.encodeByMd5(password);//前端输入的密码值
        	
            if(pwd.equals(adminVo.getAdminPassword())) {
                statusMsg = "登录成功！！！";
                this.adminUtils.putAdminInSession(request,response,session,adminVo);
            }
        }else {
            statusMsg = "登录失败";
        }
        return webResponse.getWebResponse(statusCode, statusMsg, data);
    }
    
    //管理员登陆注销
  	@RequestMapping(value = "/exitAdmin", produces = "application/json;charset=UTF-8")
  	@ResponseBody
  	public WebResponse exitAdmin(HttpServletRequest request, HttpServletResponse response, HttpSession session,String nickname,String password) {
  		String statusMsg = "";
  		Integer statusCode = 200;
  		Object data = null;
  		statusMsg="注销成功！";
  		session.removeAttribute("admin");
  		return webResponse.getWebResponse(statusCode, statusMsg, data);
  	}
}

