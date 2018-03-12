
package com.kjz.www.article.vo;
import java.math.BigDecimal;
import java.util.Date;

public class ArticleCommentVoFont implements java.io.Serializable {
	private Integer commentId; // 评论ID
	private Integer userId; // 用户昵称
	private Integer articleId; // 文章ID
	private String commentContent; // 评论内容
	private String createTime; // 创建时间

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

	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}

