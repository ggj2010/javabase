
package com.ggj.webmagic.cat.util;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesEncryptionUtil {
    private static final String CipherMode = "AES/CBC/PKCS5Padding";
    public static final String STRING_CHARSET_NAME = "UTF-8";
    public static final String AES_IV = "5efd3f6060emaomi";
    public static final String AES_PWD = "625202f9149maomi";

    private static SecretKeySpec createKey(String key) {
        byte[] data = null;
        if (key == null) {
            key = "";
        }
        StringBuffer sb = new StringBuffer(16);
        sb.append(key);
        while (sb.length() < 16) {
            sb.append("0");
        }
        if (sb.length() > 16) {
            sb.setLength(16);
        }
        try {
            data = sb.toString().getBytes(STRING_CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new SecretKeySpec(data, "AES");
    }

    private static IvParameterSpec createIV(String password) {
        byte[] data = null;
        if (password == null) {
            password = "";
        }
        StringBuffer sb = new StringBuffer(16);
        sb.append(password);
        while (sb.length() < 16) {
            sb.append("0");
        }
        if (sb.length() > 16) {
            sb.setLength(16);
        }
        try {
            data = sb.toString().getBytes(STRING_CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new IvParameterSpec(data);
    }

    public static byte[] encrypt(byte[] content, String password, String iv) {
        try {
            SecretKeySpec key = createKey(password);
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(1, key, createIV(iv));
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encrypt(String content, String password, String iv) {
        byte[] data = null;
        try {
            data = content.getBytes(STRING_CHARSET_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byte2hex(encrypt(data, password, iv));
    }

    public static byte[] decrypt(byte[] content, String password, String iv) {
        try {
            SecretKeySpec key = createKey(password);
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(2, key, createIV(iv));
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String content, String password, String iv) {
        byte[] data = null;
        try {
            data = hex2byte(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        data = decrypt(data, password, iv);
        if (data == null) {
            return null;
        }
        try {
            return new String(data, STRING_CHARSET_NAME);
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static String byte2hex(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        String tmp = "";
        for (byte b2 : b) {
            tmp = Integer.toHexString(b2 & 255);
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
        }
        return sb.toString().toUpperCase();
    }

    private static byte[] hex2byte(String inputString) {
        if (inputString == null || inputString.length() < 2) {
            return new byte[0];
        }
        inputString = inputString.toLowerCase();
        int l = inputString.length() / 2;
        byte[] result = new byte[l];
        for (int i = 0; i < l; i++) {
            result[i] = (byte) (Integer.parseInt(inputString.substring(i * 2, (i * 2) + 2), 16)
                    & 255);
        }
        return result;
    }

    public static String decryptMain(String result) {
       return decrypt(result,AES_PWD,AES_IV);
    }
    public static String encryptMain(String result) {
       return encrypt(result,AES_PWD,AES_IV);
    }

    public static void main(String[] args) {
        System.out.println(decryptMain(""));
        System.out.println(decryptMain(""));
    }
}
