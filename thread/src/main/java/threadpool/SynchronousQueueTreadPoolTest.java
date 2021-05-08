package threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 使用SynchronousQueue 当队列看线程当情况
 * @author gaoguangjin
 */
@Slf4j
public class SynchronousQueueTreadPoolTest {
    public static void main(String[] args) {


        //有以下两个线程池,目前已有10个请求正在处理中，请问当第11个请求进来时aPool和bPool分别会如何处理？
        ThreadPoolExecutor aPool = new ThreadPoolExecutor(10,20,5, TimeUnit.MINUTES,new SynchronousQueue<Runnable>());
        ThreadPoolExecutor bPool = new ThreadPoolExecutor(10,20,5,TimeUnit.MINUTES,new ArrayBlockingQueue<Runnable>(10));

        //bpool 肯定会放到队列里面
        //apool会启动新的线程来处理

        //SynchronousQueue是一个很特殊的Queue，当往这个Queue放东西时，必须有另外一个线程在从这个Queue里拿，
        // 如没有，则直接失败，在上面的场景中，当第11个请求进来时，往这个Queue中放就将失败，
        // 而这个时候运行的线程数又小于maxPoolSize，因此将启动新线程进行处理

        SynchronousQueue<Runnable> synchronousQueue = new SynchronousQueue<Runnable>();
        try {
            boolean flag = synchronousQueue.offer(new Runnable() {
                @Override
                public void run() {

                }
            });
            log.info("{}",flag);
        } catch (Exception e) {
            log.error("ee",e);
        }

        testResult(aPool);
        log.info("--------------无敌分割线-----------");
        testResult(bPool);
    }

    private static void testResult(ThreadPoolExecutor pool) {

        for (int i = 0; i <10 ; i++) {
            pool.submit(new Thread(()->{
                try {
                    Thread.sleep(100000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }));
        }

        log.info("activeThreadCount={}",pool.getActiveCount());

        pool.submit(new Thread(()->{
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));

        log.info("activeThreadCount={}",pool.getActiveCount());

    }
}
