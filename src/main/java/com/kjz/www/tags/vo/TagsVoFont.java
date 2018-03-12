
package com.kjz.www.tags.vo;
import java.math.BigDecimal;
import java.util.Date;

public class TagsVoFont implements java.io.Serializable {
	private Integer tagsId; // 标签编号
	private String tagsName; // 标签名
	private String createTime; // 创建时间

	public Integer getTagsId() {
		return tagsId;
	}
	public void setTagsId(Integer tagsId) {
		this.tagsId = tagsId;
	}

	public String getTagsName() {
		return tagsName;
	}
	public void setTagsName(String tagsName) {
		this.tagsName = tagsName;
	}

	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}

