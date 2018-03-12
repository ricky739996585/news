
package com.kjz.www.photo.domain;
import java.math.BigDecimal;
import java.util.Date;

public class PhotoType implements java.io.Serializable {
	private Integer photoTypeId; // 相册类型ID
	private String photoTypeName; // 相册类型名称
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

	public String getTbStatus() {
		return tbStatus;
	}
	public void setTbStatus(String tbStatus) {
		this.tbStatus = tbStatus;
	}

}

