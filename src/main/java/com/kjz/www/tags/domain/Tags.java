
package com.kjz.www.tags.domain;
import java.math.BigDecimal;
import java.util.Date;

public class Tags implements java.io.Serializable {
	private Integer tagsId; // 标签编号
	private String tagsName; // 标签名
	private String tbStatus; // 状态：正常，正常；删除，删除；

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

	public String getTbStatus() {
		return tbStatus;
	}
	public void setTbStatus(String tbStatus) {
		this.tbStatus = tbStatus;
	}

}

