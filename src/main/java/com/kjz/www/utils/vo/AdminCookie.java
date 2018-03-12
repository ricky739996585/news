package com.kjz.www.utils.vo;

public class AdminCookie implements java.io.Serializable {
	private Integer adminAccount;
	private String password;
	private String createTime;

	
	public Integer getAdminAccount() {
		return adminAccount;
	}
	public void setAdminAccount(Integer adminAccount) {
		this.adminAccount = adminAccount;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
