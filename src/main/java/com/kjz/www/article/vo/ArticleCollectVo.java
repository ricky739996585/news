
package com.kjz.www.article.vo;
import java.math.BigDecimal;
import java.util.Date;

public class ArticleCollectVo implements java.io.Serializable {
	private Integer articleCollectId; // 文章收集编号
	private Integer articleId; // 文章编号
	private Integer userId; // 用户编号
	private String createTime; // 创建时间
	private String modifyTime; // 修改时间
	private String tbStatus; // 状态：正常，正常；删除，删除；

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

