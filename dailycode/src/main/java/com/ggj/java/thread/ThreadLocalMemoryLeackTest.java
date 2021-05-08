package com.ggj.java.thread;


import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 *  ThreadLocalMap里面Entry key就是threadLocal对象本身，value就是放进threadLocal里面的值
 *     假设ThreadLocalMap的Entry key threadLocal是强引用，当线程创建一个threadLocalA，将threadLocalA置为null，
 *     ThreadLocalMap里面的Entry key 还是会持有threadLocalA的强引用,这时候threadLocalA是始终无法被回收的
 * 因为ThreadLocalMap 的key 是弱引用，当GC的时候key值Threadlocal会被回收，导致key未null，这样value值就一直无法被释放，导致内存泄漏。
 * 但是Threadlocal设计的时候有兜底方案，就是在get和set时候回主动将key值为null的值，去掉他的value值。
 *
 *
 * ThreadLocalMap的getEntry函数的流程大概为：

 首先从ThreadLocal的直接索引位置(通过ThreadLocal.threadLocalHashCode & (table.length-1)运算得到)获取Entry e，如果e不为null并且key相同则返回e；
 如果e为null或者key不一致则向下一个位置查询，如果下一个位置的key和当前需要查询的key相等，则返回对应的Entry。否则，如果key值为null，则擦除该位置的Entry，并继续向下一个位置查询。在这个过程中遇到的key为null的Entry都会被擦除，那么Entry内的value也就没有强引用链，自然会被回收。仔细研究代码可以发现，set操作也有类似的思想，将key为null的这些Entry都删除，防止内存泄露。
 　　但是光这样还是不够的，上面的设计思路依赖一个前提条件：要调用ThreadLocalMap的getEntry函数或者set函数。这当然是不可能任何情况都成立的，所以很多情况下需要使用者手动调用ThreadLocal的remove函数，手动删除不再需要的ThreadLocal，防止内存泄露。所以JDK建议将ThreadLocal变量定义成private static的，这样的话ThreadLocal的生命周期就更长，由于一直存在ThreadLocal的强引用，所以ThreadLocal也就不会被回收，也就能保证任何时候都能根据ThreadLocal的弱引用访问到Entry的value值，然后remove它，防止内存泄露。

 *
 * @author:gaoguangjin
 * @date:2018/1/25
 */
public class ThreadLocalMemoryLeackTest {
    private static ThreadLocal<Byte[]> threadLocal = new ThreadLocal<>();


    public static void main(String[] args) {
        ThreadLocalMemoryLeackTest threadLocalMemoryLeackTest = new ThreadLocalMemoryLeackTest();
        threadLocalMemoryLeackTest.test();
    }

    private void test() {
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ExecutorService pool = Executors.newFixedThreadPool(50);
        for (int i = 0; i <= 50; i++) {
            pool.execute(getThread());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Runnable getThread() {
        return new Thread() {
            @Override
            public void run() {
                threadLocal.set(new Byte[1024 * 1024 * 1]);
                //注释就可以看到内存泄漏
                //threadLocal.remove();
                System.out.println("2222");
            }
        };
    }
}
