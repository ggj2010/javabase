package com.ggj.java.teach;

/**
 * @author gaoguangjin
 */
public class StaticTest {
    private static int a=5;


    public static void main(String[] args) {
        StaticTest staticTest=new StaticTest();
        StaticTest.a=6;
        StaticTest staticTest2=new StaticTest();
        System.out.println("args = [" + staticTest2.a + "]");
    }
}
