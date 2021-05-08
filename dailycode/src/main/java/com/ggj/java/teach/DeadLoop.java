package com.ggj.java.teach;

import java.io.IOException;

/**
 * @author gaoguangjin
 */
public class DeadLoop {
    public static void main(String[] args) throws IOException {
        System.in.read();
        new Thread(()->{
            while (true){
                System.out.println("args = [" + "" + "]");
            }
        }).start();
    }
}
