
package com.kjz.www.article.controller.base;
import java.io.IOException;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.kjz.www.utils.ArticleTagsUtils;
import com.kjz.www.utils.OSSUtils;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.http.MediaType;
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
import com.kjz.www.article.service.IArticleTagsService;
import com.kjz.www.article.domain.Article;
import com.kjz.www.article.domain.ArticleTags;
import com.kjz.www.article.vo.ArticleTagsVo;
import com.kjz.www.article.vo.ArticleVo;
import com.kjz.www.article.vo.ArticleVoFont;
import com.kjz.www.utils.UserUtils;
import com.kjz.www.utils.vo.UserCookie;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Controller
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    protected WebResponse webResponse;

    @Resource
    protected UserUtils userUtils;

    @Resource
    protected ArticleTagsUtils articleTagsUtils;

    @Resource
    protected IArticleService articleService;

    @Resource
    protected IArticleTagsService articleTagsService;
    //判断添加或修改
    @RequestMapping(value = "/addOrEditArticle", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse addOrEditArticle(HttpServletRequest request, HttpServletResponse response, HttpSession session, String articleId, @RequestParam(required = false) String userId, @RequestParam(required = false) String title, @RequestParam(required = false) String content, @RequestParam(required = false) String preContent, @RequestParam(required = false) String clicks, @RequestParam(required = false) String typeName, @RequestParam(required = false) String isPass, @RequestParam(required = false) String tbStatus,@RequestParam(required = false)String tagsId) {
        if (articleId == null || articleId.length() == 0) {
            return this.addArticle(request, response, session, userId, title, content, preContent, clicks, typeName, isPass, tagsId);
        } else {
            return this.editArticle(request, response, session, articleId, userId, title, content, preContent, clicks, typeName, isPass, tbStatus,tagsId);
        }
    }

    //添加文章
    @RequestMapping(value = "/addArticle", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse addArticle(HttpServletRequest request, HttpServletResponse response, HttpSession session, String userId, String title, String content, String preContent, String clicks, String typeName, String isPass, String tagsId) {
        //初始化数据
        Object data = null;
        String statusMsg = "";
        Integer statusCode = 200;
        Map<String, String> paramMap = new HashMap<String, String>();//保存前端传过来的Json到Map集合
        paramMap.put("userId", userId);
        paramMap.put("title", title);
        paramMap.put("content", content);
        paramMap.put("preContent", preContent);
        paramMap.put("clicks", clicks);
        paramMap.put("typeName", typeName);
        paramMap.put("isPass", isPass);
        data = paramMap;//map集合保存到data
        //数据校验
        if (userId == null || "".equals(userId.trim()) || title == null || "".equals(title.trim()) || content == null || "".equals(content.trim()) || clicks == null || "".equals(clicks.trim()) || typeName == null || "".equals(typeName.trim()) || isPass == null || "".equals(isPass.trim())) {
            statusMsg = " 参数为空错误！！！！";
            statusCode = 201;
            return webResponse.getWebResponse(statusCode, statusMsg, data);
        }
//        if (title.length() > 50 || content.length() > 65535 || preContent.length() > 65535 || typeName.length() > 50 || isPass.length() > 50) {
//            statusMsg = " 参数长度过长错误！！！";
//            statusCode = 201;
//            return webResponse.getWebResponse(statusCode, statusMsg, data);
//        }
        String tbStatus = "normal";
        Article article = new Article();
//        //获得用户userCookie
//        UserCookie userCookie = this.userUtils.getLoginUser(request, response, session);
//        if (userCookie == null) {//判断userCookie是否为空
//            statusMsg = "请登录！";
//            statusCode = 201;
//            data = statusMsg;
//            return webResponse.getWebResponse(statusCode, statusMsg, data);//返回WebResponse对象
//        }
//        //小黑屋:若用户状态为“禁止”，不允许发表文章
//        else{
//            String userTbStatus=this.userUtils.getUserFromSession(request, response, session).getTbStatus();
//            if(userTbStatus.equals("禁止")){
//                statusMsg = "您已进入黑名单，无法发表文章";
//                statusCode = 201;
//                data = statusMsg;
//                return webResponse.getWebResponse(statusCode, statusMsg, data);//返回WebResponse对象
//                }
//            else{
//                statusMsg = "添加成功";
//                statusCode = 200;
//                data = statusMsg;
//                //return webResponse.getWebResponse(statusCode, statusMsg, data);//返回WebResponse对象
//            }
//        }

        boolean isAdd = true;
        //交给判断信息
        return this.addOrEditArticle(request, response, session, data, article,userId,title,content,preContent,clicks,typeName,isPass,tbStatus, isAdd,tagsId);
    }

    //更改文章
    @RequestMapping(value = "/editArticle", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse editArticle(HttpServletRequest request, HttpServletResponse response, HttpSession session, String articleId, @RequestParam(required = false) String userId, @RequestParam(required = false) String title, @RequestParam(required = false) String content, @RequestParam(required = false) String preContent, @RequestParam(required = false) String clicks, @RequestParam(required = false) String typeName, @RequestParam(required = false) String isPass, @RequestParam(required = false) String tbStatus,@RequestParam(required = false)String tagsId) {
        Object data = null;
        String statusMsg = "";
        Integer statusCode = 200;
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("articleId", articleId);
        paramMap.put("userId", userId);
        paramMap.put("title", title);
        paramMap.put("content", content);
        paramMap.put("preContent", preContent);
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
        return this.addOrEditArticle(request, response, session, data, article,userId,title,content,preContent,clicks,typeName,isPass,tbStatus, isAdd,tagsId);
    }


    //校验文章格式
    private WebResponse addOrEditArticle(HttpServletRequest request, HttpServletResponse response, HttpSession session, Object data, Article article, String userId, String title, String content, String preContent, String clicks, String typeName, String isPass, String tbStatus, boolean isAdd,String tagsId) {
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
        if (preContent != null && !("".equals(preContent.trim()))) {
            if(preContent.length() > 65535) {
                statusMsg = " 参数长度过长错误,preContent";
                statusCode = 201;
                return webResponse.getWebResponse(statusCode, statusMsg, data);
            }
            article.setPreContent(preContent);
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
            if (article.getArticleId() > 0) {//若文章表插入成功
                //插入文章-标签表
                ArticleTags articleTagsEntity=this.articleTagsUtils.setEntity(article.getArticleId(), tagsId, tbStatus);
                this.articleTagsService.insert(articleTagsEntity);
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

    //查找文章（通过articleId）
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
            articleVo.setClicks(articleVo.getClicks()+1);//浏览数+1
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
            buf.append("pre_content like '%").append(keyword).append("%'");
            buf.append(" or ");
            buf.append("type_name like '%").append(keyword).append("%'");
            buf.append(" or ");
            buf.append("is_pass like '%").append(keyword).append("%'");
            buf.append(")");
            condition.put(buf.toString(), "and");
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
//	    if (userCookie == null) {
//	      statusMsg = "请登录！";
//	      statusCode = 201;
//	      data = statusMsg;
//	      return JSON.toJSONString(data);
//	    }

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
            buf.append("pre_content like '%").append(keyword).append("%'");
            buf.append(" or ");
            buf.append("type_name like '%").append(keyword).append("%'");
            buf.append(" or ");
            buf.append("is_pass like '%").append(keyword).append("%'");
            buf.append(")");
            condition.put(buf.toString(), "and");
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

    //根据文章id删除
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

    //获取新闻列表
    @RequestMapping(value = "/getArticleNewList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getArticleNewList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
                                    @RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                    @RequestParam(defaultValue = "10", required = false) Integer pageSize,
                                    @RequestParam(defaultValue = "正常", required = false) String tbStatus,
                                    @RequestParam(required = false) String keyword,
                                    //@RequestParam(defaultValue = "新闻",required = false) String typeName,
                                    @RequestParam(defaultValue = "article_id", required = false) String order,
                                    @RequestParam(defaultValue = "desc", required = false) String desc ) {
        Object data = null;
        String statusMsg = "";
        int statusCode = 200;
        LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
        UserCookie userCookie = this.userUtils.getLoginUser(request, response, session);

        if (tbStatus != null && tbStatus.length() > 0) {
            condition.put("tb_status='" + tbStatus + "'", "and");
        }
        condition.put("type_name='新闻'", "and");//要新闻
        condition.put("is_pass= '通过'","and");//要通过审核

        if (keyword != null && keyword.length() > 0) {//若搜索关键字不为空
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
    //获取论坛列表
    @RequestMapping(value = "/getArticleForumList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getArticleForumList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
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

        if (tbStatus != null && tbStatus.length() > 0) {
            condition.put("tb_status='" + tbStatus + "'", "and");
        }

        condition.put("is_pass= '通过'","and");//要通过审核
        condition.put("type_name='论坛'", "and");//要论坛
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


    //获取博客列表
    @RequestMapping(value = "/getArticleBlogList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getArticleBlogList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
                                     @RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                     @RequestParam(defaultValue = "10", required = false) Integer pageSize,
                                     @RequestParam(defaultValue = "正常", required = false) String tbStatus,
                                     @RequestParam(required = false)String userId,
                                     @RequestParam(required = false) String keyword,
                                     @RequestParam(defaultValue = "article_id", required = false) String order,
                                     @RequestParam(defaultValue = "desc", required = false) String desc ) {
        Object data = null;
        String statusMsg = "";
        int statusCode = 200;
        LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
        UserCookie userCookie = this.userUtils.getLoginUser(request, response, session);

//			if (userId != null && userId.length() > 0) {
//				condition.put("userId='" + userId + "'", "and");
//			}

        if (tbStatus != null && tbStatus.length() > 0) {
            condition.put("tb_status='" + tbStatus + "'", "and");
        }

        condition.put("is_pass= '通过'","and");//要通过审核
        condition.put("type_name='博客'", "and");//要博客
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

    //根据标签获取文章列表
    @RequestMapping(value = "/getTagArticleList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getTagArticleList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
                                    @RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                    @RequestParam(defaultValue = "10", required = false) Integer pageSize,
                                    @RequestParam(defaultValue = "正常", required = false) String tbStatus,
                                    @RequestParam(required = false) String keyword,
                                    @RequestParam(required = false) Integer tagId,
                                    @RequestParam(defaultValue = "article_id", required = false) String order,
                                    @RequestParam(defaultValue = "desc", required = false) String desc ) {
        Object data = null;
        String statusMsg = "";
        int statusCode = 200;
        LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
        UserCookie userCookie = this.userUtils.getLoginUser(request, response, session);

        if (tbStatus != null && tbStatus.length() > 0) {
            condition.put("tb_status='" + tbStatus + "'", "and");
        }
        condition.put("is_pass= '通过'","and");//要通过审核
        //tag文章列表
        List<Map<String,Object>> taglist=this.articleService.getArticlesByTagId(tagId);
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
        map.put("list", taglist);//tag文章列表
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

    @RequestMapping(value = "/insertPic", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject insertPic(HttpServletRequest request){
        Map<String,Object> map=new HashedMap();
        JSONArray jsonArray=new JSONArray();
        JSONObject jsonObject=new JSONObject();
        MultipartRequest multipartRequest= (MultipartRequest) request;
        Iterator<String> iterator=multipartRequest.getFileNames();
        MultipartFile multipartFile=null;
        while (iterator.hasNext()){
            String str = iterator.next();    //这个文件并不是原来的文件名
            multipartFile = (MultipartFile) multipartRequest.getFile(str);
            multipartFile.getOriginalFilename();
            String fileName= UUID.randomUUID().toString()+multipartFile.getOriginalFilename();
            String url="";
            //进行cos对象上传操作
            OSSUtils utils=new OSSUtils();
            OSSClient ossClient=utils.createCilent();
            try {
                url=utils.upload(fileName,multipartFile.getInputStream(),ossClient);
                jsonArray.add(url);
                jsonObject.put("data",url);
            } catch (IOException e) {
                e.printStackTrace();
                jsonObject.put("errno",1);
                jsonObject.put("data","上传失败！");
            }
        }
        jsonObject.put("errno",0);
        return jsonObject;
    }

    
    //发表文章（String userId, String title, String content,  String tagsId,String typeName）
    @RequestMapping(value = "/newArticle", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    private WebResponse newArticle(HttpServletRequest request, HttpServletResponse response, HttpSession session, String userId, String title, String content,  String tagsId,String typeName) {
        //初始化数据
        Object data = null;
        String statusMsg = "";
        Integer statusCode = 200;
        Map<String, String> paramMap = new HashMap<String, String>();//保存前端传过来的Json到Map集合
        paramMap.put("userId", userId);
        paramMap.put("title", title);
        paramMap.put("content", content);
        data = paramMap;//map集合保存到data
        Article article =  new Article();
        
        //数据校验
        
        //检测userId
        if(userId == null || ("".equals(userId.trim()))){//用户未登录
        	statusMsg = "请登录！";
	        statusCode = 201;
	        data = statusMsg;
	        return webResponse.getWebResponse(statusCode, statusMsg, data);//返回WebResponse对象
        }
        else{//若userId不为空
            if (!userId.matches("^[0-9]*$")) {//用户参数错误
                statusMsg = " userId参数数字型错误！！！";
                statusCode = 201;
                return webResponse.getWebResponse(statusCode, statusMsg, data);
                }
            //检测用户小黑屋
            else{
            	Integer userIdNumeri = Integer.parseInt(userId);
            	Boolean userState=this.userUtils.getUserStateById(userIdNumeri);
	            if(!userState){
	            	statusMsg = "您已进入黑名单，无法发表文章";
	                statusCode = 201;
	                data = statusMsg;
	                return webResponse.getWebResponse(statusCode, statusMsg, data);//返回WebResponse对象
	                }
	            else{
	            	article.setUserId(userIdNumeri);
	            	}
	            }
            }     
        
        //检测标题格式
        if (title != null && !("".equals(title.trim()))) {
            if(title.length() > 50) {
                statusMsg = " 参数长度过长错误,title";
                statusCode = 201;
                return webResponse.getWebResponse(statusCode, statusMsg, data);
            }
            article.setTitle(title);
        }
        //检测内容格式
        if (content != null && !("".equals(content.trim()))) {
            if(content.length() > 65535) {
                statusMsg = " 参数长度过长错误,content";
                statusCode = 201;
                return webResponse.getWebResponse(statusCode, statusMsg, data);
            }
            article.setContent(content);
        }
//        if (preContent != null && !("".equals(preContent.trim()))) {
//            if(preContent.length() > 65535) {
//                statusMsg = " 参数长度过长错误,preContent";
//                statusCode = 201;
//                return webResponse.getWebResponse(statusCode, statusMsg, data);
//            }
//            article.setPreContent(preContent);
//        }
        
        //检测tagsId格式
        if (!tagsId.matches("^[0-9]*$")) {//标签参数错误
            statusMsg = " tagsId参数数字型错误！！！";
            statusCode = 201;
            return webResponse.getWebResponse(statusCode, statusMsg, data);
            }
        
        //检测通过
        article.setTypeName(typeName);//博客
        article.setClicks(0);//默认0点击量
        article.setIsPass("未审核");//默认未审核
        article.setTbStatus("正常");//默认文章状态正常
        this.articleService.insert(article);
        if (article.getArticleId() > 0) {//若文章表插入成功
            //插入文章-标签表
        	String tbStatus="正常";
        	ArticleTags articleTagsEntity=this.articleTagsUtils.setEntity(article.getArticleId(), tagsId, tbStatus);
            this.articleTagsService.insert(articleTagsEntity);
            statusMsg = "成功插入文章标签！！！";
            } else {
                statusCode = 202;
                statusMsg = "insert文章标签false";
            }
        return webResponse.getWebResponse(statusCode, statusMsg, data);
        
//        int num = this.articleService.update(article);
//        if (num > 0) {
//            statusMsg = "成功修改！！！";
//        } else {
//            statusCode = 202;
//            statusMsg = "update false";
//        }

    }
    
    //发表新闻
    @RequestMapping(value = "/newNewsArticle", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse newNewsArticle(HttpServletRequest request, HttpServletResponse response, HttpSession session, String userId, String title, String content,  String tagsId) {
        return this.newArticle(request, response, session, userId, title, content, tagsId, "新闻");
    }
    
    //发表博客
    @RequestMapping(value = "/newBlogArticle", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse newBlogArticle(HttpServletRequest request, HttpServletResponse response, HttpSession session, String userId, String title, String content,  String tagsId) {
        return this.newArticle(request, response, session, userId, title, content, tagsId, "博客");
    }
    
    //发表论坛
    @RequestMapping(value = "/newForumArticle", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse newForumArticle(HttpServletRequest request, HttpServletResponse response, HttpSession session, String userId, String title, String content,  String tagsId) {
        return this.newArticle(request, response, session, userId, title, content, tagsId, "论坛");
    }
}

