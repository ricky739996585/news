
package com.kjz.www.article.domain;
import java.math.BigDecimal;
import java.util.Date;

public class Article implements java.io.Serializable {
	private Integer articleId; // 文章ID
	private Integer userId; // 用户ID
	private String title; // 文章标题
	private String content; // 文章内容
	private Integer clicks; // 浏览次数
	private String typeName; // 文章类型
	private String isPass; // 状态：未审核，未审核；通过，通过；不通过，不通过；
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

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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

}

