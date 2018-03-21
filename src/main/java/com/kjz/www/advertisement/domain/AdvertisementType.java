
package com.kjz.www.advertisement.domain;
import java.math.BigDecimal;
import java.util.Date;

public class AdvertisementType implements java.io.Serializable {
	private Integer advertisementTypeId; // 广告类型ID
	private String typeName; // 类型名称
	private Integer orderNum; // 顺序
	private String status; // 状态:正常，正常；删除，删除;

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

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}

