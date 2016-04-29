package com.ggj.encrypt.common.utils;

import java.security.SecureRandom;

import org.apache.log4j.Logger;

/**
 * @author:gaoguangjin
 * @Description: 生成密钥向量
 * @Email:335424093@qq.com
 * @Date 2016/4/27 11:23
 */
public class CalcKeyIvUtils {
	
	private static Logger log = Logger.getLogger(CalcKeyIvUtils.class);
	
	public static final byte[] KEY_SEED = "1234567890_1234567890_1234567890".getBytes();
	

	/**
	 * 计算一般密钥
	 * @param appKey
	 * @return
	 * @throws Exception
	 */
	public static byte[] getIv(String appKey, String timestamp) throws Exception {
		byte[] b1 = appKey.getBytes();
		b1 = swap(b1);
		byte[] t1 = timestamp.getBytes();
		for (int i = 0; i < 6; i++) {
			b1[i] = t1[timestamp.length() - 2 - i];
		}
		byte[] res = byteXOR(b1, KEY_SEED);
		return res;
	}
	
	/**
	 * 计算一般向量
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public static byte[] getIv(String token) throws Exception {
		byte[] b1 = token.getBytes();
		b1 = swap(b1);
		int halvesLen = token.length() / 2;
		byte[] s1 = new byte[halvesLen];
		byte[] s2 = new byte[halvesLen];
		System.arraycopy(b1, 0, s1, 0, halvesLen);
		System.arraycopy(b1, halvesLen, s2, 0, halvesLen);
		byte[] res = byteXOR(s1, s2);
		return res;
	}

	public static byte[] swap(byte[] b1) {
		for (int i = 0; i < (b1.length - 4); i = i + 4) {
			byte temp = b1[i];
			b1[i] = b1[i + 1];
			b1[i + 1] = b1[i + 2];
			b1[i + 2] = b1[i + 3];
			b1[i + 3] = temp;
		}
		return b1;
	}
	
	/**
	 * 两个字符异或操作
	 * 
	 * @param byte1 字节数组1
	 * @param byte2 字节数组2
	 * @return
	 * @throws Exception
	 */
	public static byte[] byteXOR(byte[] byte1, byte[] byte2) throws Exception {
		if (byte1.length != byte2.length) {
			System.out.println("两个字节数组长度不一(" + byte1.length + "," + byte2.length + "),取短");
			throw new Exception("两个字节数组长度不一");
		}
		int len = byte1.length > byte2.length ? byte2.length : byte1.length;
		byte[] r = new byte[len];
		byte temp3;
		for (int i = 0; i < len; i++) {
			byte temp1 = byte1[i];
			byte temp2 = byte2[i];
			temp3 = (byte) (temp1 ^ temp2);
			r[i] = temp3;
		}
		for (int i = 0; i < r.length; i++) {
			int v = r[i];
			boolean isTrue = (v >= 48 && v <= 57) || (v >= 65 && v <= 90) || (v >= 97 && v <= 122);
			if (!isTrue) {
				int m = 97 + Math.abs(r[i]) % 25; // 模,值在0-25中间
				r[i] = (byte) m;
			}
		}
		return r;
	}
	
	public static String genRandomStr(int len) {
		// 35是因为数组是从0开始的，52个字母+10个数字
		final int maxNum = 62;
		int i; // 生成的随机数
		int count = 0; // 生成的密码的长度
		char[] str = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
				'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
				'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z','A',
				'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
				'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
		StringBuffer pwd = new StringBuffer();
		SecureRandom sr = new SecureRandom();
		while (count < len) {
			//生成随机数，取绝对值，防止生成负数，
			i = Math.abs(sr.nextInt(maxNum)); // 生成的数最大为36-1
			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}
		String random = pwd.toString();
		//log.info("随机数原串：" + random);
		return random;
	}
	
	public static void showBytes(byte[] bytes) {
		if (null == bytes) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		for (byte b : bytes) {
			sb.append(b + " ");
		}
		sb.append("]");
		System.out.println(sb.toString());
	}
}
