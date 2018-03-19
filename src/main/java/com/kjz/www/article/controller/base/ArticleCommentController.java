
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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;

import com.kjz.www.common.WebResponse;
import com.kjz.www.article.service.IArticleCommentService;
import com.kjz.www.article.domain.Article;
import com.kjz.www.article.domain.ArticleComment;
import com.kjz.www.article.vo.ArticleCommentVo;
import com.kjz.www.article.vo.ArticleCommentVoFont;
import com.kjz.www.article.vo.ArticleVo;
import com.kjz.www.utils.UserUtils;
import com.kjz.www.utils.vo.UserCookie;

@Controller
@RequestMapping("/articleComment")
public class ArticleCommentController {

	@Autowired
	protected WebResponse webResponse;

	@Resource
	protected UserUtils userUtils;

	@Resource
	protected IArticleCommentService articleCommentService;

	@RequestMapping(value = "/addOrEditArticleComment", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse addOrEditArticleComment(HttpServletRequest request, HttpServletResponse response, HttpSession session, String commentId, @RequestParam(required = false) String userId, @RequestParam(required = false) String articleId, @RequestParam(required = false) String commentContent, @RequestParam(required = false) String tbStatus) {
		if (commentId == null || commentId.length() == 0) {
			return this.addArticleComment(request, response, session, userId, articleId, commentContent);
		} else {
			return this.editArticleComment(request, response, session, commentId, userId, articleId, commentContent, tbStatus);
		}
	}

	//发表评论
	@RequestMapping(value = "/addArticleComment", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse addArticleComment(HttpServletRequest request, HttpServletResponse response, HttpSession session, String userId, String articleId, String commentContent) {
		Object data = null;
		String statusMsg = "";
		Integer statusCode = 200;
		//userId=userUtils.getUserFromSession(request, response, session).getUserId().toString();
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userId", userId);
		paramMap.put("articleId", articleId);
		paramMap.put("commentContent", commentContent);
		data = paramMap;
		if (userId == null || "".equals(userId.trim()) || articleId == null || "".equals(articleId.trim()) || commentContent == null || "".equals(commentContent.trim())) {
			statusMsg = " 参数为空错误！！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		if (commentContent.length() > 65535) {
			statusMsg = " 参数长度过长错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		String tbStatus = "normal";
		ArticleComment articleComment = new ArticleComment();

		UserCookie userCookie = this.userUtils.getLoginUser(request, response, session);
//		if (userCookie == null) {
//			statusMsg = "请登录！";
//			statusCode = 201;
//			data = statusMsg;
//			return webResponse.getWebResponse(statusCode, statusMsg, data);
//		}
//		//小黑屋:若用户状态为“禁止”，不允许发表评论
//		else{
			Integer userIdNumeri = Integer.parseInt(userId);
        	Boolean userState=this.userUtils.getUserStateById(userIdNumeri);
            if(!userState){
				statusMsg = "您已进入黑名单，无法发表评论";
				statusCode = 201;
				data = statusMsg;
				return webResponse.getWebResponse(statusCode, statusMsg, data);//返回WebResponse对象
			}
//		}

		boolean isAdd = true;
		return this.addOrEditArticleComment(request, response, session, data, articleComment,userId,articleId,commentContent,tbStatus, isAdd);
	}


	@RequestMapping(value = "/editArticleComment", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse editArticleComment(HttpServletRequest request, HttpServletResponse response, HttpSession session, String commentId, @RequestParam(required = false) String userId, @RequestParam(required = false) String articleId, @RequestParam(required = false) String commentContent, @RequestParam(required = false) String tbStatus) {
		Object data = null;
		String statusMsg = "";
		Integer statusCode = 200;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("commentId", commentId);
		paramMap.put("userId", userId);
		paramMap.put("articleId", articleId);
		paramMap.put("commentContent", commentContent);
		paramMap.put("tbStatus", tbStatus);
		data = paramMap;
		if (commentId == null || "".equals(commentId.trim())) {
			statusMsg = "未获得主键参数错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		Integer commentIdNumeri = commentId.matches("^[0-9]*$") ? Integer.parseInt(commentId) : 0;
		if (commentIdNumeri == 0) {
			statusMsg = "主键不为数字错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		ArticleCommentVo articleCommentVo = this.articleCommentService.getById(commentIdNumeri);
		ArticleComment articleComment = new ArticleComment();
		BeanUtils.copyProperties(articleCommentVo, articleComment);
		UserCookie userCookie = this.userUtils.getLoginUser(request, response, session);
		if (userCookie == null) {
			statusMsg = "请登录！";
			statusCode = 201;
			data = statusMsg;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}

		boolean isAdd = false;
		return this.addOrEditArticleComment(request, response, session, data, articleComment,userId,articleId,commentContent,tbStatus, isAdd);
	}


	private WebResponse addOrEditArticleComment(HttpServletRequest request, HttpServletResponse response, HttpSession session, Object data, ArticleComment articleComment, String userId, String articleId, String commentContent, String tbStatus, boolean isAdd) {
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
			articleComment.setUserId(userIdNumeri);
		}
		Integer articleIdNumeri = 0;
		if (articleId != null && !("".equals(articleId.trim()))) {
			if (!articleId.matches("^[0-9]*$")) {
				statusMsg = " 参数数字型错误！！！不能为0,articleId";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			}
			articleIdNumeri = Integer.parseInt(articleId);
			articleComment.setArticleId(articleIdNumeri);
		}
		if (commentContent != null && !("".equals(commentContent.trim()))) {
			if(commentContent.length() > 65535) {
				statusMsg = " 参数长度过长错误,commentContent";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			articleComment.setCommentContent(commentContent);
		}
		if (tbStatus != null && !("".equals(tbStatus.trim()))) {
			if(tbStatus.length() > 50) {
				statusMsg = " 参数长度过长错误,tbStatus";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			articleComment.setTbStatus(tbStatus);
		}
		if (isAdd) {
			this.articleCommentService.insert(articleComment);
			if (articleComment.getCommentId() > 0) {
				statusMsg = "成功插入！！！";
			} else {
				statusCode = 202;
				statusMsg = "insert false";
			} 
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		int num = this.articleCommentService.update(articleComment);
		if (num > 0) {
			statusMsg = "成功修改！！！";
		} else {
			statusCode = 202;
			statusMsg = "update false";
		}
		return webResponse.getWebResponse(statusCode, statusMsg, data);
	}


	@RequestMapping(value = "/getArticleCommentById", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getArticleCommentById(String commentId) {
		Object data = commentId;
		Integer statusCode = 200;
		String statusMsg = "";
		if (commentId == null || commentId.length() == 0 || commentId.length() > 11) {
			statusMsg = "参数为空或参数过长错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		Integer commentIdNumNumeri = commentId.matches("^[0-9]*$") ? Integer.parseInt(commentId) : 0;
		if (commentIdNumNumeri == 0 ) {
			statusMsg = "参数数字型错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		ArticleCommentVo articleCommentVo = this.articleCommentService.getById(commentIdNumNumeri);
		if (articleCommentVo != null && articleCommentVo.getCommentId() > 0) {
			data = articleCommentVo;
			statusMsg = "获取单条数据成功！！！";
		} else {
			statusCode = 202;
			statusMsg = "no record!!!";
		}
		return webResponse.getWebResponse(statusCode, statusMsg, data);
	}


	@RequestMapping(value = "/getOneArticleComment", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getOneArticleComment(@RequestParam(defaultValue = "正常", required = false) String tbStatus) {
		LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
		condition.put("tb_status='" + tbStatus + "'", "");
		ArticleCommentVo articleCommentVo = this.articleCommentService.getOne(condition);
		Object data = null;
		String statusMsg = "";
		if (articleCommentVo != null && articleCommentVo.getCommentId() > 0) {
			data = articleCommentVo;
			statusMsg = "根据条件获取单条数据成功！！！";
		} else {
			statusMsg = "no record!!!";
		}
		return webResponse.getWebResponse(statusMsg, data);
	}

	@RequestMapping(value = "/getArticleCommentList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getArticleCommentList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
		@RequestParam(defaultValue = "1", required = false) Integer pageNo,  
		@RequestParam(defaultValue = "10", required = false) Integer pageSize, 
		@RequestParam(defaultValue = "正常", required = false) String tbStatus, 
		@RequestParam(required = false) String keyword, 
		@RequestParam(defaultValue = "comment_id", required = false) String order,
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
			buf.append("comment_content like '%").append(keyword).append("%'");
			buf.append(")");
			condition.put(buf.toString(), "and");
		}
		String field = null;
		if (condition.size() > 0) {
			condition.put(condition.entrySet().iterator().next().getKey(), "");
		}
		int count = this.articleCommentService.getCount(condition, field);
		if (order != null && order.length() > 0 & "desc".equals(desc)) {
			order = order + " desc";
		}
		List<ArticleCommentVo> list = this.articleCommentService.getList(condition, pageNo, pageSize, order, field);
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("total", count);
		int size = list.size();
		if (size > 0) {
			List<ArticleCommentVoFont> listFont = new ArrayList<ArticleCommentVoFont>();
			ArticleCommentVo vo;
			ArticleCommentVoFont voFont = new ArticleCommentVoFont(); 
			for (int i = 0; i < size; i++) {
				vo = list.get(i);
				BeanUtils.copyProperties(vo, voFont);
				listFont.add(voFont);
				voFont = new ArticleCommentVoFont();
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

	@RequestMapping(value = "/getAdminArticleCommentList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getAdminArticleCommentList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
		@RequestParam(defaultValue = "1", required = false) Integer pageNo,  
		@RequestParam(defaultValue = "10", required = false) Integer pageSize, 
		@RequestParam(defaultValue = "正常", required = false) String tbStatus, 
		@RequestParam(required = false) String keyword, 
		@RequestParam(defaultValue = "comment_id", required = false) String order,
		@RequestParam(defaultValue = "desc", required = false) String desc ) {
		Object data = null;
		String statusMsg = "";
		int statusCode = 200;
		LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
//		UserCookie userCookie = this.userUtils.getLoginUser(request, response, session);
//		if (userCookie == null) {
//			statusMsg = "请登录！";
//			statusCode = 201;
//			data = statusMsg;
//			return JSON.toJSONString(data);
//		}

		if (tbStatus != null && tbStatus.length() > 0) {
			condition.put("tb_status='" + tbStatus + "'", "and");
		}
		if (keyword != null && keyword.length() > 0) {
			StringBuffer buf = new StringBuffer();
			buf.append("(");
			buf.append("comment_content like '%").append(keyword).append("%'");
			buf.append(")");
			condition.put(buf.toString(), "and");
		}
		String field = null;
		if (condition.size() > 0) {
			condition.put(condition.entrySet().iterator().next().getKey(), "");
		}
		int count = this.articleCommentService.getCount(condition, field);
		if (order != null && order.length() > 0 & "desc".equals(desc)) {
			order = order + " desc";
		}
		List<ArticleCommentVo> list = this.articleCommentService.getList(condition, pageNo, pageSize, order, field);
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

	//删除评论
	@RequestMapping(value = "/delArticleComment", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse delArticleComment(int id) {
		int num = this.articleCommentService.delBySign(id);;
		Object data = null;
		String statusMsg = "";
		if (num > 0) {
			statusMsg = "成功删除！！！";
		} else {
			statusMsg = "no record!!!";
		}
		return webResponse.getWebResponse(statusMsg, data);
	}

	//获取一篇文章的评论列表
	@RequestMapping(value = "/getOneArticleCommentList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getOneArticleCommentList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
		@RequestParam String articleId, //articleId的评论
		@RequestParam(defaultValue = "1", required = false) Integer pageNo,  
		@RequestParam(defaultValue = "10", required = false) Integer pageSize, 
		@RequestParam(defaultValue = "正常", required = false) String tbStatus, 
		@RequestParam(required = false) String keyword, 
		@RequestParam(defaultValue = "comment_id", required = false) String order,
		@RequestParam(defaultValue = "desc", required = false) String desc ) {
		Object data = null;
		String statusMsg = "";
		int statusCode = 200;
		LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
		//检测登陆状态
//		UserCookie userCookie = this.userUtils.getLoginUser(request, response, session);
//		if (userCookie == null) {
//			statusMsg = "请登录！";
//			statusCode = 201;
//			data = statusMsg;
//			return JSON.toJSONString(data);
//		}

		//检测格式
		if(articleId !=null ||articleId.length()>0)
		{
			condition.put("article_id='"+articleId+"'", "and");
		}
		
		if (tbStatus != null && tbStatus.length() > 0) {
			condition.put("tb_status='" + tbStatus + "'", "and");
		}
		if (keyword != null && keyword.length() > 0) {
			StringBuffer buf = new StringBuffer();
			buf.append("(");
			buf.append("comment_content like '%").append(keyword).append("%'");
			buf.append(")");
			condition.put(buf.toString(), "and");
		}
		String field = null;
		if (condition.size() > 0) {
			condition.put(condition.entrySet().iterator().next().getKey(), "");
		}
		int count = this.articleCommentService.getCount(condition, field);
		if (order != null && order.length() > 0 & "desc".equals(desc)) {
			order = order + " desc";
		}
		List<ArticleCommentVo> list = this.articleCommentService.getList(condition, pageNo, pageSize, order, field);
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("total", count);
		int size = list.size();
		JSONArray jsonArray=new JSONArray();
		if (size > 0) {
			for(ArticleCommentVo articleCommentvo:list){
	    		JSONObject json=new JSONObject();
	    		//获得用户昵称
	    		String userNickname=this.userUtils.getUserById(articleCommentvo.getUserId()).getNickname();//userId
	    		json.put("userNickname", userNickname);
	    		json.put("articleCommentvo", articleCommentvo);
	    		jsonArray.add(json);
        	}
            map.put("list", jsonArray);
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

	//删除评论（不删数据库记录
    @RequestMapping(value = "/deleteArticleComment", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse deleteArticleComment(HttpServletRequest request, HttpServletResponse response, HttpSession session, String commentId) {
        Object data = null;
        String statusMsg = "";
        Integer statusCode = 200;
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("commentId", commentId);
        data = paramMap;
        if (commentId == null || "".equals(commentId.trim())) {
            statusMsg = "未获得主键参数错误！！！";
            statusCode = 201;
            return webResponse.getWebResponse(statusCode, statusMsg, data);
        }
        Integer commentIdNumeri = commentId.matches("^[0-9]*$") ? Integer.parseInt(commentId) : 0;
        if (commentIdNumeri == 0) {
            statusMsg = "主键不为数字错误！！！";
            statusCode = 201;
            return webResponse.getWebResponse(statusCode, statusMsg, data);
        }
        //获得原文章
        ArticleCommentVo articleCommentVo = this.articleCommentService.getById(commentIdNumeri);
        ArticleComment articleComment = new ArticleComment();
        BeanUtils.copyProperties(articleCommentVo, articleComment);  
    
        articleComment.setTbStatus("删除");
        int num = this.articleCommentService.update(articleComment);
        if (num > 0) {
            statusMsg = "评论已删除！！！";
        } else {
            statusCode = 202;
            statusMsg = "删除错误";
        }
        return webResponse.getWebResponse(statusCode, statusMsg, data);
    }
}

