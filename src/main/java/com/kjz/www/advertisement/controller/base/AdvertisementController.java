
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
import com.kjz.www.advertisement.service.IAdvertisementService;
import com.kjz.www.advertisement.domain.Advertisement;
import com.kjz.www.advertisement.vo.AdvertisementVo;
import com.kjz.www.advertisement.vo.AdvertisementVoFont;
import com.kjz.www.utils.UserUtils;
import com.kjz.www.utils.vo.UserCookie;

@Controller
@RequestMapping("/advertisement")
public class AdvertisementController {

	@Autowired
	protected WebResponse webResponse;

	@Resource
	protected UserUtils userUtils;

	@Resource
	protected IAdvertisementService advertisementService;

	@RequestMapping(value = "/addOrEditAdvertisement", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse addOrEditAdvertisement(HttpServletRequest request, HttpServletResponse response, HttpSession session, String advertisementId, @RequestParam(required = false) String title, @RequestParam(required = false) String pic, @RequestParam(required = false) String targetUrl, @RequestParam(required = false) String orderNum, @RequestParam(required = false) String advertisementType, @RequestParam(required = false) String status) {
		if (advertisementId == null || advertisementId.length() == 0) {
			return this.addAdvertisement(request, response, session, title, pic, targetUrl, orderNum, advertisementType, status);
		} else {
			return this.editAdvertisement(request, response, session, advertisementId, title, pic, targetUrl, orderNum, advertisementType, status);
		}
	}

	@RequestMapping(value = "/addAdvertisement", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse addAdvertisement(HttpServletRequest request, HttpServletResponse response, HttpSession session, String title, String pic, String targetUrl, String orderNum, String advertisementType, String status) {
		Object data = null;
		String statusMsg = "";
		Integer statusCode = 200;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("title", title);
		paramMap.put("pic", pic);
		paramMap.put("targetUrl", targetUrl);
		paramMap.put("orderNum", orderNum);
		paramMap.put("advertisementType", advertisementType);
		paramMap.put("status", status);
		data = paramMap;
		if (title == null || "".equals(title.trim()) || pic == null || "".equals(pic.trim()) || targetUrl == null || "".equals(targetUrl.trim()) || orderNum == null || "".equals(orderNum.trim()) || advertisementType == null || "".equals(advertisementType.trim()) || status == null || "".equals(status.trim())) {
			statusMsg = " 参数为空错误！！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		if (title.length() > 100 || pic.length() > 245 || targetUrl.length() > 600 || advertisementType.length() > 100 || status.length() > 20) {
			statusMsg = " 参数长度过长错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		Advertisement advertisement = new Advertisement();

		UserCookie userCookie = this.userUtils.getLoginUser(request, response, session);
		if (userCookie == null) {
			statusMsg = "请登录！";
			statusCode = 201;
			data = statusMsg;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}

		boolean isAdd = true;
		return this.addOrEditAdvertisement(request, response, session, data, advertisement,title,pic,targetUrl,orderNum,advertisementType,status, isAdd);
	}


	@RequestMapping(value = "/editAdvertisement", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse editAdvertisement(HttpServletRequest request, HttpServletResponse response, HttpSession session, String advertisementId, @RequestParam(required = false) String title, @RequestParam(required = false) String pic, @RequestParam(required = false) String targetUrl, @RequestParam(required = false) String orderNum, @RequestParam(required = false) String advertisementType, @RequestParam(required = false) String status) {
		Object data = null;
		String statusMsg = "";
		Integer statusCode = 200;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("advertisementId", advertisementId);
		paramMap.put("title", title);
		paramMap.put("pic", pic);
		paramMap.put("targetUrl", targetUrl);
		paramMap.put("orderNum", orderNum);
		paramMap.put("advertisementType", advertisementType);
		paramMap.put("status", status);
		data = paramMap;
		if (advertisementId == null || "".equals(advertisementId.trim())) {
			statusMsg = "未获得主键参数错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		Integer advertisementIdNumeri = advertisementId.matches("^[0-9]*$") ? Integer.parseInt(advertisementId) : 0;
		if (advertisementIdNumeri == 0) {
			statusMsg = "主键不为数字错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		AdvertisementVo advertisementVo = this.advertisementService.getById(advertisementIdNumeri);
		Advertisement advertisement = new Advertisement();
		BeanUtils.copyProperties(advertisementVo, advertisement);
		UserCookie userCookie = this.userUtils.getLoginUser(request, response, session);
		if (userCookie == null) {
			statusMsg = "请登录！";
			statusCode = 201;
			data = statusMsg;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}

		boolean isAdd = false;
		return this.addOrEditAdvertisement(request, response, session, data, advertisement,title,pic,targetUrl,orderNum,advertisementType,status, isAdd);
	}


private WebResponse addOrEditAdvertisement(HttpServletRequest request, HttpServletResponse response, HttpSession session, Object data, Advertisement advertisement, String title, String pic, String targetUrl, String orderNum, String advertisementType, String status, boolean isAdd) {
		String statusMsg = "";
		Integer statusCode = 200;
		if (title != null && !("".equals(title.trim()))) {
			if(title.length() > 100) {
				statusMsg = " 参数长度过长错误,title";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			advertisement.setTitle(title);
		}
		if (pic != null && !("".equals(pic.trim()))) {
			if(pic.length() > 245) {
				statusMsg = " 参数长度过长错误,pic";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			advertisement.setPic(pic);
		}
		if (targetUrl != null && !("".equals(targetUrl.trim()))) {
			if(targetUrl.length() > 600) {
				statusMsg = " 参数长度过长错误,targetUrl";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			advertisement.setTargetUrl(targetUrl);
		}
		Integer orderNumNumeri = 0;
		if (orderNum != null && !("".equals(orderNum.trim()))) {
			if (!orderNum.matches("^[0-9]*$")) {
				statusMsg = " 参数数字型错误！！！不能为0,orderNum";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			}
			orderNumNumeri = Integer.parseInt(orderNum);
			advertisement.setOrderNum(orderNumNumeri);
		}
		if (advertisementType != null && !("".equals(advertisementType.trim()))) {
			if(advertisementType.length() > 100) {
				statusMsg = " 参数长度过长错误,advertisementType";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			advertisement.setAdvertisementType(advertisementType);
		}
		if (status != null && !("".equals(status.trim()))) {
			if(status.length() > 20) {
				statusMsg = " 参数长度过长错误,status";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			advertisement.setStatus(status);
		}
		if (isAdd) {
			this.advertisementService.insert(advertisement);
			if (advertisement.getAdvertisementId() > 0) {
				statusMsg = "成功插入！！！";
			} else {
				statusCode = 202;
				statusMsg = "insert false";
			} 
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		int num = this.advertisementService.update(advertisement);
		if (num > 0) {
			statusMsg = "成功修改！！！";
		} else {
			statusCode = 202;
			statusMsg = "update false";
		}
		return webResponse.getWebResponse(statusCode, statusMsg, data);
	}


	@RequestMapping(value = "/getAdvertisementById", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getAdvertisementById(String advertisementId) {
		Object data = advertisementId;
		Integer statusCode = 200;
		String statusMsg = "";
		if (advertisementId == null || advertisementId.length() == 0 || advertisementId.length() > 11) {
			statusMsg = "参数为空或参数过长错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		Integer advertisementIdNumNumeri = advertisementId.matches("^[0-9]*$") ? Integer.parseInt(advertisementId) : 0;
		if (advertisementIdNumNumeri == 0 ) {
			statusMsg = "参数数字型错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		AdvertisementVo advertisementVo = this.advertisementService.getById(advertisementIdNumNumeri);
		if (advertisementVo != null && advertisementVo.getAdvertisementId() > 0) {
			data = advertisementVo;
			statusMsg = "获取单条数据成功！！！";
		} else {
			statusCode = 202;
			statusMsg = "no record!!!";
		}
		return webResponse.getWebResponse(statusCode, statusMsg, data);
	}


	@RequestMapping(value = "/getOneAdvertisement", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getOneAdvertisement(@RequestParam(defaultValue = "正常", required = false) String tbStatus) {
		LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
		condition.put("tb_status='" + tbStatus + "'", "");
		AdvertisementVo advertisementVo = this.advertisementService.getOne(condition);
		Object data = null;
		String statusMsg = "";
		if (advertisementVo != null && advertisementVo.getAdvertisementId() > 0) {
			data = advertisementVo;
			statusMsg = "根据条件获取单条数据成功！！！";
		} else {
			statusMsg = "no record!!!";
		}
		return webResponse.getWebResponse(statusMsg, data);
	}

	@RequestMapping(value = "/getAdvertisementList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getAdvertisementList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
		@RequestParam(defaultValue = "1", required = false) Integer pageNo,  
		@RequestParam(defaultValue = "10", required = false) Integer pageSize, 
		@RequestParam(defaultValue = "正常", required = false) String tbStatus, 
		@RequestParam(required = false) String keyword, 
		@RequestParam(defaultValue = "advertisement_id", required = false) String order,
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
			buf.append("title like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("pic like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("target_url like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("advertisement_type like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("status like '%").append(keyword).append("%'");
			buf.append(")");
			condition.put(buf.toString(), "and");
		}
		String field = null;
		if (condition.size() > 0) {
			condition.put(condition.entrySet().iterator().next().getKey(), "");
		}
		int count = this.advertisementService.getCount(condition, field);
		if (order != null && order.length() > 0 & "desc".equals(desc)) {
			order = order + " desc";
		}
		List<AdvertisementVo> list = this.advertisementService.getList(condition, pageNo, pageSize, order, field);
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("total", count);
		int size = list.size();
		if (size > 0) {
			List<AdvertisementVoFont> listFont = new ArrayList<AdvertisementVoFont>();
			AdvertisementVo vo;
			AdvertisementVoFont voFont = new AdvertisementVoFont(); 
			for (int i = 0; i < size; i++) {
				vo = list.get(i);
				BeanUtils.copyProperties(vo, voFont);
				listFont.add(voFont);
				voFont = new AdvertisementVoFont();
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

	@RequestMapping(value = "/getAdminAdvertisementList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getAdminAdvertisementList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
		@RequestParam(defaultValue = "1", required = false) Integer pageNo,  
		@RequestParam(defaultValue = "10", required = false) Integer pageSize, 
		@RequestParam(defaultValue = "正常", required = false) String tbStatus, 
		@RequestParam(required = false) String keyword, 
		@RequestParam(defaultValue = "advertisement_id", required = false) String order,
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
			buf.append("title like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("pic like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("target_url like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("advertisement_type like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("status like '%").append(keyword).append("%'");
			buf.append(")");
			condition.put(buf.toString(), "and");
		}
		String field = null;
		if (condition.size() > 0) {
			condition.put(condition.entrySet().iterator().next().getKey(), "");
		}
		int count = this.advertisementService.getCount(condition, field);
		if (order != null && order.length() > 0 & "desc".equals(desc)) {
			order = order + " desc";
		}
		List<AdvertisementVo> list = this.advertisementService.getList(condition, pageNo, pageSize, order, field);
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

	@RequestMapping(value = "/delAdvertisement", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse delAdvertisement(int id) {
		int num = this.advertisementService.delBySign(id);;
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

