
package com.kjz.www.user.controller.base;

import com.alibaba.fastjson.JSON;
import com.kjz.www.common.WebResponse;
import com.kjz.www.user.domain.UserRelation;
import com.kjz.www.user.service.IUserRelationService;
import com.kjz.www.user.vo.UserRelationVo;
import com.kjz.www.user.vo.UserRelationVoFont;
import com.kjz.www.utils.UserUtils;
import com.kjz.www.utils.vo.UserCookie;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/userRelation")
public class UserRelationController {

	@Autowired
	protected WebResponse webResponse;

	@Resource
	protected UserUtils userUtils;

	@Resource
	protected IUserRelationService userRelationService;

	@RequestMapping(value = "/addOrEditUserRelation", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse addOrEditUserRelation(HttpServletRequest request, HttpServletResponse response, HttpSession session, String relationId, @RequestParam(required = false) String userId, @RequestParam(required = false) String followerId, @RequestParam(required = false) String relationType, @RequestParam(required = false) String tbStatus) {
		if (relationId == null || relationId.length() == 0) {
			return this.addUserRelation(request, response, session, userId, followerId, relationType);
		} else {
			return this.editUserRelation(request, response, session, relationId, userId, followerId, relationType, tbStatus);
		}
	}

	@RequestMapping(value = "/addUserRelation", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse addUserRelation(HttpServletRequest request, HttpServletResponse response, HttpSession session, String userId, String followerId, String relationType) {
		Object data = null;
		String statusMsg = "";
		Integer statusCode = 200;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userId", userId);
		paramMap.put("followerId", followerId);
		paramMap.put("relationType", relationType);
		data = paramMap;
		if (userId == null || "".equals(userId.trim()) || followerId == null || "".equals(followerId.trim()) || relationType == null || "".equals(relationType.trim())) {
			statusMsg = " 参数为空错误！！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		if (relationType.length() > 50) {
			statusMsg = " 参数长度过长错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		String tbStatus = "normal";
		UserRelation userRelation = new UserRelation();

		UserCookie userCookie = this.userUtils.getLoginUser(request, response, session);
		if (userCookie == null) {
			statusMsg = "请登录！";
			statusCode = 201;
			data = statusMsg;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}

		boolean isAdd = true;
		return this.addOrEditUserRelation(request, response, session, data, userRelation,userId,followerId,relationType,tbStatus, isAdd);
	}


	@RequestMapping(value = "/editUserRelation", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse editUserRelation(HttpServletRequest request, HttpServletResponse response, HttpSession session, String relationId, @RequestParam(required = false) String userId, @RequestParam(required = false) String followerId, @RequestParam(required = false) String relationType, @RequestParam(required = false) String tbStatus) {
		Object data = null;
		String statusMsg = "";
		Integer statusCode = 200;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("relationId", relationId);
		paramMap.put("userId", userId);
		paramMap.put("followerId", followerId);
		paramMap.put("relationType", relationType);
		paramMap.put("tbStatus", tbStatus);
		data = paramMap;
		if (relationId == null || "".equals(relationId.trim())) {
			statusMsg = "未获得主键参数错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		Integer relationIdNumeri = relationId.matches("^[0-9]*$") ? Integer.parseInt(relationId) : 0;
		if (relationIdNumeri == 0) {
			statusMsg = "主键不为数字错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		UserRelationVo userRelationVo = this.userRelationService.getById(relationIdNumeri);
		UserRelation userRelation = new UserRelation();
		BeanUtils.copyProperties(userRelationVo, userRelation);
		UserCookie userCookie = this.userUtils.getLoginUser(request, response, session);
		if (userCookie == null) {
			statusMsg = "请登录！";
			statusCode = 201;
			data = statusMsg;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}

		boolean isAdd = false;
		return this.addOrEditUserRelation(request, response, session, data, userRelation,userId,followerId,relationType,tbStatus, isAdd);
	}


private WebResponse addOrEditUserRelation(HttpServletRequest request, HttpServletResponse response, HttpSession session, Object data, UserRelation userRelation, String userId, String followerId, String relationType, String tbStatus, boolean isAdd) {
		String statusMsg = "";
		Integer statusCode = 200;
		Integer userIdNumeri = 0;
		if (userId != null && !("".equals(userId.trim()))) {
			if (!userId.matches("^[0-9]*$")) {
				statusMsg = " 参数数字型错误！！！不能为0,userId";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			}
			userIdNumeri = Integer.parseInt(userId);
			userRelation.setUserId(userIdNumeri);
		}
		Integer followerIdNumeri = 0;
		if (followerId != null && !("".equals(followerId.trim()))) {
			if (!followerId.matches("^[0-9]*$")) {
				statusMsg = " 参数数字型错误！！！不能为0,followerId";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			}
			followerIdNumeri = Integer.parseInt(followerId);
			userRelation.setFollowerId(followerIdNumeri);
		}
		if (relationType != null && !("".equals(relationType.trim()))) {
			if(relationType.length() > 50) {
				statusMsg = " 参数长度过长错误,relationType";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			userRelation.setRelationType(relationType);
		}
		if (tbStatus != null && !("".equals(tbStatus.trim()))) {
			if(tbStatus.length() > 50) {
				statusMsg = " 参数长度过长错误,tbStatus";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			userRelation.setTbStatus(tbStatus);
		}
		if (isAdd) {
			this.userRelationService.insert(userRelation);
			if (userRelation.getRelationId() > 0) {
				statusMsg = "成功插入！！！";
			} else {
				statusCode = 202;
				statusMsg = "insert false";
			} 
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		int num = this.userRelationService.update(userRelation);
		if (num > 0) {
			statusMsg = "成功修改！！！";
		} else {
			statusCode = 202;
			statusMsg = "update false";
		}
		return webResponse.getWebResponse(statusCode, statusMsg, data);
	}


	@RequestMapping(value = "/getUserRelationById", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getUserRelationById(String relationId) {
		Object data = relationId;
		Integer statusCode = 200;
		String statusMsg = "";
		if (relationId == null || relationId.length() == 0 || relationId.length() > 11) {
			statusMsg = "参数为空或参数过长错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		Integer relationIdNumNumeri = relationId.matches("^[0-9]*$") ? Integer.parseInt(relationId) : 0;
		if (relationIdNumNumeri == 0 ) {
			statusMsg = "参数数字型错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		UserRelationVo userRelationVo = this.userRelationService.getById(relationIdNumNumeri);
		if (userRelationVo != null && userRelationVo.getRelationId() > 0) {
			data = userRelationVo;
			statusMsg = "获取单条数据成功！！！";
		} else {
			statusCode = 202;
			statusMsg = "no record!!!";
		}
		return webResponse.getWebResponse(statusCode, statusMsg, data);
	}


	@RequestMapping(value = "/getOneUserRelation", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getOneUserRelation(@RequestParam(defaultValue = "正常", required = false) String tbStatus) {
		LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
		condition.put("tb_status='" + tbStatus + "'", "");
		UserRelationVo userRelationVo = this.userRelationService.getOne(condition);
		Object data = null;
		String statusMsg = "";
		if (userRelationVo != null && userRelationVo.getRelationId() > 0) {
			data = userRelationVo;
			statusMsg = "根据条件获取单条数据成功！！！";
		} else {
			statusMsg = "no record!!!";
		}
		return webResponse.getWebResponse(statusMsg, data);
	}

	@RequestMapping(value = "/getUserRelationList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getUserRelationList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
                                           @RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                           @RequestParam(defaultValue = "10", required = false) Integer pageSize,
                                           @RequestParam(defaultValue = "正常", required = false) String tbStatus,
                                           @RequestParam(required = false) String keyword,
                                           @RequestParam(defaultValue = "relation_Id", required = false) String order,
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
			buf.append("relation_type like '%").append(keyword).append("%'");
			buf.append(")");
			condition.put(buf.toString(), "and");
		}
		String field = null;
		if (condition.size() > 0) {
			condition.put(condition.entrySet().iterator().next().getKey(), "");
		}
		int count = this.userRelationService.getCount(condition, field);
		if (order != null && order.length() > 0 & "desc".equals(desc)) {
			order = order + " desc";
		}
		List<UserRelationVo> list = this.userRelationService.getList(condition, pageNo, pageSize, order, field);
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("total", count);
		int size = list.size();
		if (size > 0) {
			List<UserRelationVoFont> listFont = new ArrayList<UserRelationVoFont>();
			UserRelationVo vo;
			UserRelationVoFont voFont = new UserRelationVoFont();
			for (int i = 0; i < size; i++) {
				vo = list.get(i);
				BeanUtils.copyProperties(vo, voFont);
				listFont.add(voFont);
				voFont = new UserRelationVoFont();
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

	@RequestMapping(value = "/getAdminUserRelationList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getAdminUserRelationList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
		@RequestParam(defaultValue = "1", required = false) Integer pageNo,  
		@RequestParam(defaultValue = "10", required = false) Integer pageSize, 
		@RequestParam(defaultValue = "正常", required = false) String tbStatus, 
		@RequestParam(required = false) String keyword, 
		@RequestParam(defaultValue = "relation_Id", required = false) String order,
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
			buf.append("relation_type like '%").append(keyword).append("%'");
			buf.append(")");
			condition.put(buf.toString(), "and");
		}
		String field = null;
		if (condition.size() > 0) {
			condition.put(condition.entrySet().iterator().next().getKey(), "");
		}
		int count = this.userRelationService.getCount(condition, field);
		if (order != null && order.length() > 0 & "desc".equals(desc)) {
			order = order + " desc";
		}
		List<UserRelationVo> list = this.userRelationService.getList(condition, pageNo, pageSize, order, field);
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

	@RequestMapping(value = "/delUserRelation", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse delUserRelation(int id) {
		int num = this.userRelationService.delBySign(id);;
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

