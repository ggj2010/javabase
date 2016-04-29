package com.ggj.encrypt.common.utils;

import java.io.IOException;
import java.net.URLDecoder;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @ClassName:DesUtil.java
 * @Description:  分组对称加密算法util  
 * @author gaoguangjin
 * @Date 2015-9-25 上午10:13:54
 */
public class DesUtil {
	
	private final static String DES = "DES";
	// KEY种子
	public static final String KEY_STR = "http://www.ggjlovezjy.com";
	private static String data;
	

	public static String encrypt(String data) throws Exception {
		return encrypt( data, KEY_STR);
	}
	
	/**
	 * Description 根据键值进行加密
	 * @param data 
	 * @param key  加密键byte数组
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String data, String key) throws Exception {
		byte[] bt = encrypt(data.getBytes(), key.getBytes());
		String strs = new BASE64Encoder().encode(bt);
		return strs;
	}
	
	/**
	 * Description 根据键值进行解密
	 * @param data
	 * @param key  加密键byte数组
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static String decrypt(String data, String key) throws IOException, Exception {
		if (data == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] buf = decoder.decodeBuffer(data);
		byte[] bt = decrypt(buf, key.getBytes());
		return new String(bt);
	}


	/**
	 * Description 根据键值进行加密
	 * @param data
	 * @param key  加密键byte数组
	 * @return
	 * @throws Exception
	 */
	private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		// 生成一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		
		// 从原始密钥数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		
		// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		
		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance(DES);
		
		// 用密钥初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
		
		return cipher.doFinal(data);
	}
	
	/**
	 * Description 根据键值进行解密
	 * @param data
	 * @param key  加密键byte数组
	 * @return
	 * @throws Exception
	 */
	private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		// 生成一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		
		// 从原始密钥数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		
		// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance(DES);
		
		// 用密钥初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
		
		return cipher.doFinal(data);
	}

	/**
	 * 带向量的加密
	 * @param str
	 * @param secretKey
	 * @param iv
	 * @return
     * @throws Exception
     */
	public static String encrypt(String str, String secretKey, byte[] iv) throws Exception {
		IvParameterSpec zeroIv = new IvParameterSpec(iv);
		SecretKeySpec key = new SecretKeySpec(secretKey.getBytes("UTF-8"), "DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
		byte[] encryptedData = cipher.doFinal(str.getBytes("UTF-8"));
		byte[] temp = Base64.encodeBase64(encryptedData, false);
		return new String(temp);
	}

	/**
	 * 带向量的解密
	 * @param str
	 * @param secretKey
	 * @param iv
	 * @return
     * @throws Exception
     */
	public static String decrypt(String str, String secretKey, byte[] iv) throws Exception {
		if (str == null || "".equals(str)) {
			return str;
		}
		String str1 = URLDecoder.decode(str, "UTF-8");
		byte str2[] = Base64.decodeBase64(str1.getBytes());
		IvParameterSpec zeroIv = new IvParameterSpec(iv);
		SecretKeySpec key = new SecretKeySpec(secretKey.getBytes("UTF-8"), "DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
		byte[] str3 = cipher.doFinal(str2);
		String res = str;
		if (str3 != null) {
			res = new String(str3, "UTF-8");
		}
		return res;
	}
}
