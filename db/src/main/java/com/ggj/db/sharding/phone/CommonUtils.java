package com.ggj.db.sharding.phone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 通用底层工具类，包括加密，码表操作
 */
public class CommonUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);
    private static char[] charMap = null;

    private CommonUtils(){

    }

    static{
        //从kms获取码表
        charMap = new char[]{'a', 'b'};
    }

    /**
     * 使用AES算法,使用指定key和IV对指定字节数组进行加密
     */
     static byte[] encrypt(byte[] bytes, byte[] key, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
            SecretKeySpec KeySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, KeySpec, new IvParameterSpec(iv));
            return cipher.doFinal(bytes);
        } catch (Exception e) {
            LOGGER.error(String.valueOf(e), e);
            return null;
        }
    }

    /**
     * 使用AES算法，使用指定key和IV对指定字节数组进行解密
     */
    static byte[] decrypt(byte[] bytes, byte[] key, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
            SecretKeySpec KeySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, KeySpec, new IvParameterSpec(iv));
            return cipher.doFinal(bytes);
        } catch (Exception e) {
            LOGGER.warn(String.valueOf(e), e);
            return null;
        }
    }

    /**
     * 查询一个字符在码表中的位置
     * @param x
     * @return
     */
    static int findIndexinCharMap(char x){
        for(int i = 0;i<charMap.length;i++){
            if (charMap[i] == x){
                return i;
            }
        }
        return -1;
    }
}
