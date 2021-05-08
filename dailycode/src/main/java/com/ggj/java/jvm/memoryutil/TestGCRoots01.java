package com.ggj.java.jvm.memoryutil;

/**
 * @author gaoguangjin
 */
public class TestGCRoots01 {

    int _10MB = 10 * 1024 * 1024;
    byte[] memory = new byte[8 * _10MB];
    public static void main(String[] args) {
         method01();
        System.out.println("返回main方法");
        System.gc();
        System.out.println("第二次GC完成");
    }

    public static void method01() {
        TestGCRoots01 t = new TestGCRoots01();
        t.test();
      //  t=null;
        System.gc();
        System.out.println("第一次GC完成");
    }

    public static void test() {
       // int _10MB = 10 * 1024 * 1024;
       // byte[] memory = new byte[8 * _10MB];
    }
}

