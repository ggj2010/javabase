package concurrentthred;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * author:gaoguangjin
 * Description:俗名回环栅栏，它允许一组线程互相等待，功能和countdowanLatch类似，不过 CyclicBarrier可以重用
 * Email:335424093@qq.com
 * Date 2016/1/8 14:16
 */
@Slf4j
public class CyclicBarrierUtil {

    public static void main(String[] args) {
//        new CyclicBarrierUtil().useCyclicBarrier();
        new CyclicBarrierUtil().userCyclicBarrierThread();
    }

    /**
     * 所有线程执行抵达栅栏的时候，去执行mainThread(),然后再执行栅栏后面的
     * 业务场景。
     * 我们需要统计全国的业务数据。其中各省的数据库是独立的，也就是说按省分库。并且统计的数据量很大，统计过程也比较慢。
     * 为了提高性能，快速计算。我们采取并发的方式，多个线程同时计算各省数据，最后再汇总统计
     * 也就是说开n个线程去计算，都计算完毕之后 再让主线程去汇总各个值
     */
    public void userCyclicBarrierThread(){
        int cyclicSize=10;
        CyclicBarrier cyclicBarrier=new CyclicBarrier(cyclicSize,startMainThread());
        for (int i = 0; i < cyclicSize; i++) {
            startThread("子线程"+i,cyclicBarrier);
        }

        for (int i = 0; i <100 ; i++) {
            new Thread(){
                @Override
                public void run() {
                    log.info("============");
                }
            }.start();
        }

    }

    private Thread startMainThread() {
       return  new Thread(){
            public void run() {
                log.info("所有子线程都已经执行咯，====》执行主线程咯！");
            }
        };
    }

    /**
     * 到达栅栏点时候 会一起进行执行结束
     */
    public void useCyclicBarrier(){
        int cyclicSize=10;
        CyclicBarrier cyclicBarrier=new CyclicBarrier(cyclicSize);
        for (int i = 0; i < cyclicSize; i++) {
            startThread("子线程"+i,cyclicBarrier);
        }

        new Thread() {
            public void run() {
                log.info(Thread.currentThread().getName()+"主线程啊");
            }
        }.start();

    }
    private void startThread(final String name, final CyclicBarrier cyclicBarrier){
        new Thread(name){
            public void run() {
                log.info(Thread.currentThread().getName()+"开始执行=======>");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                log.info(Thread.currentThread().getName()+"到达栅栏点，同时执行结束！");
            }
        }.start();
    }

}
