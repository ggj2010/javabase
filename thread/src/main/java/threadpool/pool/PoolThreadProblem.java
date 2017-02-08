package threadpool.pool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;


/**
 * 线程池提交任务时候 最好以队列形式提交 这样可以控制线程池里面任务数量
 *
 * @author:gaoguangjin
 * @date 2017/1/11 14:17
 */
@Slf4j
public class PoolThreadProblem {
    static int threadNumber = 100;
    static BlockingQueue<Runnable> blockQueue = new ArrayBlockingQueue<Runnable>(threadNumber);
    static AtomicInteger atomicInteger= new AtomicInteger();

    public static void main(String[] args) {
        //虽然设置了固定线程池大小，但是可以一直往线程池里面提交东西，如果一直不停地往pool中加入task，
        // 但是task的处理速度远跟不上加入的速度，但造成pool积压严重。其实如果只一个空的task，也不会有多大问题
        //如果task中又包含了很多对象，如字符串，map，list等等，在生成task时也会把这些东西一起生成，即使加了pool中，又不会马上执行到，只能是空站着耗内存罢
//        badUse();
        rightUse();
    }

    private static void rightUse() {
        ExecutorService pool = Executors.newFixedThreadPool(threadNumber);
        List<Future> listFuture = new ArrayList<>();
        new Thread(() ->
                addThread()
        ).start();

        while (true) {
            Runnable thread = null;
            try {
                if (listFuture.size() < threadNumber) {
                    //线程池里面的线程执行结束了 才继续添加
                    thread = blockQueue.take();
                    Future<?> future = pool.submit(thread);
                    log.info("add to pool " + atomicInteger.incrementAndGet());
                    listFuture.add(future);
                } else {
                    for (int i = 0; i < threadNumber; i++) {
                        Future future = listFuture.get(i);
                        future.get();
                    }
                    listFuture = new ArrayList<>();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void addThread() {
        for (int i = 0; i < 10000 * 1000; i++) {
            try {
                blockQueue.put(thread());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void badUse() {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10000 * 1000; i++) {
            pool.execute(thread());
            log.info("add to pool " + atomicInteger.incrementAndGet());
        }
    }

    private static Runnable thread() {
        task2();
        return new Thread(() ->
                task()
        );
    }

    private static void task2() {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 100000; i++) {
            map.put(Math.random() + "", Math.random() + "");
        }
        log.info("task2");
    }

    private static void task() {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 100000; i++) {
            map.put(Math.random() + "", Math.random() + "");
        }
        try {
            Thread.sleep(1000 * 20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("执行task"+atomicInteger.decrementAndGet());
    }

}
