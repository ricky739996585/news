
package com.kjz.www.tags.vo;
import java.math.BigDecimal;
import java.util.Date;

public class TagsVo implements java.io.Serializable {
	private Integer tagsId; // 标签编号
	private String tagsName; // 标签名
	private String createTime; // 创建时间
	private String modifyTime; // 修改时间
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

