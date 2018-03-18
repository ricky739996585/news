package com.kjz.www.utils.vo;

public class MailVo {
	private String senderName;
	private String accepterName;
	private String mailTheme;
	private String content;
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getAccepterName() {
		return accepterName;
	}
	public void setAccepterName(String accepterName) {
		this.accepterName = accepterName;
	}
	public String getMailTheme() {
		return mailTheme;
	}
	public void setMailTheme(String mailTheme) {
		this.mailTheme = mailTheme;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public MailVo() {
		super();
	}
	public MailVo(String senderName, String accepterName, String mailTheme, String content) {
		super();
		this.senderName = senderName;
		this.accepterName = accepterName;
		this.mailTheme = mailTheme;
		this.content = content;
	}
	@Override
	public String toString() {
		return "Mail [senderName=" + senderName + ", accepterName=" + accepterName + ", mailTheme=" + mailTheme
				+ ", content=" + content + "]";
	}
	
	
}
