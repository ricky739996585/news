
package com.kjz.www.photo.vo;
import java.math.BigDecimal;
import java.util.Date;

public class PhotoVoFont implements java.io.Serializable {
	private Integer photoId; // 照片ID
	private Integer userId; // 用户ID
	private String photoName; // 照片名称
	private String photoSrc; // 照片路径
	private String photoDescription; // 照片描述
	private String photoTypeName; // 相册类型名
	private String createTime; // 创建时间

	public Integer getPhotoId() {
		return photoId;
	}
	public void setPhotoId(Integer photoId) {
		this.photoId = photoId;
	}

	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getPhotoName() {
		return photoName;
	}
	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}

	public String getPhotoSrc() {
		return photoSrc;
	}
	public void setPhotoSrc(String photoSrc) {
		this.photoSrc = photoSrc;
	}

	public String getPhotoDescription() {
		return photoDescription;
	}
	public void setPhotoDescription(String photoDescription) {
		this.photoDescription = photoDescription;
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

