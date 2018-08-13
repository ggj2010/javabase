package com.ggj.java.teach;

import java.util.Scanner;

/**
 * @author:gaoguangjin
 * @date:2018/4/29
 */
public class Main {
    public static void main(String[] args) {
//        OverrideToString overrideToString =new OverrideToString();
//        new OverrideToString();
//        new OverrideToString("NAME",2);
//        //如果这个对象 没有重写toString方法，那么打印的就是String.valueOf(Object), 也就是这个Object.toString
//        // this.getClass().getName() + "@" + Integer.toHexString(this.hashCode());
//        System.out.println(overrideToString);
//        System.out.println(1);

        Work work=new Work();
        System.out.println("请输入要工作的孩子数字");
        new Scanner(System.in).nextInt();
        Parent parent=work.getWorkByName("1");
        parent.work();
    }



     class TestInner{



    }
}
