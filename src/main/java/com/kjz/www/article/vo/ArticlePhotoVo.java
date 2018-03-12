
package com.kjz.www.article.vo;
import java.math.BigDecimal;
import java.util.Date;

public class ArticlePhotoVo implements java.io.Serializable {
	private Integer articlePhotoId; // 文章图片编号
	private Integer articleId; // 文章编号
	private String articlePhotoUrl; // 文章图片地址
	private String createTime; // 创建时间
	private String modifyTime; // 修改时间
	private String tbStatus; // 状态：正常，正常；删除，删除；

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

