
package com.kjz.www.article.domain;
import java.math.BigDecimal;
import java.util.Date;

public class ArticleComment implements java.io.Serializable {
	private Integer commentId; // 评论ID
	private Integer userId; // 用户昵称
	private Integer articleId; // 文章ID
	private String commentContent; // 评论内容
	private String tbStatus; // 状态：正常，正常；删除，删除；

	public Integer getCommentId() {
		return commentId;
	}
	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getArticleId() {
		return articleId;
	}
	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public String getTbStatus() {
		return tbStatus;
	}
	public void setTbStatus(String tbStatus) {
		this.tbStatus = tbStatus;
	}

}

