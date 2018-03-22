
package com.kjz.www.advertisement.vo;
import java.math.BigDecimal;
import java.util.Date;

public class AdvertisementTypeVoFont implements java.io.Serializable {
	private Integer advertisementTypeId; // 广告类型ID
	private String typeName; // 类型名称
	private Integer orderNum; // 顺序
	private String createTime; // 创建时间
	private String tbstatus; // 状态:正常，正常；删除，删除;

	public Integer getAdvertisementTypeId() {
		return advertisementTypeId;
	}
	public void setAdvertisementTypeId(Integer advertisementTypeId) {
		this.advertisementTypeId = advertisementTypeId;
	}

	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getTbstatus() {
		return tbstatus;
	}
	public void setTbstatus(String tbstatus) {
		this.tbstatus = tbstatus;
	}



}

