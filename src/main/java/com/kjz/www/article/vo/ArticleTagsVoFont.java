
package com.kjz.www.article.vo;
import java.math.BigDecimal;
import java.util.Date;

public class ArticleTagsVoFont implements java.io.Serializable {
	private Integer articleTagsId; // 文章标签ID
	private Integer tagsId; // 
	private Integer articleId; // 
	private String createTime; // 创建时间

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

	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}

