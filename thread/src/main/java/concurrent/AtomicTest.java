package concurrent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/2/23 12:40
 */
public class AtomicTest {
    int notsafeSum;
    private int value;

//程序中有变量的读取、写入或判断操作
    public int getNext() {
        return value++;
    }

    public static void main(String[] args) {
        AtomicTest atomicTest = new AtomicTest();

        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.set(10);

        atomicInteger.getAndIncrement();



        AtomicLong atomicLong = new AtomicLong();
        AtomicBoolean atomicBoolean = new AtomicBoolean();

     //   atomicTest.notsafeOne();
        atomicTest.notsafeTwo();
    }

    /**
     * thrad:Thread-0  value0
     thrad:Thread-1  value0
     */
    private void notsafeTwo() {
        for (int l = 0; l < 2; l++) {
            new Thread(() -> {
                System.out.println("thrad:"+Thread.currentThread().getName()+"  value"+getNext());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

    }

    private void notsafeOne() {
        //可以看到输出结果不一致 多运行几次就看出来了，我们想要的是100 输出的确是98，99
        for (int l = 0; l < 10; l++) {
            new Thread(() -> {
                for (int i = 0; i < 10; i++) {
                    notsafeSum = notsafeSum + 1;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i == 9)
                        System.out.println("sum=" + notsafeSum);
                }
            }).start();
        }


        //System.out.println("sum="+notsafeSum);
    }
}
