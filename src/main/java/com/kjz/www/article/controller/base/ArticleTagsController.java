
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
import com.kjz.www.article.service.IArticleTagsService;
import com.kjz.www.article.domain.ArticleTags;
import com.kjz.www.article.vo.ArticleTagsVo;
import com.kjz.www.article.vo.ArticleTagsVoFont;
import com.kjz.www.utils.UserUtils;
import com.kjz.www.utils.vo.UserCookie;

@Controller
@RequestMapping("/articleTags")
public class ArticleTagsController {

	@Autowired
	protected WebResponse webResponse;

	@Resource
	protected UserUtils userUtils;

	@Resource
	protected IArticleTagsService articleTagsService;

//	@RequestMapping(value = "/addOrEditArticleTags", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//	@ResponseBody
//	public WebResponse addOrEditArticleTags(HttpServletRequest request, HttpServletResponse response, HttpSession session, String articleTagsId, @RequestParam(required = false) String tagsId, @RequestParam(required = false) String articleId, @RequestParam(required = false) String tbStatus) {
//		if (articleTagsId == null || articleTagsId.length() == 0) {
//			return this.addArticleTags(request, response, session, tagsId, articleId);
//		}
//		else {
//			return this.editArticleTags(request, response, session, articleTagsId, tagsId, articleId, tbStatus);
//		}
//	}

	@RequestMapping(value = "/addArticleTags", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse addArticleTags(HttpServletRequest request, HttpServletResponse response, HttpSession session, String[] tagsIds, String articleId) {
		Object data = null;
		String statusMsg = "";
		Integer statusCode = 200;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tagsIds", tagsIds);
		paramMap.put("articleId", articleId);
		data = paramMap;
		if (tagsIds.length <0 || articleId == null || "".equals(articleId.trim())) {
			statusMsg = " 参数为空错误！！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		String tbStatus = "normal";
		ArticleTags articleTags = new ArticleTags();

		UserCookie userCookie = this.userUtils.getLoginUser(request, response, session);
		if (userCookie == null) {
			statusMsg = "请登录！";
			statusCode = 201;
			data = statusMsg;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}

		boolean isAdd = true;
		return this.addOrEditArticleTags(request, response, session, data, articleTags,tagsIds,articleId,tbStatus, isAdd);
	}


//	@RequestMapping(value = "/editArticleTags", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//	@ResponseBody
//	public WebResponse editArticleTags(HttpServletRequest request, HttpServletResponse response, HttpSession session, String articleTagsId, @RequestParam(required = false) String tagsId, @RequestParam(required = false) String articleId, @RequestParam(required = false) String tbStatus) {
//		Object data = null;
//		String statusMsg = "";
//		Integer statusCode = 200;
//		Map<String, String> paramMap = new HashMap<String, String>();
//		paramMap.put("articleTagsId", articleTagsId);
//		paramMap.put("tagsId", tagsId);
//		paramMap.put("articleId", articleId);
//		paramMap.put("tbStatus", tbStatus);
//		data = paramMap;
//		if (articleTagsId == null || "".equals(articleTagsId.trim())) {
//			statusMsg = "未获得主键参数错误！！！";
//			statusCode = 201;
//			return webResponse.getWebResponse(statusCode, statusMsg, data);
//		}
//		Integer articleTagsIdNumeri = articleTagsId.matches("^[0-9]*$") ? Integer.parseInt(articleTagsId) : 0;
//		if (articleTagsIdNumeri == 0) {
//			statusMsg = "主键不为数字错误！！！";
//			statusCode = 201;
//			return webResponse.getWebResponse(statusCode, statusMsg, data);
//		}
//		ArticleTagsVo articleTagsVo = this.articleTagsService.getById(articleTagsIdNumeri);
//		ArticleTags articleTags = new ArticleTags();
//		BeanUtils.copyProperties(articleTagsVo, articleTags);
//		UserCookie userCookie = this.userUtils.getLoginUser(request, response, session);
//		if (userCookie == null) {
//			statusMsg = "请登录！";
//			statusCode = 201;
//			data = statusMsg;
//			return webResponse.getWebResponse(statusCode, statusMsg, data);
//		}
//
//		boolean isAdd = false;
//		return this.addOrEditArticleTags(request, response, session, data, articleTags,tagsId,articleId,tbStatus, isAdd);
//	}


private WebResponse addOrEditArticleTags(HttpServletRequest request, HttpServletResponse response, HttpSession session, Object data, ArticleTags articleTags, String[] tagsIds, String articleId, String tbStatus, boolean isAdd) {
		String statusMsg = "";
		Integer statusCode = 200;
		Integer tagsIdNumeri = 0;
		List<ArticleTags> list=null;
		if (tagsIds.length> 0) {
			for (int i=0;i<tagsIds.length;i++){
				if (!tagsIds[i].matches("^[0-9]*$")) {
					statusMsg = " 参数数字型错误！！！不能为0,tagsId";
					statusCode = 201;
					return webResponse.getWebResponse(statusCode, statusMsg, data);
				}
				ArticleTags tags=new ArticleTags();
				tags.setArticleId(Integer.parseInt(articleId));
				tags.setTagsId(Integer.parseInt(tagsIds[i]));
				list.add(tags);
//				tagsIdNumeri = Integer.parseInt(tagsId);
//				articleTags.setTagsId(tagsIdNumeri);
			}
		}
		Integer articleIdNumeri = 0;
		if (articleId != null && !("".equals(articleId.trim()))) {
			if (!articleId.matches("^[0-9]*$")) {
				statusMsg = " 参数数字型错误！！！不能为0,articleId";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			}
			articleIdNumeri = Integer.parseInt(articleId);
			articleTags.setArticleId(articleIdNumeri);
		}
		if (tbStatus != null && !("".equals(tbStatus.trim()))) {
			if(tbStatus.length() > 50) {
				statusMsg = " 参数长度过长错误,tbStatus";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			articleTags.setTbStatus(tbStatus);
		}
		if (isAdd) {
//			this.articleTagsService.insert(articleTags);
			this.articleTagsService.insertByBatch(list);
			if (articleTags.getArticleTagsId() > 0) {
				statusMsg = "成功插入！！！";
			} else {
				statusCode = 202;
				statusMsg = "insert false";
			} 
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		int num = this.articleTagsService.update(articleTags);
		if (num > 0) {
			statusMsg = "成功修改！！！";
		} else {
			statusCode = 202;
			statusMsg = "update false";
		}
		return webResponse.getWebResponse(statusCode, statusMsg, data);
	}


	@RequestMapping(value = "/getArticleTagsById", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getArticleTagsById(String articleTagsId) {
		Object data = articleTagsId;
		Integer statusCode = 200;
		String statusMsg = "";
		if (articleTagsId == null || articleTagsId.length() == 0 || articleTagsId.length() > 11) {
			statusMsg = "参数为空或参数过长错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		Integer articleTagsIdNumNumeri = articleTagsId.matches("^[0-9]*$") ? Integer.parseInt(articleTagsId) : 0;
		if (articleTagsIdNumNumeri == 0 ) {
			statusMsg = "参数数字型错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		ArticleTagsVo articleTagsVo = this.articleTagsService.getById(articleTagsIdNumNumeri);
		if (articleTagsVo != null && articleTagsVo.getArticleTagsId() > 0) {
			data = articleTagsVo;
			statusMsg = "获取单条数据成功！！！";
		} else {
			statusCode = 202;
			statusMsg = "no record!!!";
		}
		return webResponse.getWebResponse(statusCode, statusMsg, data);
	}


	@RequestMapping(value = "/getOneArticleTags", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getOneArticleTags(@RequestParam(defaultValue = "正常", required = false) String tbStatus) {
		LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
		condition.put("tb_status='" + tbStatus + "'", "");
		ArticleTagsVo articleTagsVo = this.articleTagsService.getOne(condition);
		Object data = null;
		String statusMsg = "";
		if (articleTagsVo != null && articleTagsVo.getArticleTagsId() > 0) {
			data = articleTagsVo;
			statusMsg = "根据条件获取单条数据成功！！！";
		} else {
			statusMsg = "no record!!!";
		}
		return webResponse.getWebResponse(statusMsg, data);
	}

	@RequestMapping(value = "/getArticleTagsList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getArticleTagsList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
		@RequestParam(defaultValue = "1", required = false) Integer pageNo,  
		@RequestParam(defaultValue = "10", required = false) Integer pageSize, 
		@RequestParam(defaultValue = "正常", required = false) String tbStatus, 
		@RequestParam(required = false) String keyword, 
		@RequestParam(defaultValue = "article_tags_id", required = false) String order,
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
			buf.append(")");
			condition.put(buf.toString(), "and");
		}
		String field = null;
		if (condition.size() > 0) {
			condition.put(condition.entrySet().iterator().next().getKey(), "");
		}
		int count = this.articleTagsService.getCount(condition, field);
		if (order != null && order.length() > 0 & "desc".equals(desc)) {
			order = order + " desc";
		}
		List<ArticleTagsVo> list = this.articleTagsService.getList(condition, pageNo, pageSize, order, field);
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("total", count);
		int size = list.size();
		if (size > 0) {
			List<ArticleTagsVoFont> listFont = new ArrayList<ArticleTagsVoFont>();
			ArticleTagsVo vo;
			ArticleTagsVoFont voFont = new ArticleTagsVoFont(); 
			for (int i = 0; i < size; i++) {
				vo = list.get(i);
				BeanUtils.copyProperties(vo, voFont);
				listFont.add(voFont);
				voFont = new ArticleTagsVoFont();
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

	@RequestMapping(value = "/getAdminArticleTagsList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getAdminArticleTagsList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
		@RequestParam(defaultValue = "1", required = false) Integer pageNo,  
		@RequestParam(defaultValue = "10", required = false) Integer pageSize, 
		@RequestParam(defaultValue = "正常", required = false) String tbStatus, 
		@RequestParam(required = false) String keyword, 
		@RequestParam(defaultValue = "article_tags_id", required = false) String order,
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
			buf.append(")");
			condition.put(buf.toString(), "and");
		}
		String field = null;
		if (condition.size() > 0) {
			condition.put(condition.entrySet().iterator().next().getKey(), "");
		}
		int count = this.articleTagsService.getCount(condition, field);
		if (order != null && order.length() > 0 & "desc".equals(desc)) {
			order = order + " desc";
		}
		List<ArticleTagsVo> list = this.articleTagsService.getList(condition, pageNo, pageSize, order, field);
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

	@RequestMapping(value = "/delArticleTags", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse delArticleTags(int id) {
		int num = this.articleTagsService.delBySign(id);;
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

