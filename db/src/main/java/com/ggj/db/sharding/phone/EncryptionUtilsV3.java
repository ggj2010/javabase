package com.ggj.db.sharding.phone;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.Key;

@Slf4j
public class EncryptionUtilsV3 {
    private static final String ENCODING_KEY = "ASCII";
    private static final String ENCODING_TEXT = "UTF-8";


    /**
     * 区别AES算法对空格字符的混淆
     */
    private static final String SPACE_INSTEAD = "~$#!^";
    private static final String SPACE2_INSTEAD = "^#~$!";
    private static final String SPACE2 = String.valueOf((char) 0);
    private static final String SPACELIST = " " + SPACE2;
    /**
     * AES算法使用的KEY在Lion中的配置项名称
     */
    private static String ENCRYPT_KEY = null;
    private static final String ENCRYPT_IV = "nv926fjl,dv6yuia";

    static {
        //从kms获取aes秘钥  256
        ENCRYPT_KEY ="123456789123";
        System.out.println(ENCRYPT_KEY.getBytes().length);
    }

    //不可实例化
    private EncryptionUtilsV3(){

    }
    /**
     * 加密普通文本
     *
     * @param text
     *            待加密文本
     * @return
     */
    public static String encrypt(String text) {
        // 空处理
        if (StringUtils.isBlank(text)) {
            return "";
        }
        text = text.replace(" ", SPACE_INSTEAD).replace(SPACE2, SPACE2_INSTEAD);
        try{
            byte[] bytes = text.getBytes(ENCODING_TEXT);
            byte[] encryptedBytes = CommonUtils.encrypt(bytes, (text.substring(text.length()-4,text.length())+ENCRYPT_KEY).getBytes(ENCODING_KEY),
                    ENCRYPT_IV.getBytes(ENCODING_KEY));
            if (!ArrayUtils.isEmpty(encryptedBytes)) {
                return parseByte2Hex(encryptedBytes)+"_2";
            }
        }catch (UnsupportedEncodingException e) {
            log.warn("EncryptionUtils.encrypt.UnsupportedEncodingException", e);
        } catch (Exception e) {
            log.error(String.valueOf(e), e);
        }
        return "";
    }

    /**
     * 解密普通文本(不可解密由.Net加密的普通文本)
     *
     * @param cipherText
     *            待解密文本
     * @return
     */
    public static String decrypt(String cipherText,String salt) {
        // 空处理
        if (StringUtils.isBlank(cipherText)) {
            return "";
        }
        String content = cipherText.lastIndexOf("_") > 0 ? cipherText.substring(0, cipherText.lastIndexOf("_")) : cipherText;
        try {
            byte[] cipherBytes = parseHex2Byte(content);
            byte[] decryptedBytes = CommonUtils.decrypt(cipherBytes, (salt+ENCRYPT_KEY).getBytes(ENCODING_KEY), ENCRYPT_IV
                    .getBytes(ENCODING_KEY));
            if (!ArrayUtils.isEmpty(decryptedBytes)) {
                String decrypted = new String(decryptedBytes, ENCODING_TEXT);
                return StringUtils.strip(decrypted, SPACELIST).replace(SPACE_INSTEAD, " ")
                        .replace(SPACE2_INSTEAD, SPACE2);
            }
        } catch (UnsupportedEncodingException e) {
            log.warn("EncryptionUtils.decrypt.UnsupportedEncodingException", e);
        } catch (Exception e) {
            log.warn(String.valueOf(e), e);
        }
        return "";
    }

    /**
     * 转换为16进制字符串
     * @param bytes
     * @return
     */
    private static String parseByte2Hex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toLowerCase());
        }
        return sb.toString();
    }

    /**
     * 16进制字符转化为byte数组
     * @param hexText
     * @return
     */
    private static byte[] parseHex2Byte(String hexText) {
        if (StringUtils.isEmpty(hexText)) {
            return null;
        }
        byte[] result = new byte[hexText.length() / 2];
        for (int i = 0; i < hexText.length() / 2; i++) {
            int high = Integer.parseInt(hexText.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexText.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}
