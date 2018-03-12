
package com.kjz.www.article.vo;
import java.math.BigDecimal;
import java.util.Date;

public class ArticleCollectVoFont implements java.io.Serializable {
	private Integer articleCollectId; // 文章收集编号
	private Integer articleId; // 文章编号
	private Integer userId; // 用户编号
	private String createTime; // 创建时间

	public Integer getArticleCollectId() {
		return articleCollectId;
	}
	public void setArticleCollectId(Integer articleCollectId) {
		this.articleCollectId = articleCollectId;
	}

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

	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}

