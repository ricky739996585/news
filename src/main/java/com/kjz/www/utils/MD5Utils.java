package com.kjz.www.utils;

import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.util.Random;

@Component("md5Utils")
public class MD5Utils {
	//产生md5：密码+盐值的随机字符串
    public static String generate(String password,String salt) {
        password = md5Hex(password + salt);
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = password.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        return new String(cs);
    }
    /**
     * 获取十六进制字符串形式的MD5摘要
     */
    public static String md5Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(src.getBytes());
            return new String(new Hex().encode(bs));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 校验密码是否正确
     */
    public static boolean verify(String password, String md5) {
        char[] cs1 = new char[32];
        char[] cs2 = new char[16];
        for (int i = 0; i < 48; i += 3) {
            cs1[i / 3 * 2] = md5.charAt(i);
            cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
            cs2[i / 3] = md5.charAt(i + 1);
        }
        String salt = new String(cs2);
        return md5Hex(password + salt).equals(new String(cs1));
    }

    //无盐值：
    private static String[] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
    //将明文字符中以MD5方式加密后返回可读的十六进制数字符串
    public static String encodeByMd5(String password) throws Exception{
		MessageDigest md5 = MessageDigest.getInstance("md5");
		byte[] results = md5.digest(password.getBytes());
		return byteArrayToString(results);
	}
	//将byte[]转成String返回
	private static String byteArrayToString(byte[] results){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<results.length;i++){
			//将每一位byte转发String
			sb.append(byteToString(results[i]));
		}
		return sb.toString();
	}
	//将每一位byte转发String(核心算法)
	private static String byteToString(byte b){
		int n = b;
		if(n < 0 ){
			n = 256 + n ; 
		}
		//十六进制的第一位
		int d1 = n / 16;
		
		//十六进制的第二位
		int d2 = n % 16;
		
		//(十)  0-255 共256个
		//(十六)0-FF
		
		return hex[d1] + hex[d2];
	}

}


