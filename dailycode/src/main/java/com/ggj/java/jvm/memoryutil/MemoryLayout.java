package com.ggj.java.jvm.memoryutil;

/**
 * java堆:对象实例、数组
 * java虚拟机栈:
 * 局部变量（各种基本类型的变量、对象变量引用地址）
 * 方法区：
 * 虚拟机加载的类信息、静态变量、常量及时编译器后的代码
 * 被static修饰的变量是静态变量，从JVM规范层面看，它会存储在“方法区”（method area）这个运行时数据区里
 * jdk1.8之后常量池、静态成员变量等迁移到了堆中
 * @author gaoguangjin
 */
public class MemoryLayout {
    //方法区
    private final static int a = 123;

    //方法区 存放testClass的引用
    private final static TestClass testClass = new TestClass();

    //栈
    private  int b = 123;

    //数组引用是存放在Java栈中的 数组是存放在堆里面
    private final static int[] intArrya = new int[1024 * 1024];

    public static void test() {
        //栈
        int temp = 10;
        //数组引用是存放在Java栈中的 数组是存放在堆里面
        byte[] byteArray = new byte[1024 * 1024];
        MemoryLayout memoryLayout=new MemoryLayout();
    }

    public static void main(String[] args) {
        test();
    }
}
