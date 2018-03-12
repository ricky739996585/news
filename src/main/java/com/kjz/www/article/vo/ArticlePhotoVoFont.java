
package com.kjz.www.article.vo;
import java.math.BigDecimal;
import java.util.Date;

public class ArticlePhotoVoFont implements java.io.Serializable {
	private Integer articlePhotoId; // 文章图片编号
	private Integer articleId; // 文章编号
	private String articlePhotoUrl; // 文章图片地址
	private String createTime; // 创建时间

	public Integer getArticlePhotoId() {
		return articlePhotoId;
	}
	public void setArticlePhotoId(Integer articlePhotoId) {
		this.articlePhotoId = articlePhotoId;
	}

	public Integer getArticleId() {
		return articleId;
	}
	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	public String getArticlePhotoUrl() {
		return articlePhotoUrl;
	}
	public void setArticlePhotoUrl(String articlePhotoUrl) {
		this.articlePhotoUrl = articlePhotoUrl;
	}

	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}

