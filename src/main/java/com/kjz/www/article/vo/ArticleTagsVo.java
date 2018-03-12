
package com.kjz.www.article.vo;
import java.math.BigDecimal;
import java.util.Date;

public class ArticleTagsVo implements java.io.Serializable {
	private Integer articleTagsId; // 文章标签ID
	private Integer tagsId; // 
	private Integer articleId; // 
	private String createTime; // 创建时间
	private String modifyTime; // 修改时间
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

	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getTbStatus() {
		return tbStatus;
	}
	public void setTbStatus(String tbStatus) {
		this.tbStatus = tbStatus;
	}

}

