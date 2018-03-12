
package com.kjz.www.article.domain;
import java.math.BigDecimal;
import java.util.Date;

public class ArticleTags implements java.io.Serializable {
	private Integer articleTagsId; // 文章标签ID
	private Integer tagsId; // 
	private Integer articleId; // 
	private String tbStatus; // 状态：正常，正常；删除，删除；

	public Integer getArticleTagsId() {
		return articleTagsId;
	}
	public void setArticleTagsId(Integer articleTagsId) {
		this.articleTagsId = articleTagsId;
	}

	public Integer getTagsId() {
		return tagsId;
	}
	public void setTagsId(Integer tagsId) {
		this.tagsId = tagsId;
	}

	public Integer getArticleId() {
		return articleId;
	}
	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	public String getTbStatus() {
		return tbStatus;
	}
	public void setTbStatus(String tbStatus) {
		this.tbStatus = tbStatus;
	}

}

