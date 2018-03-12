/*** 生成自 bingowu的 autoWeb 于 2018年01月22日 V2.0.0 版本  */
package com.kjz.www.common;

public class Config {

	public static String VERSION = "V1.0.0";

	// session ------------------------ start ---------------------------
	public static String USER_SESSION_NAME = "user";
	public static String mobileInSession = "mobile";
	public static String verificationCodeInSession = "verificationCode";
	public static String verificationCodeSetTimeInSession = "verificationCodeSetTime";
	public static String verificationCodeMapInSession = "verificationCodeMap";
	public static String graphicVerificationCodeInSession = "graphicVerificationCode";
	// session ------------------------ end ---------------------------

	// user ------------------------ start ---------------------------
	public static String userSaltCharacter = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIGKLMNOPQRSTUVWXYZ";
	public static Integer userSaltNumber = 4;
	public static String verificationCodeCharacter = "012345678998765432101357924680086429753196385207411470258369";
	public static Integer verificationCodeNumber = 4;
	// user ----------------------- end --------------------------
}
