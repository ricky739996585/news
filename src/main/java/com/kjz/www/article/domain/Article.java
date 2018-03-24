
package com.kjz.www.article.domain;

public class Article implements java.io.Serializable {
	private Integer articleId; // 文章编号
	private Integer userId; // 用户编号
	private String title; // 文章标题
	private String author;//原作者
	private String content; // 文章内容
	private String preContent; // 预览内容
	private Integer clicks; // 浏览次数
	private String typeName; // 文章类型
	private String isPass; // 状态：未审核，未审核；通过，通过；不通过，不通过；
	private String top; //置顶状态：正常，正常；置顶，置顶；
	private String topTime; //置顶时间
	private String tbStatus; // 状态：正常，正常；删除，删除；

	public Integer getArticleId() {
		return articleId;
	}
	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public String getPreContent() {
		return preContent;
	}
	public void setPreContent(String preContent) {
		this.preContent = preContent;
	}

	public Integer getClicks() {
		return clicks;
	}
	public void setClicks(Integer clicks) {
		this.clicks = clicks;
	}

	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getIsPass() {
		return isPass;
	}
	public void setIsPass(String isPass) {
		this.isPass = isPass;
	}

	public String getTbStatus() {
		return tbStatus;
	}
	public void setTbStatus(String tbStatus) {
		this.tbStatus = tbStatus;
	}


	public String getTop() {
		return top;
	}

	public void setTop(String top) {
		this.top = top;
	}


	public String getTopTime() {
		return topTime;
	}

	public void setTopTime(String topTime) {
		this.topTime = topTime;
	}
}

