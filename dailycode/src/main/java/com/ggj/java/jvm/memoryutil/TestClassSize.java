package com.ggj.java.jvm.memoryutil;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;

/**
 * @author gaoguangjin
 */
public class TestClassSize {

    //int a;                      // 4 字节
    byte b;                     // 1 字节
    //Integer c = new Integer(1);//4


  static Object generate() {
        Object object = new Object();
        return object;
    }

    static void print(String message) {
        System.out.println(message);
        System.out.println("-------------------------");
    }

    public static void main(String[] args) {
        Object obj = generate();

        TestClassSize testClassSize=new TestClassSize();
        //查看对象内部信息
        print(ClassLayout.parseInstance(obj).toPrintable());

        //查看对象外部信息
        print(GraphLayout.parseInstance(obj).toPrintable());

        //获取对象总大小
        print("obj size : " + GraphLayout.parseInstance(obj).totalSize()+" bytes");

        print("testClassSize size : " + GraphLayout.parseInstance(testClassSize).totalSize()+" bytes");
    }
}
