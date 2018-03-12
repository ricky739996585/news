
package com.kjz.www.photo.vo;
import java.math.BigDecimal;
import java.util.Date;

public class PhotoTypeVo implements java.io.Serializable {
	private Integer photoTypeId; // 相册类型ID
	private String photoTypeName; // 相册类型名称
	private String createTime; // 创建时间
	private String modifyTime; // 修改时间
	private String tbStatus; // 状态：正常，正常；删除，删除；

	public Integer getPhotoTypeId() {
		return photoTypeId;
	}
	public void setPhotoTypeId(Integer photoTypeId) {
		this.photoTypeId = photoTypeId;
	}

	public String getPhotoTypeName() {
		return photoTypeName;
	}
	public void setPhotoTypeName(String photoTypeName) {
		this.photoTypeName = photoTypeName;
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

