
package com.kjz.www.advertisement.vo;
import java.math.BigDecimal;
import java.util.Date;

public class AdvertisementVo implements java.io.Serializable {
	private Integer advertisementId; // 主键id
	private String title; // 轮播图内容主题
	private String pic; // 图片url
	private String targetUrl; // 图片跳转的地址
	private Integer orderNum; // 顺序
	private String advertisementType; // 广告类型
	private String createTime; // 创建时间
	private String modifyTime; // 修改时间
	private String tbstatus; // 状态:正常,normal;删除,del;

	public Integer getAdvertisementId() {
		return advertisementId;
	}
	public void setAdvertisementId(Integer advertisementId) {
		this.advertisementId = advertisementId;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getTargetUrl() {
		return targetUrl;
	}
	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public String getAdvertisementType() {
		return advertisementType;
	}
	public void setAdvertisementType(String advertisementType) {
		this.advertisementType = advertisementType;
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
	public String getTbstatus() {
		return tbstatus;
	}
	public void setTbstatus(String tbstatus) {
		this.tbstatus = tbstatus;
	}

	

}

