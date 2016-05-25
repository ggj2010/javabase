package concurrentthred;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/**
 * author:gaoguangjin
 * Description:实现类似计数器的功能。比如有一个任务A，它要等待其他4个任务执行完毕之后才能执行 和jion（）功能类似
 * Email:335424093@qq.com
 * Date 2016/1/8 13:31
 */
@Slf4j
public class CountDownLatchUtil {

    public static void main(String[] args) {
        new CountDownLatchUtil().useCountDownLatch();
    }

    /**
     * 让子线程都执行完之后再执行父类方法，可以实现线程计算线程执行时间的功能。
     */
    public void useCountDownLatch(){
        long beginTime=System.currentTimeMillis();
        //启动两个线程
        int threadSize=20;
        CountDownLatch countDownLatch=new CountDownLatch(threadSize);
        try {
            for (int i = 0; i <threadSize ; i++) {
                startThread("子线程"+i,countDownLatch);
            }
            countDownLatch.await();
            mainMethod(beginTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }

    private void mainMethod(long beginTime){
        log.info("执行父类方法");
        log.info("计数器执行时间："+(System.currentTimeMillis()-beginTime));
    }
    private void startThread(final String name, final CountDownLatch countDownLatch){
        new Thread(name){
            public void run() {
                log.info(Thread.currentThread().getName()+"执行=======>");
                countDownLatch.countDown();
                log.info(Thread.currentThread().getName()+"执行结束=======>");
            }
        }.start();

    }

}
