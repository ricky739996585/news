
package com.kjz.www.photo.vo;
import java.math.BigDecimal;
import java.util.Date;

public class PhotoTypeVoFont implements java.io.Serializable {
	private Integer photoTypeId; // 相册类型ID
	private String photoTypeName; // 相册类型名称
	private String createTime; // 创建时间

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

}

