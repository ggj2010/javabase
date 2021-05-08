package com.ggj.java.lettcode;

import lombok.extern.slf4j.Slf4j;

import javax.naming.directory.Attribute;
import java.util.HashSet;
import java.util.Set;

/**
 * 进制之间的转化
 * long to string
 * @author gaoguangjin
 */
@Slf4j
public class LongToString {
    static Set<String> set=new HashSet<>();


    private final static char[] digits = {'0','1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
            'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y'};
    public static String parseRadix(long origin){
        char[] buf = new char[61];
        int charPos = 60;
        while (origin >= 61) {
            buf[charPos--] = digits[(int)((origin % 61))];
            origin = origin / 61;
        }
        buf[charPos] = digits[(int)(origin)];
        return new String(buf, charPos, (61 - charPos));
    }


    public static void main(String[] args) {
        //13位置
        System.out.println(parseRadix(91316840726L));
        System.out.println(Long.toString(7831684072L,32));
        System.out.println(to62RadixString(10000));
        System.out.println(radixString("1L7fuMW"));


        int i=110112;
       String two="";
        while (i>0){
            two=i%2+two;
            i=i/2;
        }
        System.out.println(two);

        System.out.println("args = [" + Integer.toBinaryString(110112) + "]");
    }



    private static String to62RadixString(long seq) {
        StringBuilder sBuilder = new StringBuilder();
        while (true) {
            int remainder = (int) (seq % 61);
            sBuilder.append(digits[remainder]);
            seq = seq / 61;
            if (seq == 0) {
                break;
            }
        }
        return sBuilder.reverse().toString();
    }


    private static long radixString(String str) {
        long sum = 0L;
        int len = str.length();
        for (int i = 0; i < len; i++) {
            sum += indexDigits(str.charAt(len-i-1))*Math.pow((double)61,(double)i);

        }
        return sum;
    }

    private static int indexDigits(char ch){
        for (int i = 0; i < digits.length; i++) {
            if (ch == digits[i]){
                return i;
            }
        }
        return -1;
    }




}
