package queue;


import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.SynchronousQueue;

import lombok.extern.slf4j.Slf4j;

/**
 * 同步队列，是一个没有数据缓冲的BlockingQueue，生产者线程对其的插入操作put必须等待消费者的移除操作take，反过来也一样。
 * 只能一次性放一个对象，newCachedThreadPool用到了
 * LockSupport.park()实现阻塞的
 * @author:gaoguangjin
 * @date 2017/1/9 10:28
 */
@Slf4j
public class SynchronousQueueReview {
    static SynchronousQueue<String> sq = new SynchronousQueue<String>();
//    static LinkedBlockingDeque<String> sq = new LinkedBlockingDeque<String>();

    public static void main(String[] args) {
        demo1();
    }

    private static void demo1() {
        new Thread() {

            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    try {
                        sq.put(i+"");
                        log.info("put {}",i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String value = sq.take();
                        log.info(value);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
