package com.ggj.java.jvm.memoryutil;

import java.io.IOException;

/**
 * @author gaoguangjin
 */
public class TestClass {
    public static void main(String[] args) throws IOException {
        System.in.read();
        String a = "";
        //11128
        //12052
        //12184
        for (int i = 0; i <= 1024; i++) {
            a = a + "a";
        }

        System.in.read();
    }

}
