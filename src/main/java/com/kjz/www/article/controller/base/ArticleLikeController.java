
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
import com.kjz.www.article.service.IArticleLikeService;
import com.kjz.www.article.domain.ArticleLike;
import com.kjz.www.article.vo.ArticleLikeVo;
import com.kjz.www.article.vo.ArticleLikeVoFont;
import com.kjz.www.utils.UserUtils;
import com.kjz.www.utils.vo.UserCookie;

@Controller
@RequestMapping("/articleLike")
public class ArticleLikeController {

	@Autowired
	protected WebResponse webResponse;

	@Resource
	protected UserUtils userUtils;

	@Resource
	protected IArticleLikeService articleLikeService;

	@RequestMapping(value = "/addOrEditArticleLike", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse addOrEditArticleLike(HttpServletRequest request, HttpServletResponse response, HttpSession session, String articleLikeId, @RequestParam(required = false) String articleId, @RequestParam(required = false) String userId, @RequestParam(required = false) String tbStatus) {
		if (articleLikeId == null || articleLikeId.length() == 0) {
			return this.addArticleLike(request, response, session, articleId, userId);
		} else {
			return this.editArticleLike(request, response, session, articleLikeId, articleId, userId, tbStatus);
		}
	}

	@RequestMapping(value = "/addArticleLike", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse addArticleLike(HttpServletRequest request, HttpServletResponse response, HttpSession session, String articleId, String userId) {
		Object data = null;
		String statusMsg = "";
		Integer statusCode = 200;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("articleId", articleId);
		paramMap.put("userId", userId);
		data = paramMap;
		if (articleId == null || "".equals(articleId.trim()) || userId == null || "".equals(userId.trim())) {
			statusMsg = " 参数为空错误！！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		String tbStatus = "normal";
		ArticleLike articleLike = new ArticleLike();

		UserCookie userCookie = this.userUtils.getLoginUser(request, response, session);
		if (userCookie == null) {
			statusMsg = "请登录！";
			statusCode = 201;
			data = statusMsg;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}

		boolean isAdd = true;
		return this.addOrEditArticleLike(request, response, session, data, articleLike,articleId,userId,tbStatus, isAdd);
	}


	@RequestMapping(value = "/editArticleLike", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse editArticleLike(HttpServletRequest request, HttpServletResponse response, HttpSession session, String articleLikeId, @RequestParam(required = false) String articleId, @RequestParam(required = false) String userId, @RequestParam(required = false) String tbStatus) {
		Object data = null;
		String statusMsg = "";
		Integer statusCode = 200;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("articleLikeId", articleLikeId);
		paramMap.put("articleId", articleId);
		paramMap.put("userId", userId);
		paramMap.put("tbStatus", tbStatus);
		data = paramMap;
		if (articleLikeId == null || "".equals(articleLikeId.trim())) {
			statusMsg = "未获得主键参数错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		Integer articleLikeIdNumeri = articleLikeId.matches("^[0-9]*$") ? Integer.parseInt(articleLikeId) : 0;
		if (articleLikeIdNumeri == 0) {
			statusMsg = "主键不为数字错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		ArticleLikeVo articleLikeVo = this.articleLikeService.getById(articleLikeIdNumeri);
		ArticleLike articleLike = new ArticleLike();
		BeanUtils.copyProperties(articleLikeVo, articleLike);
		UserCookie userCookie = this.userUtils.getLoginUser(request, response, session);
		if (userCookie == null) {
			statusMsg = "请登录！";
			statusCode = 201;
			data = statusMsg;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}

		boolean isAdd = false;
		return this.addOrEditArticleLike(request, response, session, data, articleLike,articleId,userId,tbStatus, isAdd);
	}


private WebResponse addOrEditArticleLike(HttpServletRequest request, HttpServletResponse response, HttpSession session, Object data, ArticleLike articleLike, String articleId, String userId, String tbStatus, boolean isAdd) {
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
			articleLike.setArticleId(articleIdNumeri);
		}
		Integer userIdNumeri = 0;
		if (userId != null && !("".equals(userId.trim()))) {
			if (!userId.matches("^[0-9]*$")) {
				statusMsg = " 参数数字型错误！！！不能为0,userId";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			}
			userIdNumeri = Integer.parseInt(userId);
			articleLike.setUserId(userIdNumeri);
		}
		if (tbStatus != null && !("".equals(tbStatus.trim()))) {
			if(tbStatus.length() > 50) {
				statusMsg = " 参数长度过长错误,tbStatus";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			articleLike.setTbStatus(tbStatus);
		}
		if (isAdd) {
			this.articleLikeService.insert(articleLike);
			if (articleLike.getArticleLikeId() > 0) {
				statusMsg = "成功插入！！！";
			} else {
				statusCode = 202;
				statusMsg = "insert false";
			} 
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		int num = this.articleLikeService.update(articleLike);
		if (num > 0) {
			statusMsg = "成功修改！！！";
		} else {
			statusCode = 202;
			statusMsg = "update false";
		}
		return webResponse.getWebResponse(statusCode, statusMsg, data);
	}


	@RequestMapping(value = "/getArticleLikeById", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getArticleLikeById(String articleLikeId) {
		Object data = articleLikeId;
		Integer statusCode = 200;
		String statusMsg = "";
		if (articleLikeId == null || articleLikeId.length() == 0 || articleLikeId.length() > 11) {
			statusMsg = "参数为空或参数过长错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		Integer articleLikeIdNumNumeri = articleLikeId.matches("^[0-9]*$") ? Integer.parseInt(articleLikeId) : 0;
		if (articleLikeIdNumNumeri == 0 ) {
			statusMsg = "参数数字型错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		ArticleLikeVo articleLikeVo = this.articleLikeService.getById(articleLikeIdNumNumeri);
		if (articleLikeVo != null && articleLikeVo.getArticleLikeId() > 0) {
			data = articleLikeVo;
			statusMsg = "获取单条数据成功！！！";
		} else {
			statusCode = 202;
			statusMsg = "no record!!!";
		}
		return webResponse.getWebResponse(statusCode, statusMsg, data);
	}


	@RequestMapping(value = "/getOneArticleLike", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getOneArticleLike(@RequestParam(defaultValue = "正常", required = false) String tbStatus) {
		LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
		condition.put("tb_status='" + tbStatus + "'", "");
		ArticleLikeVo articleLikeVo = this.articleLikeService.getOne(condition);
		Object data = null;
		String statusMsg = "";
		if (articleLikeVo != null && articleLikeVo.getArticleLikeId() > 0) {
			data = articleLikeVo;
			statusMsg = "根据条件获取单条数据成功！！！";
		} else {
			statusMsg = "no record!!!";
		}
		return webResponse.getWebResponse(statusMsg, data);
	}

	@RequestMapping(value = "/getArticleLikeList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getArticleLikeList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
		@RequestParam(defaultValue = "1", required = false) Integer pageNo,  
		@RequestParam(defaultValue = "10", required = false) Integer pageSize, 
		@RequestParam(defaultValue = "正常", required = false) String tbStatus, 
		@RequestParam(required = false) String keyword, 
		@RequestParam(defaultValue = "article_like_id", required = false) String order,
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
		int count = this.articleLikeService.getCount(condition, field);
		if (order != null && order.length() > 0 & "desc".equals(desc)) {
			order = order + " desc";
		}
		List<ArticleLikeVo> list = this.articleLikeService.getList(condition, pageNo, pageSize, order, field);
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("total", count);
		int size = list.size();
		if (size > 0) {
			List<ArticleLikeVoFont> listFont = new ArrayList<ArticleLikeVoFont>();
			ArticleLikeVo vo;
			ArticleLikeVoFont voFont = new ArticleLikeVoFont(); 
			for (int i = 0; i < size; i++) {
				vo = list.get(i);
				BeanUtils.copyProperties(vo, voFont);
				listFont.add(voFont);
				voFont = new ArticleLikeVoFont();
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

	@RequestMapping(value = "/getAdminArticleLikeList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getAdminArticleLikeList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
		@RequestParam(defaultValue = "1", required = false) Integer pageNo,  
		@RequestParam(defaultValue = "10", required = false) Integer pageSize, 
		@RequestParam(defaultValue = "正常", required = false) String tbStatus, 
		@RequestParam(required = false) String keyword, 
		@RequestParam(defaultValue = "article_like_id", required = false) String order,
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
		int count = this.articleLikeService.getCount(condition, field);
		if (order != null && order.length() > 0 & "desc".equals(desc)) {
			order = order + " desc";
		}
		List<ArticleLikeVo> list = this.articleLikeService.getList(condition, pageNo, pageSize, order, field);
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

	@RequestMapping(value = "/delArticleLike", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse delArticleLike(int id) {
		int num = this.articleLikeService.delBySign(id);;
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

