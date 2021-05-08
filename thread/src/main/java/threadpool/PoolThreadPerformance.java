package threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * author:gaoguangjin
 * Description: 利用线程池和不利用线程池执行线程性能比较，证明线程的创建史需要开销的。
 * Email:335424093@qq.com
 * Date 2016/1/12 16:26
 */
@Slf4j
public class PoolThreadPerformance {

    public static void main(String[] args) {
        PoolThreadPerformance p = new PoolThreadPerformance();
//        p.userPool();
//        p.notPool();
    }

    /**
     * 4000线程 threadpool.PoolThreadPerformance 62 displayTime- 执行时间：545
     */
    public void notPool() {
        displayTime(false);
    }
    /**
     *4000线程：threadpool.PoolThreadPerformance 62 displayTime- 执行时间：1716
     */
    public void userPool() {
        displayTime(true);
    }

    private void displayTime(boolean userPool) {
        long beingTime = System.currentTimeMillis();
        if (userPool) {
            ExecutorService pool = Executors.newSingleThreadExecutor();
            for (int i = 0; i < 4000; i++) {
                pool.execute(getThread(i + ""));
            }
            pool.shutdown();
            pool.shutdownNow();
            try {
                pool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            CountDownLatch countDownLacth=new CountDownLatch(4000);
            for (int i = 0; i <4000; i++) {
                getCountDownLacthThread(i + "",countDownLacth).start();
            }
            try {
                countDownLacth.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.currentTimeMillis();

        log.info("执行时间："+(endTime-beingTime));
    }


    public Thread getThread(final String name) {
        return new Thread(name) {
            public void run() {
                log.info("线程名称：" + Thread.currentThread().getName());
            }
        };
    }

    public Thread getCountDownLacthThread(final String name,final  CountDownLatch countDownLacth) {
        return new Thread(name) {
            public void run() {
                log.info("线程名称：" + Thread.currentThread().getName());
                    countDownLacth.countDown();
            }
        };
    }

}
