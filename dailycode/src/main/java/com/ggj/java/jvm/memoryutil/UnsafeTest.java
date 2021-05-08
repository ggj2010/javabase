package com.ggj.java.jvm.memoryutil;

import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author gaoguangjin
 */
@Slf4j
public class UnsafeTest {

    private int a;

    /**
     * 字段value的内存偏移地址
     * @param args
     */
    public static void main(String[] args) {
        offsetTest();

        AtomicInteger atomicInteger=new AtomicInteger();
        atomicInteger.getAndIncrement();
    }

    private static void offsetTest() {
        MemoryUtil.NodeA nodeA = new MemoryUtil.NodeA();
        try {
            long ageOffset = reflectGetUnsafe().objectFieldOffset(nodeA.getClass().getDeclaredField("age"));
            long nodeBOffset = reflectGetUnsafe().objectFieldOffset(nodeA.getClass().getDeclaredField("nodeB"));
            //markword 占用12，ageOffset为第一个变量所以偏移量12
            log.info("ageOffset field offset {}", ageOffset);
            //16
            log.info("nodeBOffset field offset {}", nodeBOffset);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private static Unsafe reflectGetUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
