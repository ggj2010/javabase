package controlthread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * author:gaoguangjin
 * Description:condition 是和lock搭配使用的，其效果类似于wait（）和notify()
 * Email:335424093@qq.com
 * Date 2016/1/7 14:47
 */
@Slf4j
public class Conditions {
    public AtomicInteger atomicInteger = new AtomicInteger();
    public Lock lock = new ReentrantLock();
    public Condition condition = lock.newCondition();
    public volatile boolean  flag ;


    public static void main(String[] args) {
        new Conditions().displayContion();
    }

    /**
     * 实现一个主线程先打印5到1 1子线程打印5 循环过程
     */
    public void displayContion() {
        Thread threadFather = new Thread("主线程") {
            public void run() {
                displayNumber(Thread.currentThread().getName());
            }
        };
        threadFather.start();

        Thread threadChild = new Thread("子线程") {
            public void run() {
                displayNumber(Thread.currentThread().getName());
            }
            ;
        };
        threadChild.start();
    }


    public void displayNumber(String name) {
        lock.lock();
        try {
            //必须保证主线程先执行 一开始是flag是false 后来变成true if条件就没用了
            if (flag || "主线程".equals(name)) {
                while (atomicInteger.get() < 100) {
                    for (int i = 0; i <= 4; i++) {
                        log.info("线程名称：" + name + ":" + atomicInteger.get());
                        atomicInteger.incrementAndGet();
                        if (i == 4) {
                            condition.signal();
                            if(!flag)
                                flag=true;
                            condition.await();
                        }
                    }
                }
                condition.signal();
            } else {
                //不是父线程进入就直接等待
                log.info("线程名称：" + name + ":线程等待-------------" );
                condition.await();
            }
        } catch (Exception e) {
        } finally {
            lock.unlock();
        }
    }
}