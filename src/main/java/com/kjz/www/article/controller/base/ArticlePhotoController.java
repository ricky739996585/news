
package com.kjz.www.article.controller.base;
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
import com.kjz.www.article.service.IArticlePhotoService;
import com.kjz.www.article.domain.ArticlePhoto;
import com.kjz.www.article.vo.ArticlePhotoVo;
import com.kjz.www.article.vo.ArticlePhotoVoFont;
import com.kjz.www.utils.UserUtils;
import com.kjz.www.utils.vo.UserCookie;

@Controller
@RequestMapping("/articlePhoto")
public class ArticlePhotoController {

	@Autowired
	protected WebResponse webResponse;

	@Resource
	protected UserUtils userUtils;

	@Resource
	protected IArticlePhotoService articlePhotoService;

	@RequestMapping(value = "/addOrEditArticlePhoto", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse addOrEditArticlePhoto(HttpServletRequest request, HttpServletResponse response, HttpSession session, String articlePhotoId, @RequestParam(required = false) String articleId, @RequestParam(required = false) String articlePhotoUrl, @RequestParam(required = false) String tbStatus) {
		if (articlePhotoId == null || articlePhotoId.length() == 0) {
			return this.addArticlePhoto(request, response, session, articleId, articlePhotoUrl);
		} else {
			return this.editArticlePhoto(request, response, session, articlePhotoId, articleId, articlePhotoUrl, tbStatus);
		}
	}

	@RequestMapping(value = "/addArticlePhoto", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse addArticlePhoto(HttpServletRequest request, HttpServletResponse response, HttpSession session, String articleId, String articlePhotoUrl) {
		Object data = null;
		String statusMsg = "";
		Integer statusCode = 200;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("articleId", articleId);
		paramMap.put("articlePhotoUrl", articlePhotoUrl);
		data = paramMap;
		if (articleId == null || "".equals(articleId.trim()) || articlePhotoUrl == null || "".equals(articlePhotoUrl.trim())) {
			statusMsg = " 参数为空错误！！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		if (articlePhotoUrl.length() > 65535) {
			statusMsg = " 参数长度过长错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		String tbStatus = "normal";
		ArticlePhoto articlePhoto = new ArticlePhoto();

		UserCookie userCookie = this.userUtils.getLoginUser(request, response, session);
		if (userCookie == null) {
			statusMsg = "请登录！";
			statusCode = 201;
			data = statusMsg;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}

		boolean isAdd = true;
		return this.addOrEditArticlePhoto(request, response, session, data, articlePhoto,articleId,articlePhotoUrl,tbStatus, isAdd);
	}


	@RequestMapping(value = "/editArticlePhoto", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse editArticlePhoto(HttpServletRequest request, HttpServletResponse response, HttpSession session, String articlePhotoId, @RequestParam(required = false) String articleId, @RequestParam(required = false) String articlePhotoUrl, @RequestParam(required = false) String tbStatus) {
		Object data = null;
		String statusMsg = "";
		Integer statusCode = 200;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("articlePhotoId", articlePhotoId);
		paramMap.put("articleId", articleId);
		paramMap.put("articlePhotoUrl", articlePhotoUrl);
		paramMap.put("tbStatus", tbStatus);
		data = paramMap;
		if (articlePhotoId == null || "".equals(articlePhotoId.trim())) {
			statusMsg = "未获得主键参数错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		Integer articlePhotoIdNumeri = articlePhotoId.matches("^[0-9]*$") ? Integer.parseInt(articlePhotoId) : 0;
		if (articlePhotoIdNumeri == 0) {
			statusMsg = "主键不为数字错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		ArticlePhotoVo articlePhotoVo = this.articlePhotoService.getById(articlePhotoIdNumeri);
		ArticlePhoto articlePhoto = new ArticlePhoto();
		BeanUtils.copyProperties(articlePhotoVo, articlePhoto);
		UserCookie userCookie = this.userUtils.getLoginUser(request, response, session);
		if (userCookie == null) {
			statusMsg = "请登录！";
			statusCode = 201;
			data = statusMsg;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}

		boolean isAdd = false;
		return this.addOrEditArticlePhoto(request, response, session, data, articlePhoto,articleId,articlePhotoUrl,tbStatus, isAdd);
	}


private WebResponse addOrEditArticlePhoto(HttpServletRequest request, HttpServletResponse response, HttpSession session, Object data, ArticlePhoto articlePhoto, String articleId, String articlePhotoUrl, String tbStatus, boolean isAdd) {
		String statusMsg = "";
		Integer statusCode = 200;
		Integer articleIdNumeri = 0;
		if (articleId != null && !("".equals(articleId.trim()))) {
			if (!articleId.matches("^[0-9]*$")) {
				statusMsg = " 参数数字型错误！！！不能为0,articleId";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			}
			articleIdNumeri = Integer.parseInt(articleId);
			articlePhoto.setArticleId(articleIdNumeri);
		}
		if (articlePhotoUrl != null && !("".equals(articlePhotoUrl.trim()))) {
			if(articlePhotoUrl.length() > 65535) {
				statusMsg = " 参数长度过长错误,articlePhotoUrl";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			articlePhoto.setArticlePhotoUrl(articlePhotoUrl);
		}
		if (tbStatus != null && !("".equals(tbStatus.trim()))) {
			if(tbStatus.length() > 50) {
				statusMsg = " 参数长度过长错误,tbStatus";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			articlePhoto.setTbStatus(tbStatus);
		}
		if (isAdd) {
			this.articlePhotoService.insert(articlePhoto);
			if (articlePhoto.getArticlePhotoId() > 0) {
				statusMsg = "成功插入！！！";
			} else {
				statusCode = 202;
				statusMsg = "insert false";
			} 
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		int num = this.articlePhotoService.update(articlePhoto);
		if (num > 0) {
			statusMsg = "成功修改！！！";
		} else {
			statusCode = 202;
			statusMsg = "update false";
		}
		return webResponse.getWebResponse(statusCode, statusMsg, data);
	}


	@RequestMapping(value = "/getArticlePhotoById", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getArticlePhotoById(String articlePhotoId) {
		Object data = articlePhotoId;
		Integer statusCode = 200;
		String statusMsg = "";
		if (articlePhotoId == null || articlePhotoId.length() == 0 || articlePhotoId.length() > 11) {
			statusMsg = "参数为空或参数过长错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		Integer articlePhotoIdNumNumeri = articlePhotoId.matches("^[0-9]*$") ? Integer.parseInt(articlePhotoId) : 0;
		if (articlePhotoIdNumNumeri == 0 ) {
			statusMsg = "参数数字型错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		ArticlePhotoVo articlePhotoVo = this.articlePhotoService.getById(articlePhotoIdNumNumeri);
		if (articlePhotoVo != null && articlePhotoVo.getArticlePhotoId() > 0) {
			data = articlePhotoVo;
			statusMsg = "获取单条数据成功！！！";
		} else {
			statusCode = 202;
			statusMsg = "no record!!!";
		}
		return webResponse.getWebResponse(statusCode, statusMsg, data);
	}


	@RequestMapping(value = "/getOneArticlePhoto", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getOneArticlePhoto(@RequestParam(defaultValue = "正常", required = false) String tbStatus) {
		LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
		condition.put("tb_status='" + tbStatus + "'", "");
		ArticlePhotoVo articlePhotoVo = this.articlePhotoService.getOne(condition);
		Object data = null;
		String statusMsg = "";
		if (articlePhotoVo != null && articlePhotoVo.getArticlePhotoId() > 0) {
			data = articlePhotoVo;
			statusMsg = "根据条件获取单条数据成功！！！";
		} else {
			statusMsg = "no record!!!";
		}
		return webResponse.getWebResponse(statusMsg, data);
	}

	@RequestMapping(value = "/getArticlePhotoList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getArticlePhotoList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
		@RequestParam(defaultValue = "1", required = false) Integer pageNo,  
		@RequestParam(defaultValue = "10", required = false) Integer pageSize, 
		@RequestParam(defaultValue = "正常", required = false) String tbStatus, 
		@RequestParam(required = false) String keyword, 
		@RequestParam(defaultValue = "article_photo_id", required = false) String order,
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
			buf.append("article_photo_url like '%").append(keyword).append("%'");
			buf.append(")");
			condition.put(buf.toString(), "and");
		}
		String field = null;
		if (condition.size() > 0) {
			condition.put(condition.entrySet().iterator().next().getKey(), "");
		}
		int count = this.articlePhotoService.getCount(condition, field);
		if (order != null && order.length() > 0 & "desc".equals(desc)) {
			order = order + " desc";
		}
		List<ArticlePhotoVo> list = this.articlePhotoService.getList(condition, pageNo, pageSize, order, field);
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("total", count);
		int size = list.size();
		if (size > 0) {
			List<ArticlePhotoVoFont> listFont = new ArrayList<ArticlePhotoVoFont>();
			ArticlePhotoVo vo;
			ArticlePhotoVoFont voFont = new ArticlePhotoVoFont(); 
			for (int i = 0; i < size; i++) {
				vo = list.get(i);
				BeanUtils.copyProperties(vo, voFont);
				listFont.add(voFont);
				voFont = new ArticlePhotoVoFont();
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

	@RequestMapping(value = "/getAdminArticlePhotoList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getAdminArticlePhotoList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
		@RequestParam(defaultValue = "1", required = false) Integer pageNo,  
		@RequestParam(defaultValue = "10", required = false) Integer pageSize, 
		@RequestParam(defaultValue = "正常", required = false) String tbStatus, 
		@RequestParam(required = false) String keyword, 
		@RequestParam(defaultValue = "article_photo_id", required = false) String order,
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
			buf.append("article_photo_url like '%").append(keyword).append("%'");
			buf.append(")");
			condition.put(buf.toString(), "and");
		}
		String field = null;
		if (condition.size() > 0) {
			condition.put(condition.entrySet().iterator().next().getKey(), "");
		}
		int count = this.articlePhotoService.getCount(condition, field);
		if (order != null && order.length() > 0 & "desc".equals(desc)) {
			order = order + " desc";
		}
		List<ArticlePhotoVo> list = this.articlePhotoService.getList(condition, pageNo, pageSize, order, field);
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

	@RequestMapping(value = "/delArticlePhoto", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse delArticlePhoto(int id) {
		int num = this.articlePhotoService.delBySign(id);;
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

