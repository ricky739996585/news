
package com.kjz.www.advertisement.controller.base;
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
import com.kjz.www.advertisement.service.IAdvertisementTypeService;
import com.kjz.www.advertisement.domain.AdvertisementType;
import com.kjz.www.advertisement.vo.AdvertisementTypeVo;
import com.kjz.www.advertisement.vo.AdvertisementTypeVoFont;
import com.kjz.www.utils.UserUtils;
import com.kjz.www.utils.vo.UserCookie;

@Controller
@RequestMapping("/advertisementType")
public class AdvertisementTypeController {

	@Autowired
	protected WebResponse webResponse;

	@Resource
	protected UserUtils userUtils;

	@Resource
	protected IAdvertisementTypeService advertisementTypeService;

	@RequestMapping(value = "/addOrEditAdvertisementType", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse addOrEditAdvertisementType(HttpServletRequest request, HttpServletResponse response, HttpSession session, String advertisementTypeId, @RequestParam(required = false) String typeName, @RequestParam(required = false) String orderNum, @RequestParam(required = false) String tbstatus) {
		if (advertisementTypeId == null || advertisementTypeId.length() == 0) {
			return this.addAdvertisementType(request, response, session, typeName, orderNum, tbstatus);
		} else {
			return this.editAdvertisementType(request, response, session, advertisementTypeId, typeName, orderNum, tbstatus);
		}
	}

	@RequestMapping(value = "/addAdvertisementType", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse addAdvertisementType(HttpServletRequest request, HttpServletResponse response, HttpSession session, String typeName, String orderNum, String tbstatus) {
		Object data = null;
		String statusMsg = "";
		Integer statusCode = 200;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("typeName", typeName);
		paramMap.put("orderNum", orderNum);
		paramMap.put("tbstatus", tbstatus);
		data = paramMap;
		if (typeName == null || "".equals(typeName.trim()) || orderNum == null || "".equals(orderNum.trim()) || tbstatus == null || "".equals(tbstatus.trim())) {
			statusMsg = " 参数为空错误！！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		if (typeName.length() > 200 || tbstatus.length() > 50) {
			statusMsg = " 参数长度过长错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		AdvertisementType advertisementType = new AdvertisementType();

		UserCookie userCookie = this.userUtils.getLoginUser(request, response, session);
		if (userCookie == null) {
			statusMsg = "请登录！";
			statusCode = 201;
			data = statusMsg;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}

		boolean isAdd = true;
		return this.addOrEditAdvertisementType(request, response, session, data, advertisementType,typeName,orderNum,tbstatus, isAdd);
	}


	@RequestMapping(value = "/editAdvertisementType", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse editAdvertisementType(HttpServletRequest request, HttpServletResponse response, HttpSession session, String advertisementTypeId, @RequestParam(required = false) String typeName, @RequestParam(required = false) String orderNum, @RequestParam(required = false) String tbstatus) {
		Object data = null;
		String statusMsg = "";
		Integer statusCode = 200;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("advertisementTypeId", advertisementTypeId);
		paramMap.put("typeName", typeName);
		paramMap.put("orderNum", orderNum);
		paramMap.put("tbstatus", tbstatus);
		data = paramMap;
		if (advertisementTypeId == null || "".equals(advertisementTypeId.trim())) {
			statusMsg = "未获得主键参数错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		Integer advertisementTypeIdNumeri = advertisementTypeId.matches("^[0-9]*$") ? Integer.parseInt(advertisementTypeId) : 0;
		if (advertisementTypeIdNumeri == 0) {
			statusMsg = "主键不为数字错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		AdvertisementTypeVo advertisementTypeVo = this.advertisementTypeService.getById(advertisementTypeIdNumeri);
		AdvertisementType advertisementType = new AdvertisementType();
		BeanUtils.copyProperties(advertisementTypeVo, advertisementType);
		UserCookie userCookie = this.userUtils.getLoginUser(request, response, session);
		if (userCookie == null) {
			statusMsg = "请登录！";
			statusCode = 201;
			data = statusMsg;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}

		boolean isAdd = false;
		return this.addOrEditAdvertisementType(request, response, session, data, advertisementType,typeName,orderNum,tbstatus, isAdd);
	}


private WebResponse addOrEditAdvertisementType(HttpServletRequest request, HttpServletResponse response, HttpSession session, Object data, AdvertisementType advertisementType, String typeName, String orderNum, String tbstatus, boolean isAdd) {
		String statusMsg = "";
		Integer statusCode = 200;
		if (typeName != null && !("".equals(typeName.trim()))) {
			if(typeName.length() > 200) {
				statusMsg = " 参数长度过长错误,typeName";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			advertisementType.setTypeName(typeName);
		}
		Integer orderNumNumeri = 0;
		if (orderNum != null && !("".equals(orderNum.trim()))) {
			if (!orderNum.matches("^[0-9]*$")) {
				statusMsg = " 参数数字型错误！！！不能为0,orderNum";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			}
			orderNumNumeri = Integer.parseInt(orderNum);
			advertisementType.setOrderNum(orderNumNumeri);
		}
		if (tbstatus != null && !("".equals(tbstatus.trim()))) {
			if(tbstatus.length() > 50) {
				statusMsg = " 参数长度过长错误,status";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			advertisementType.setTbstatus(tbstatus);
		}
		if (isAdd) {
			this.advertisementTypeService.insert(advertisementType);
			if (advertisementType.getAdvertisementTypeId() > 0) {
				statusMsg = "成功插入！！！";
			} else {
				statusCode = 202;
				statusMsg = "insert false";
			} 
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		int num = this.advertisementTypeService.update(advertisementType);
		if (num > 0) {
			statusMsg = "成功修改！！！";
		} else {
			statusCode = 202;
			statusMsg = "update false";
		}
		return webResponse.getWebResponse(statusCode, statusMsg, data);
	}


	@RequestMapping(value = "/getAdvertisementTypeById", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getAdvertisementTypeById(String advertisementTypeId) {
		Object data = advertisementTypeId;
		Integer statusCode = 200;
		String statusMsg = "";
		if (advertisementTypeId == null || advertisementTypeId.length() == 0 || advertisementTypeId.length() > 11) {
			statusMsg = "参数为空或参数过长错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		Integer advertisementTypeIdNumNumeri = advertisementTypeId.matches("^[0-9]*$") ? Integer.parseInt(advertisementTypeId) : 0;
		if (advertisementTypeIdNumNumeri == 0 ) {
			statusMsg = "参数数字型错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		AdvertisementTypeVo advertisementTypeVo = this.advertisementTypeService.getById(advertisementTypeIdNumNumeri);
		if (advertisementTypeVo != null && advertisementTypeVo.getAdvertisementTypeId() > 0) {
			data = advertisementTypeVo;
			statusMsg = "获取单条数据成功！！！";
		} else {
			statusCode = 202;
			statusMsg = "no record!!!";
		}
		return webResponse.getWebResponse(statusCode, statusMsg, data);
	}


	@RequestMapping(value = "/getOneAdvertisementType", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getOneAdvertisementType(@RequestParam(defaultValue = "正常", required = false) String tbStatus) {
		LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
		condition.put("tb_status='" + tbStatus + "'", "");
		AdvertisementTypeVo advertisementTypeVo = this.advertisementTypeService.getOne(condition);
		Object data = null;
		String statusMsg = "";
		if (advertisementTypeVo != null && advertisementTypeVo.getAdvertisementTypeId() > 0) {
			data = advertisementTypeVo;
			statusMsg = "根据条件获取单条数据成功！！！";
		} else {
			statusMsg = "no record!!!";
		}
		return webResponse.getWebResponse(statusMsg, data);
	}

	@RequestMapping(value = "/getAdvertisementTypeList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getAdvertisementTypeList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
		@RequestParam(defaultValue = "1", required = false) Integer pageNo,  
		@RequestParam(defaultValue = "10", required = false) Integer pageSize, 
		@RequestParam(defaultValue = "正常", required = false) String tbStatus, 
		@RequestParam(required = false) String keyword, 
		@RequestParam(defaultValue = "advertisement_type_id", required = false) String order,
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
			buf.append("type_name like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("status like '%").append(keyword).append("%'");
			buf.append(")");
			condition.put(buf.toString(), "and");
		}
		String field = null;
		if (condition.size() > 0) {
			condition.put(condition.entrySet().iterator().next().getKey(), "");
		}
		int count = this.advertisementTypeService.getCount(condition, field);
		if (order != null && order.length() > 0 & "desc".equals(desc)) {
			order = order + " desc";
		}
		List<AdvertisementTypeVo> list = this.advertisementTypeService.getList(condition, pageNo, pageSize, order, field);
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("total", count);
		int size = list.size();
		if (size > 0) {
			List<AdvertisementTypeVoFont> listFont = new ArrayList<AdvertisementTypeVoFont>();
			AdvertisementTypeVo vo;
			AdvertisementTypeVoFont voFont = new AdvertisementTypeVoFont(); 
			for (int i = 0; i < size; i++) {
				vo = list.get(i);
				BeanUtils.copyProperties(vo, voFont);
				listFont.add(voFont);
				voFont = new AdvertisementTypeVoFont();
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

	@RequestMapping(value = "/getAdminAdvertisementTypeList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getAdminAdvertisementTypeList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
		@RequestParam(defaultValue = "1", required = false) Integer pageNo,  
		@RequestParam(defaultValue = "10", required = false) Integer pageSize, 
		@RequestParam(defaultValue = "正常", required = false) String tbStatus, 
		@RequestParam(required = false) String keyword, 
		@RequestParam(defaultValue = "advertisement_type_id", required = false) String order,
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
			buf.append("type_name like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("status like '%").append(keyword).append("%'");
			buf.append(")");
			condition.put(buf.toString(), "and");
		}
		String field = null;
		if (condition.size() > 0) {
			condition.put(condition.entrySet().iterator().next().getKey(), "");
		}
		int count = this.advertisementTypeService.getCount(condition, field);
		if (order != null && order.length() > 0 & "desc".equals(desc)) {
			order = order + " desc";
		}
		List<AdvertisementTypeVo> list = this.advertisementTypeService.getList(condition, pageNo, pageSize, order, field);
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

	@RequestMapping(value = "/delAdvertisementType", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse delAdvertisementType(int id) {
		int num = this.advertisementTypeService.delBySign(id);;
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

