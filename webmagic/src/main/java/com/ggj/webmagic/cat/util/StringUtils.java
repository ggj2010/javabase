package com.ggj.webmagic.cat.util;

public class StringUtils {
    private StringUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }

    public static boolean isSpace(String s) {
        return s == null || s.trim().length() == 0;
    }

    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) {
            return true;
        }
        if (!(a == null || b == null)) {
            int length = a.length();
            if (length == b.length()) {
                if ((a instanceof String) && (b instanceof String)) {
                    return a.equals(b);
                }
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static boolean equalsIgnoreCase(String a, String b) {
        if (a != b) {
            if (b == null || a.length() != b.length()) {
                return false;
            }
            if (!a.regionMatches(true, 0, b, 0, b.length())) {
                return false;
            }
        }
        return true;
    }

    public static String null2Length0(String s) {
        return s == null ? "" : s;
    }

    public static int length(CharSequence s) {
        return s == null ? 0 : s.length();
    }

    public static String upperFirstLetter(String s) {
        return (isEmpty(s) || !Character.isLowerCase(s.charAt(0))) ? s : String.valueOf((char) (s.charAt(0) - 32)) + s.substring(1);
    }

    public static String lowerFirstLetter(String s) {
        return (isEmpty(s) || !Character.isUpperCase(s.charAt(0))) ? s : String.valueOf((char) (s.charAt(0) + 32)) + s.substring(1);
    }

    public static String reverse(String s) {
        int len = length(s);
        if (len <= 1) {
            return s;
        }
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        for (int i = 0; i < mid; i++) {
            char c = chars[i];
            chars[i] = chars[(len - i) - 1];
            chars[(len - i) - 1] = c;
        }
        return new String(chars);
    }

    public static String toDBC(String s) {
        if (isEmpty(s)) {
            return s;
        }
        char[] chars = s.toCharArray();
        int i = 0;
        int len = chars.length;
        while (i < len) {
            if (chars[i] == '　') {
                chars[i] = ' ';
            } else if ('！' > chars[i] || chars[i] > '～') {
                chars[i] = chars[i];
            } else {
                chars[i] = (char) (chars[i] - 65248);
            }
            i++;
        }
        return new String(chars);
    }

    public static String toSBC(String s) {
        if (isEmpty(s)) {
            return s;
        }
        char[] chars = s.toCharArray();
        int i = 0;
        int len = chars.length;
        while (i < len) {
            if (chars[i] == ' ') {
                chars[i] = '　';
            } else if ('!' > chars[i] || chars[i] > '~') {
                chars[i] = chars[i];
            } else {
                chars[i] = (char) (chars[i] + 65248);
            }
            i++;
        }
        return new String(chars);
    }

    public static String makeSafe(String s) {
        return s == null ? "" : s;
    }

    public static String trim(String str) {
        return str == null ? "" : str.trim();
    }
}
