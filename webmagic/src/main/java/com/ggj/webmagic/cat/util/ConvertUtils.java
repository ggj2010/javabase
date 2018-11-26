package com.ggj.webmagic.cat.util;


public class ConvertUtils {
    public static final int GB = 1073741824;
    public static final int KB = 1024;
    public static final int MB = 1048576;
    static final char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private ConvertUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static String bytes2HexString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        int len = bytes.length;
        if (len <= 0) {
            return null;
        }
        char[] ret = new char[(len << 1)];
        int j = 0;
        for (int i = 0; i < len; i++) {
            int i2 = j + 1;
            ret[j] = hexDigits[(bytes[i] >>> 4) & 15];
            j = i2 + 1;
            ret[i2] = hexDigits[bytes[i] & 15];
        }
        return new String(ret);
    }

}
