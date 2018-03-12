
package com.kjz.www.admin.vo;
import java.math.BigDecimal;
import java.util.Date;

public class AdminVo implements java.io.Serializable {
	private Integer adminId; // 管理员编号
	private String adminPassword; // 管理员密码
	private String adminAccount; // 管理员账号
	private String adminName; // 管理员名称
	private String adminSex; // 用户性别
	private String adminIp; // 用户IP地址
	private String adminRegisterTime; // 管理员注册时间
	private String adminLastLogin; // 上次登录时间
	private Integer adminLoginTimes; // 登录次数
	private String adminLevel; // 等级：普通管理员，普通管理员；超级管理员，超级管理员；
	private String createTime; // 创建时间
	private String modifyTime; // 修改时间
	private String tbStatus; // 状态：正常，正常；删除，删除；

	public Integer getAdminId() {
		return adminId;
	}
	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	public String getAdminPassword() {
		return adminPassword;
	}
	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public String getAdminAccount() {
		return adminAccount;
	}
	public void setAdminAccount(String adminAccount) {
		this.adminAccount = adminAccount;
	}

	public String getAdminName() {
		return adminName;
	}
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getAdminSex() {
		return adminSex;
	}
	public void setAdminSex(String adminSex) {
		this.adminSex = adminSex;
	}

	public String getAdminIp() {
		return adminIp;
	}
	public void setAdminIp(String adminIp) {
		this.adminIp = adminIp;
	}

	public String getAdminRegisterTime() {
		return adminRegisterTime;
	}
	public void setAdminRegisterTime(String adminRegisterTime) {
		this.adminRegisterTime = adminRegisterTime;
	}

	public String getAdminLastLogin() {
		return adminLastLogin;
	}
	public void setAdminLastLogin(String adminLastLogin) {
		this.adminLastLogin = adminLastLogin;
	}

	public Integer getAdminLoginTimes() {
		return adminLoginTimes;
	}
	public void setAdminLoginTimes(Integer adminLoginTimes) {
		this.adminLoginTimes = adminLoginTimes;
	}

	public String getAdminLevel() {
		return adminLevel;
	}
	public void setAdminLevel(String adminLevel) {
		this.adminLevel = adminLevel;
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

