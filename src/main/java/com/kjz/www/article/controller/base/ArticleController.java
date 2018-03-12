
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
import com.kjz.www.article.service.IArticleService;
import com.kjz.www.article.domain.Article;
import com.kjz.www.article.vo.ArticleVo;
import com.kjz.www.article.vo.ArticleVoFont;
import com.kjz.www.utils.UserUtils;
import com.kjz.www.utils.vo.UserCookie;

@Controller
@RequestMapping("/article")
public class ArticleController {

	@Autowired
	protected WebResponse webResponse;

	@Resource
	protected UserUtils userUtils;

	@Resource
	protected IArticleService articleService;

	@RequestMapping(value = "/addOrEditArticle", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse addOrEditArticle(HttpServletRequest request, HttpServletResponse response, HttpSession session, String articleId, @RequestParam(required = false) String userId, @RequestParam(required = false) String title, @RequestParam(required = false) String content, @RequestParam(required = false) String clicks, @RequestParam(required = false) String typeName, @RequestParam(required = false) String isPass, @RequestParam(required = false) String tbStatus) {
		if (articleId == null || articleId.length() == 0) {
			return this.addArticle(request, response, session, userId, title, content, clicks, typeName, isPass);
		} else {
			return this.editArticle(request, response, session, articleId, userId, title, content, clicks, typeName, isPass, tbStatus);
		}
	}

	@RequestMapping(value = "/addArticle", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse addArticle(HttpServletRequest request, HttpServletResponse response, HttpSession session, String userId, String title, String content, String clicks, String typeName, String isPass) {
		Object data = null;
		String statusMsg = "";
		Integer statusCode = 200;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userId", userId);
		paramMap.put("title", title);
		paramMap.put("content", content);
		paramMap.put("clicks", clicks);
		paramMap.put("typeName", typeName);
		paramMap.put("isPass", isPass);
		data = paramMap;
		if (userId == null || "".equals(userId.trim()) || title == null || "".equals(title.trim()) || content == null || "".equals(content.trim()) || clicks == null || "".equals(clicks.trim()) || typeName == null || "".equals(typeName.trim()) || isPass == null || "".equals(isPass.trim())) {
			statusMsg = " 参数为空错误！！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		if (title.length() > 50 || content.length() > 65535 || typeName.length() > 50 || isPass.length() > 50) {
			statusMsg = " 参数长度过长错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		String tbStatus = "normal";
		Article article = new Article();

		UserCookie userCookie = this.userUtils.getLoginUser(request, response, session);
		if (userCookie == null) {
			statusMsg = "请登录！";
			statusCode = 201;
			data = statusMsg;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}

		boolean isAdd = true;
		return this.addOrEditArticle(request, response, session, data, article,userId,title,content,clicks,typeName,isPass,tbStatus, isAdd);
	}


	@RequestMapping(value = "/editArticle", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse editArticle(HttpServletRequest request, HttpServletResponse response, HttpSession session, String articleId, @RequestParam(required = false) String userId, @RequestParam(required = false) String title, @RequestParam(required = false) String content, @RequestParam(required = false) String clicks, @RequestParam(required = false) String typeName, @RequestParam(required = false) String isPass, @RequestParam(required = false) String tbStatus) {
		Object data = null;
		String statusMsg = "";
		Integer statusCode = 200;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("articleId", articleId);
		paramMap.put("userId", userId);
		paramMap.put("title", title);
		paramMap.put("content", content);
		paramMap.put("clicks", clicks);
		paramMap.put("typeName", typeName);
		paramMap.put("isPass", isPass);
		paramMap.put("tbStatus", tbStatus);
		data = paramMap;
		if (articleId == null || "".equals(articleId.trim())) {
			statusMsg = "未获得主键参数错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		Integer articleIdNumeri = articleId.matches("^[0-9]*$") ? Integer.parseInt(articleId) : 0;
		if (articleIdNumeri == 0) {
			statusMsg = "主键不为数字错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		ArticleVo articleVo = this.articleService.getById(articleIdNumeri);
		Article article = new Article();
		BeanUtils.copyProperties(articleVo, article);
		UserCookie userCookie = this.userUtils.getLoginUser(request, response, session);
		if (userCookie == null) {
			statusMsg = "请登录！";
			statusCode = 201;
			data = statusMsg;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}

		boolean isAdd = false;
		return this.addOrEditArticle(request, response, session, data, article,userId,title,content,clicks,typeName,isPass,tbStatus, isAdd);
	}


private WebResponse addOrEditArticle(HttpServletRequest request, HttpServletResponse response, HttpSession session, Object data, Article article, String userId, String title, String content, String clicks, String typeName, String isPass, String tbStatus, boolean isAdd) {
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
			article.setUserId(userIdNumeri);
		}
		if (title != null && !("".equals(title.trim()))) {
			if(title.length() > 50) {
				statusMsg = " 参数长度过长错误,title";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			article.setTitle(title);
		}
		if (content != null && !("".equals(content.trim()))) {
			if(content.length() > 65535) {
				statusMsg = " 参数长度过长错误,content";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			article.setContent(content);
		}
		Integer clicksNumeri = 0;
		if (clicks != null && !("".equals(clicks.trim()))) {
			if (!clicks.matches("^[0-9]*$")) {
				statusMsg = " 参数数字型错误！！！不能为0,clicks";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			}
			clicksNumeri = Integer.parseInt(clicks);
			article.setClicks(clicksNumeri);
		}
		if (typeName != null && !("".equals(typeName.trim()))) {
			if(typeName.length() > 50) {
				statusMsg = " 参数长度过长错误,typeName";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			article.setTypeName(typeName);
		}
		if (isPass != null && !("".equals(isPass.trim()))) {
			if(isPass.length() > 50) {
				statusMsg = " 参数长度过长错误,isPass";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			article.setIsPass(isPass);
		}
		if (tbStatus != null && !("".equals(tbStatus.trim()))) {
			if(tbStatus.length() > 50) {
				statusMsg = " 参数长度过长错误,tbStatus";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			article.setTbStatus(tbStatus);
		}
		if (isAdd) {
			this.articleService.insert(article);
			int articleId=article.getArticleId();
			Map<String,Object> map=new HashMap<>();
			map.put("articleId",articleId);
			data=map;
			if (article.getArticleId() > 0) {
				statusMsg = "成功插入！！！";
			} else {
				statusCode = 202;
				statusMsg = "insert false";
			} 
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		int num = this.articleService.update(article);
		if (num > 0) {
			statusMsg = "成功修改！！！";
		} else {
			statusCode = 202;
			statusMsg = "update false";
		}
		return webResponse.getWebResponse(statusCode, statusMsg, data);
	}


	@RequestMapping(value = "/getArticleById", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getArticleById(String articleId) {
		Object data = articleId;
		Integer statusCode = 200;
		String statusMsg = "";
		if (articleId == null || articleId.length() == 0 || articleId.length() > 11) {
			statusMsg = "参数为空或参数过长错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		Integer articleIdNumNumeri = articleId.matches("^[0-9]*$") ? Integer.parseInt(articleId) : 0;
		if (articleIdNumNumeri == 0 ) {
			statusMsg = "参数数字型错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		ArticleVo articleVo = this.articleService.getById(articleIdNumNumeri);
		if (articleVo != null && articleVo.getArticleId() > 0) {
			data = articleVo;
			statusMsg = "获取单条数据成功！！！";
		} else {
			statusCode = 202;
			statusMsg = "no record!!!";
		}
		return webResponse.getWebResponse(statusCode, statusMsg, data);
	}


	@RequestMapping(value = "/getOneArticle", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getOneArticle(@RequestParam(defaultValue = "正常", required = false) String tbStatus) {
		LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
		condition.put("tb_status='" + tbStatus + "'", "");
		ArticleVo articleVo = this.articleService.getOne(condition);
		Object data = null;
		String statusMsg = "";
		if (articleVo != null && articleVo.getArticleId() > 0) {
			data = articleVo;
			statusMsg = "根据条件获取单条数据成功！！！";
		} else {
			statusMsg = "no record!!!";
		}
		return webResponse.getWebResponse(statusMsg, data);
	}

	@RequestMapping(value = "/getArticleList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getArticleList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
		@RequestParam(defaultValue = "1", required = false) Integer pageNo,  
		@RequestParam(defaultValue = "10", required = false) Integer pageSize, 
		@RequestParam(defaultValue = "正常", required = false) String tbStatus, 
		@RequestParam(required = false) String keyword, 
		@RequestParam(defaultValue = "article_id", required = false) String order,
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
			buf.append("content like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("type_name like '%").append(keyword).append("%'");
			buf.append(")");
			condition.put(buf.toString(), "and is_pass = '通过'");
		}
		String field = null;
		if (condition.size() > 0) {
			condition.put(condition.entrySet().iterator().next().getKey(), "");
		}
		int count = this.articleService.getCount(condition, field);
		if (order != null && order.length() > 0 & "desc".equals(desc)) {
			order = order + " desc";
		}
		List<ArticleVo> list = this.articleService.getList(condition, pageNo, pageSize, order, field);
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("total", count);
		int size = list.size();
		if (size > 0) {
			List<ArticleVoFont> listFont = new ArrayList<ArticleVoFont>();
			ArticleVo vo;
			ArticleVoFont voFont = new ArticleVoFont(); 
			for (int i = 0; i < size; i++) {
				vo = list.get(i);
				BeanUtils.copyProperties(vo, voFont);
				listFont.add(voFont);
				voFont = new ArticleVoFont();
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

	@RequestMapping(value = "/getAdminArticleList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getAdminArticleList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
		@RequestParam(defaultValue = "1", required = false) Integer pageNo,  
		@RequestParam(defaultValue = "10", required = false) Integer pageSize, 
		@RequestParam(defaultValue = "正常", required = false) String tbStatus, 
		@RequestParam(required = false) String keyword, 
		@RequestParam(defaultValue = "article_id", required = false) String order,
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
		condition.put("is_pass= '通过'","and");
		if (keyword != null && keyword.length() > 0) {
			StringBuffer buf = new StringBuffer();
			buf.append("(");
			buf.append("title like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("content like '%").append(keyword).append("%'");
			buf.append(" or ");
			buf.append("type_name like '%").append(keyword).append("%'");
			buf.append(")");
			condition.put(buf.toString(), "and ");
		}
		String field = null;
		if (condition.size() > 0) {
			condition.put(condition.entrySet().iterator().next().getKey(), "");
		}
		int count = this.articleService.getCount(condition, field);
		if (order != null && order.length() > 0 & "desc".equals(desc)) {
			order = order + " desc";
		}
		List<ArticleVo> list = this.articleService.getList(condition, pageNo, pageSize, order, field);
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

	@RequestMapping(value = "/delArticle", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse delArticle(int id) {
		int num = this.articleService.delBySign(id);;
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

