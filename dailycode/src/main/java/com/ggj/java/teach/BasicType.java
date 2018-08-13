package com.ggj.java.teach;

import java.util.Scanner;

/**
 * @author:gaoguangjin
 * @date:2018/4/2
 */
public class BasicType {
    private static boolean boolFlag;

    public static void main(String[] args) {
        new BasicType().tests();

        //byte 等于8 bit  bit为二进制的单位
        byte maxByte=Byte.MAX_VALUE;
        byte minByte=Byte.MIN_VALUE;

        int maxInt=2147483647+1;
        System.out.println("maxInt = [" + maxInt + "]");
        System.out.println("最大值:"+maxByte+";最小值"+minByte);
        //1 int=4 byte
        int a=Integer.MAX_VALUE;
        int b=Integer.MIN_VALUE;
        //int类型数据范围
        System.out.println("最大值:"+a+";最小值"+b);
        long c=Long.MAX_VALUE;
        long d=Long.MIN_VALUE;
        //long类型数据范围
        System.out.println("最大值:"+c+";最小值"+d);

        if(boolFlag){

        }
        //类型转换
        //低类型往高类型自动转换
        int e=maxByte;

        //高类型往地类型转换需要强制，可能溢出或者丢失精度
        int f= (int) c;
        System.out.println("long类型强制转换成int类型，溢出，原来是："+c+";转换后是："+f);
        //溢出
        int g=a+a;
        System.out.println(g);

        float h=11.00f;
        //丢失精度
        int i= (int) h;
        System.out.println("丢失精度："+i);

        float j=a;
        System.out.println(""+j);

        //从低位类型到高位类型自动转换；从高位类型到低位类型需要强制类型转换：
        //算术运算 中的类型转换：1 基本就是先转换为高位数据类型，再参加运算，结果也是最高位的数据类型；

        System.out.println(h/2);
        System.out.println(16/2.0);

        char charA='a';
        //char+char，char+int——类型均提升为int
        int charB=charA+charA;
        int charC=charA+2;
        int changeA=charA;
        char china='中';
        System.out.println(china+0);
        System.out.println("args = [" + changeA + "]");

    }

    private void tests() {
        System.out.println("test 全局变量"+boolFlag+";");
    }

}
