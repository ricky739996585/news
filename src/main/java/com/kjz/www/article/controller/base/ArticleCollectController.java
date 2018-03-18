
package com.kjz.www.article.controller.base;
import java.util.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kjz.www.article.domain.Article;
import com.kjz.www.article.service.IArticleService;
import com.kjz.www.article.vo.ArticleVo;
import com.kjz.www.user.domain.User;
import com.kjz.www.user.vo.UserVo;
import org.springframework.context.annotation.Bean;
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
import com.kjz.www.article.service.IArticleCollectService;
import com.kjz.www.article.domain.ArticleCollect;
import com.kjz.www.article.vo.ArticleCollectVo;
import com.kjz.www.article.vo.ArticleCollectVoFont;
import com.kjz.www.utils.UserUtils;
import com.kjz.www.utils.vo.UserCookie;

@Controller
@RequestMapping("/articleCollect")
public class ArticleCollectController {

	@Autowired
	protected WebResponse webResponse;

	@Resource
	protected UserUtils userUtils;

	@Resource
	protected IArticleCollectService articleCollectService;

	@Resource
	protected IArticleService articleService;

	@RequestMapping(value = "/addOrEditArticleCollect", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse addOrEditArticleCollect(HttpServletRequest request, HttpServletResponse response, HttpSession session, String articleCollectId, @RequestParam(required = false) String articleId, @RequestParam(required = false) String userId, @RequestParam(required = false) String tbStatus) {
		if (articleCollectId == null || articleCollectId.length() == 0) {
			return this.addArticleCollect(request, response, session, articleId, userId);
		} else {
			return this.editArticleCollect(request, response, session, articleCollectId, articleId, userId, tbStatus);
		}
	}

	@RequestMapping(value = "/addArticleCollect", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse addArticleCollect(HttpServletRequest request, HttpServletResponse response, HttpSession session, String articleId, String userId) {
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
		ArticleCollect articleCollect = new ArticleCollect();

		UserCookie userCookie = this.userUtils.getLoginUser(request, response, session);
		if (userCookie == null) {
			statusMsg = "请登录！";
			statusCode = 201;
			data = statusMsg;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}

		boolean isAdd = true;
		return this.addOrEditArticleCollect(request, response, session, data, articleCollect,articleId,userId,tbStatus, isAdd);
	}


	@RequestMapping(value = "/editArticleCollect", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse editArticleCollect(HttpServletRequest request, HttpServletResponse response, HttpSession session, String articleCollectId, @RequestParam(required = false) String articleId, @RequestParam(required = false) String userId, @RequestParam(required = false) String tbStatus) {
		Object data = null;
		String statusMsg = "";
		Integer statusCode = 200;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("articleCollectId", articleCollectId);
		paramMap.put("articleId", articleId);
		paramMap.put("userId", userId);
		paramMap.put("tbStatus", tbStatus);
		data = paramMap;
		if (articleCollectId == null || "".equals(articleCollectId.trim())) {
			statusMsg = "未获得主键参数错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		Integer articleCollectIdNumeri = articleCollectId.matches("^[0-9]*$") ? Integer.parseInt(articleCollectId) : 0;
		if (articleCollectIdNumeri == 0) {
			statusMsg = "主键不为数字错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		ArticleCollectVo articleCollectVo = this.articleCollectService.getById(articleCollectIdNumeri);
		ArticleCollect articleCollect = new ArticleCollect();
		BeanUtils.copyProperties(articleCollectVo, articleCollect);
		UserCookie userCookie = this.userUtils.getLoginUser(request, response, session);
		if (userCookie == null) {
			statusMsg = "请登录！";
			statusCode = 201;
			data = statusMsg;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}

		boolean isAdd = false;
		return this.addOrEditArticleCollect(request, response, session, data, articleCollect,articleId,userId,tbStatus, isAdd);
	}


private WebResponse addOrEditArticleCollect(HttpServletRequest request, HttpServletResponse response, HttpSession session, Object data, ArticleCollect articleCollect, String articleId, String userId, String tbStatus, boolean isAdd) {
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
			articleCollect.setArticleId(articleIdNumeri);
		}
		Integer userIdNumeri = 0;
		if (userId != null && !("".equals(userId.trim()))) {
			if (!userId.matches("^[0-9]*$")) {
				statusMsg = " 参数数字型错误！！！不能为0,userId";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			}
			userIdNumeri = Integer.parseInt(userId);
			articleCollect.setUserId(userIdNumeri);
		}
		if (tbStatus != null && !("".equals(tbStatus.trim()))) {
			if(tbStatus.length() > 50) {
				statusMsg = " 参数长度过长错误,tbStatus";
				statusCode = 201;
				return webResponse.getWebResponse(statusCode, statusMsg, data);
			} 
			articleCollect.setTbStatus(tbStatus);
		}
		if (isAdd) {
			this.articleCollectService.insert(articleCollect);
			if (articleCollect.getArticleCollectId() > 0) {
				statusMsg = "成功插入！！！";
			} else {
				statusCode = 202;
				statusMsg = "insert false";
			} 
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		int num = this.articleCollectService.update(articleCollect);
		if (num > 0) {
			statusMsg = "成功修改！！！";
		} else {
			statusCode = 202;
			statusMsg = "update false";
		}
		return webResponse.getWebResponse(statusCode, statusMsg, data);
	}


	@RequestMapping(value = "/getArticleCollectById", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getArticleCollectById(String articleCollectId) {
		Object data = articleCollectId;
		Integer statusCode = 200;
		String statusMsg = "";
		if (articleCollectId == null || articleCollectId.length() == 0 || articleCollectId.length() > 11) {
			statusMsg = "参数为空或参数过长错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		Integer articleCollectIdNumNumeri = articleCollectId.matches("^[0-9]*$") ? Integer.parseInt(articleCollectId) : 0;
		if (articleCollectIdNumNumeri == 0 ) {
			statusMsg = "参数数字型错误！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		ArticleCollectVo articleCollectVo = this.articleCollectService.getById(articleCollectIdNumNumeri);
		if (articleCollectVo != null && articleCollectVo.getArticleCollectId() > 0) {
			data = articleCollectVo;
			statusMsg = "获取单条数据成功！！！";
		} else {
			statusCode = 202;
			statusMsg = "no record!!!";
		}
		return webResponse.getWebResponse(statusCode, statusMsg, data);
	}


	@RequestMapping(value = "/getOneArticleCollect", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getOneArticleCollect(@RequestParam(defaultValue = "正常", required = false) String tbStatus) {
		LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
		condition.put("tb_status='" + tbStatus + "'", "");
		ArticleCollectVo articleCollectVo = this.articleCollectService.getOne(condition);
		Object data = null;
		String statusMsg = "";
		if (articleCollectVo != null && articleCollectVo.getArticleCollectId() > 0) {
			data = articleCollectVo;
			statusMsg = "根据条件获取单条数据成功！！！";
		} else {
			statusMsg = "no record!!!";
		}
		return webResponse.getWebResponse(statusMsg, data);
	}

	@RequestMapping(value = "/getArticleCollectList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getArticleCollectList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
		@RequestParam(defaultValue = "1", required = false) Integer pageNo,  
		@RequestParam(defaultValue = "10", required = false) Integer pageSize, 
		@RequestParam(defaultValue = "正常", required = false) String tbStatus, 
		@RequestParam(required = false) String keyword, 
		@RequestParam(defaultValue = "article_collect_id", required = false) String order,
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
		int count = this.articleCollectService.getCount(condition, field);
		if (order != null && order.length() > 0 & "desc".equals(desc)) {
			order = order + " desc";
		}
		List<ArticleCollectVo> list = this.articleCollectService.getList(condition, pageNo, pageSize, order, field);
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("total", count);
		int size = list.size();
		if (size > 0) {
			List<ArticleCollectVoFont> listFont = new ArrayList<ArticleCollectVoFont>();
			ArticleCollectVo vo;
			ArticleCollectVoFont voFont = new ArticleCollectVoFont(); 
			for (int i = 0; i < size; i++) {
				vo = list.get(i);
				BeanUtils.copyProperties(vo, voFont);
				listFont.add(voFont);
				voFont = new ArticleCollectVoFont();
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

	@RequestMapping(value = "/getAdminArticleCollectList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getAdminArticleCollectList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
		@RequestParam(defaultValue = "1", required = false) Integer pageNo,  
		@RequestParam(defaultValue = "10", required = false) Integer pageSize, 
		@RequestParam(defaultValue = "正常", required = false) String tbStatus, 
		@RequestParam(required = false) String keyword, 
		@RequestParam(defaultValue = "article_collect_id", required = false) String order,
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
		int count = this.articleCollectService.getCount(condition, field);
		if (order != null && order.length() > 0 & "desc".equals(desc)) {
			order = order + " desc";
		}
		List<ArticleCollectVo> list = this.articleCollectService.getList(condition, pageNo, pageSize, order, field);
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

	@RequestMapping(value = "/delArticleCollect", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse delArticleCollect(int id) {
		int num = this.articleCollectService.delBySign(id);;
		Object data = null;
		String statusMsg = "";
		if (num > 0) {
			statusMsg = "成功删除！！！";
		} else {
			statusMsg = "no record!!!";
		}
		return webResponse.getWebResponse(statusMsg, data);
	}


	/**
	 * 功能：收藏、取消收藏文章
	 * @param articleId
	 * @param userId
	 * @param collect 收藏判断关键字：正常，取消
	 * @return
	 */
	@RequestMapping(value = "/articleCollect", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse articleCollect(String articleId,String userId,String collect) {
		Object data = null;
		String statusMsg = "";
		int statusCode=200;
		ArticleCollect articleCollect=new ArticleCollect();
		LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
		condition.put("article_id='" + articleId + "'", "and");
		condition.put("user_id='" + userId + "'", "and");
		ArticleCollectVo articleCollectVo=articleCollectService.getOne(condition);
		if (articleId == null || "".equals(articleId.trim()) || userId == null || "".equals(userId.trim())) {
			statusMsg = " 参数为空错误！！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		if (collect == null || "".equals(collect.trim())) {
			statusMsg = " 参数为空错误！！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		String tbStatus = collect;
		//如果已经存在记录，则只修改记录的状态
		//如果没有，则插入一条记录，并设置状态为关注
		try{
			if(articleCollectVo!=null){
				BeanUtils.copyProperties(articleCollectVo,articleCollect);
				articleCollect.setTbStatus(tbStatus);
				articleCollectService.update(articleCollect);
			}else{
				articleCollect.setUserId(Integer.parseInt(userId));
				articleCollect.setArticleId(Integer.parseInt(articleId));
				articleCollectService.insert(articleCollect);
			}
			statusMsg="操作成功！！！";
		}catch (Exception e){
			statusCode=201;
			statusMsg="操作失败！！！";
		}
		return webResponse.getWebResponse(statusCode,statusMsg, data);
	}

	/**
	 * 根据userId获取收藏文章列表
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/getCollectArticleByUserId", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public WebResponse getCollectArticleByUserId(String userId,
												 @RequestParam(defaultValue = "1", required = false) Integer pageNo,
												 @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
		Object data = null;
		String statusMsg = "";
		int statusCode=200;
		JSONObject jsonObject=new JSONObject();
		if (userId == null || "".equals(userId.trim())) {
			statusMsg = " 参数为空错误！！！！";
			statusCode = 201;
			return webResponse.getWebResponse(statusCode, statusMsg, data);
		}
		LinkedHashMap<String, String> paramMap = new LinkedHashMap<String, String>();
		paramMap.put("user_id='" + userId + "'", "and");

		//获取收藏文章列表
		try{
			List<ArticleCollectVo> articleCollectVoList=articleCollectService.getList(paramMap,pageNo,pageSize);
			int total=articleCollectService.getCount(paramMap,"*");
			List<ArticleVo> articleVoList= new LinkedList<>();
			//获取所有文章，放入articleVoList
			for (ArticleCollectVo articleCollectVo:articleCollectVoList){
				LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
				condition.put("article_id='" + articleCollectVo.getArticleId() + "'", "and");
				ArticleVo articleVo=articleService.getOne(condition);
//				Article article=new Article();
//				BeanUtils.copyProperties(articleVo,article);
				articleVoList.add(articleVo);
			}
			//把User和Article放入Json中
			JSONArray jsonArray=new JSONArray();
			for(ArticleVo articleVo:articleVoList){
				JSONObject json=new JSONObject();
				UserVo userVo=userUtils.getUserById(articleVo.getUserId());
				User user=new User();
				BeanUtils.copyProperties(userVo,user);
				Article article=new Article();
				BeanUtils.copyProperties(articleVo,article);
				json.put("article",article);
				json.put("author",user.getNickname());
				jsonArray.add(json);
			}

//			List<Map<String,Object>> list=articleCollectService.getCollectArticleByUserId(paramMap);
//			Map<String,Object> map=articleCollectService.getCollectArticleCountByUserId(Integer.parseInt(userId)).get(0);
			jsonObject.put("list",jsonArray);
			jsonObject.put("total",total);
			statusMsg="获取成功！！！";
			data=jsonObject;
		}catch (Exception e){
			statusCode=201;
			statusMsg="获取失败！！！";
		}
		return webResponse.getWebResponse(statusCode,statusMsg, data);
	}


}

