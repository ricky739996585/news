
package com.kjz.www.photo.controller.base;
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
import com.kjz.www.photo.service.IPhotoService;
import com.kjz.www.photo.domain.Photo;
import com.kjz.www.photo.vo.PhotoVo;
import com.kjz.www.photo.vo.PhotoVoFont;
import com.kjz.www.utils.UserUtils;
import com.kjz.www.utils.vo.UserCookie;

@Controller
@RequestMapping("/photo")
public class PhotoController {

	@Autowired
	protected WebResponse webResponse;

	@Resource
	protected UserUtils userUtils;

	@Resource
	protected IPhotoService photoService;

	@RequestMapping(value = "/addOrEditPhoto", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse addOrEditPhoto(HttpServletRequest request, HttpServletResponse response, HttpSession session, String photoId, @RequestParam(required = false) String userId, @RequestParam(required = false) String photoName, @RequestParam(required = false) String photoSrc, @RequestParam(required = false) String photoDescription, @RequestParam(required = false) String photoTypeName, @RequestParam(required = false) String tbStatus) {
		if (photoId == null || photoId.length() == 0) {
			return this.addPhoto(request, response, session, userId, photoName, photoSrc, photoDescription, photoTypeName);
		} else {
			return this.editPhoto(request, response, session, photoId, userId, photoName, photoSrc, photoDescription, photoTypeName, tbStatus);
		}
	}

	@RequestMapping(value = "/addPhoto", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse addPhoto(HttpServletRequest request, HttpServletResponse response, HttpSession session, String userId, String photoName, String photoSrc, String photoDescription, String photoTypeName) {
		Object data = null;
		String statusMsg = "";
		Integer statusCode = 200;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userId", userId);
		paramMap.put("photoName", photoName);
		paramMap.put("photoSrc", photoSrc);
		paramMap.put("photoDescription", photoDescription);
		paramMap.put("photoTypeName", photoTypeName);
		data = paramMap;
		if (userId == null || "".equals(userId.trim()) || photoName == null || "".equals(photoName.trim()) || photoSrc == null || "".equals(photoSrc.trim()) || photoDescription == null || "".equals(photoDescription.trim()) || photoTypeName == null || "".equals(photoTypeName.trim())) {
			statusMsg = " 参数为空错误！！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		if (photoName.length() > 50 || photoSrc.length() > 50 || photoDescription.length() > 65535 || photoTypeName.length() > 255) {
			statusMsg = " 参数长度过长错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		String tbStatus = "normal";
		Photo photo = new Photo();
		UserCookie userCookie = this.userUtils.getLoginUser(request, response, session);
		if (userCookie == null) {
			statusMsg = "请登录！";
			statusCode = 201;
			data = statusMsg;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}

		boolean isAdd = true;
		return this.addOrEditPhoto(request, response, session, data, photo,userId,photoName,photoSrc,photoDescription,photoTypeName,tbStatus, isAdd);
	}


	@RequestMapping(value = "/editPhoto", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse editPhoto(HttpServletRequest request, HttpServletResponse response, HttpSession session, String photoId, @RequestParam(required = false) String userId, @RequestParam(required = false) String photoName, @RequestParam(required = false) String photoSrc, @RequestParam(required = false) String photoDescription, @RequestParam(required = false) String photoTypeName, @RequestParam(required = false) String tbStatus) {
		Object data = null;
		String statusMsg = "";
		Integer statusCode = 200;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("photoId", photoId);
		paramMap.put("userId", userId);
		paramMap.put("photoName", photoName);
		paramMap.put("photoSrc", photoSrc);
		paramMap.put("photoDescription", photoDescription);
		paramMap.put("photoTypeName", photoTypeName);
		paramMap.put("tbStatus", tbStatus);
		data = paramMap;
		if (photoId == null || "".equals(photoId.trim())) {
			statusMsg = "未获得主键参数错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		Integer photoIdNumeri = photoId.matches("^[0-9]*$") ? Integer.parseInt(photoId) : 0;
		if (photoIdNumeri == 0) {
			statusMsg = "主键不为数字错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		PhotoVo photoVo = this.photoService.getById(photoIdNumeri);
		Photo photo = new Photo();
		BeanUtils.copyProperties(photoVo, photo);
		UserCookie userCookie = this.userUtils.getLoginUser(request, response, session);
		if (userCookie == null) {
			statusMsg = "请登录！";
			statusCode = 201;
			data = statusMsg;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}

		boolean isAdd = false;
		return this.addOrEditPhoto(request, response, session, data, photo,userId,photoName,photoSrc,photoDescription,photoTypeName,tbStatus, isAdd);
	}


private WebResponse addOrEditPhoto(HttpServletRequest request, HttpServletResponse response, HttpSession session, Object data, Photo photo, String userId, String photoName, String photoSrc, String photoDescription, String photoTypeName, String tbStatus, boolean isAdd) {
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
			photo.setUserId(userIdNumeri);
		}
		if (photoName != null && !("".equals(photoName.trim()))) {
			if(photoName.length() > 50) {
				statusMsg = " 参数长度过长错误,photoName";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			photo.setPhotoName(photoName);
		}
		if (photoSrc != null && !("".equals(photoSrc.trim()))) {
			if(photoSrc.length() > 50) {
				statusMsg = " 参数长度过长错误,photoSrc";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			photo.setPhotoSrc(photoSrc);
		}
		if (photoDescription != null && !("".equals(photoDescription.trim()))) {
			if(photoDescription.length() > 65535) {
				statusMsg = " 参数长度过长错误,photoDescription";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			photo.setPhotoDescription(photoDescription);
		}
		if (photoTypeName != null && !("".equals(photoTypeName.trim()))) {
			if(photoTypeName.length() > 255) {
				statusMsg = " 参数长度过长错误,photoTypeName";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			photo.setPhotoTypeName(photoTypeName);
		}
		if (tbStatus != null && !("".equals(tbStatus.trim()))) {
			if(tbStatus.length() > 50) {
				statusMsg = " 参数长度过长错误,tbStatus";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			photo.setTbStatus(tbStatus);
		}
		if (isAdd) {
			this.photoService.insert(photo);
			if (photo.getPhotoId() > 0) {
				statusMsg = "成功插入！！！";
			} else {
				statusCode = 202;
				statusMsg = "insert false";
			} 
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		int num = this.photoService.update(photo);
		if (num > 0) {
			statusMsg = "成功修改！！！";
		} else {
			statusCode = 202;
			statusMsg = "update false";
		}
		return webResponse.getWebResponse(statusCode, statusMsg, data);
	}


	@RequestMapping(value = "/getPhotoById", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getPhotoById(String photoId) {
		Object data = photoId;
		Integer statusCode = 200;
		String statusMsg = "";
		if (photoId == null || photoId.length() == 0 || photoId.length() > 11) {
			statusMsg = "参数为空或参数过长错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		Integer photoIdNumNumeri = photoId.matches("^[0-9]*$") ? Integer.parseInt(photoId) : 0;
		if (photoIdNumNumeri == 0 ) {
			statusMsg = "参数数字型错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		PhotoVo photoVo = this.photoService.getById(photoIdNumNumeri);
		if (photoVo != null && photoVo.getPhotoId() > 0) {
			data = photoVo;
			statusMsg = "获取单条数据成功！！！";
		} else {
			statusCode = 202;
			statusMsg = "no record!!!";
		}
		return webResponse.getWebResponse(statusCode, statusMsg, data);
	}


	@RequestMapping(value = "/getOnePhoto", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getOnePhoto(@RequestParam(defaultValue = "正常", required = false) String tbStatus) {
		LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
		condition.put("tb_status='" + tbStatus + "'", "");
		PhotoVo photoVo = this.photoService.getOne(condition);
		Object data = null;
		String statusMsg = "";
		if (photoVo != null && photoVo.getPhotoId() > 0) {
			data = photoVo;
			statusMsg = "根据条件获取单条数据成功！！！";
		} else {
			statusMsg = "no record!!!";
		}
		return webResponse.getWebResponse(statusMsg, data);
	}

	@RequestMapping(value = "/getPhotoList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getPhotoList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
		@RequestParam(defaultValue = "1", required = false) Integer pageNo,  
		@RequestParam(defaultValue = "10", required = false) Integer pageSize, 
		@RequestParam(defaultValue = "正常", required = false) String tbStatus, 
		@RequestParam(required = false) String keyword, 
		@RequestParam(defaultValue = "photo_id", required = false) String order,
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
			buf.append("photo_name like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("photo_src like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("photo_description like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("photo_type_name like '%").append(keyword).append("%'");
			buf.append(")");
			condition.put(buf.toString(), "and");
		}
		String field = null;
		if (condition.size() > 0) {
			condition.put(condition.entrySet().iterator().next().getKey(), "");
		}
		int count = this.photoService.getCount(condition, field);
		if (order != null && order.length() > 0 & "desc".equals(desc)) {
			order = order + " desc";
		}
		List<PhotoVo> list = this.photoService.getList(condition, pageNo, pageSize, order, field);
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("total", count);
		int size = list.size();
		if (size > 0) {
			List<PhotoVoFont> listFont = new ArrayList<PhotoVoFont>();
			PhotoVo vo;
			PhotoVoFont voFont = new PhotoVoFont(); 
			for (int i = 0; i < size; i++) {
				vo = list.get(i);
				BeanUtils.copyProperties(vo, voFont);
				listFont.add(voFont);
				voFont = new PhotoVoFont();
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

	@RequestMapping(value = "/getAdminPhotoList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getAdminPhotoList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
		@RequestParam(defaultValue = "1", required = false) Integer pageNo,  
		@RequestParam(defaultValue = "10", required = false) Integer pageSize, 
		@RequestParam(defaultValue = "正常", required = false) String tbStatus, 
		@RequestParam(required = false) String keyword, 
		@RequestParam(defaultValue = "photo_id", required = false) String order,
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
			buf.append("photo_name like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("photo_src like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("photo_description like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("photo_type_name like '%").append(keyword).append("%'");
			buf.append(")");
			condition.put(buf.toString(), "and");
		}
		String field = null;
		if (condition.size() > 0) {
			condition.put(condition.entrySet().iterator().next().getKey(), "");
		}
		int count = this.photoService.getCount(condition, field);
		if (order != null && order.length() > 0 & "desc".equals(desc)) {
			order = order + " desc";
		}
		List<PhotoVo> list = this.photoService.getList(condition, pageNo, pageSize, order, field);
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

	@RequestMapping(value = "/delPhoto", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse delPhoto(int id) {
		int num = this.photoService.delBySign(id);;
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

