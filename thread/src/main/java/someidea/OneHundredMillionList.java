package someidea;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 1亿的list计算大小
 *
 * @author:gaoguangjin
 * @date 2016/5/16 16:53
 */
public class OneHundredMillionList {

    static  Lock lock= new ReentrantLock();
    static int threadSum=0;

    public static void main(String[] args) {
      //  normal();
        thread();
    }

    private static void thread() {
        List<Integer> list=getList();
        long beginTime = System.currentTimeMillis();
        CountDownLatch countDownLatch=new CountDownLatch(100);
        CyclicBarrier cyclicBarrier=new CyclicBarrier(100);
        for (int i = 0; i <100 ; i++) {
            new Thread(()->{
                try {
                    int sum=0;
                    for (int j = 0; j < 1000000; j++) {
                         sum = sum + list.get(j);
                    }
                    cyclicBarrier.await();
                    add(sum,countDownLatch);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("耗费时间：" + (end - beginTime) + "; sum="+threadSum );
    }


    public static  void add(int sum, CountDownLatch countDownLatch){
        lock.lock();
        try{
            threadSum=sum+threadSum;
            countDownLatch.countDown();
        }finally {
            lock.unlock();
        }
    }

    public static List<Integer> getList(){
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < 1000000; i++) {
            list.add(1);
        }
        return  list;
    }

    private static void normal() {
        List<Integer> list=getList();
        long beginTime = System.currentTimeMillis();
        int sum = 0;
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 1000000; j++) {
                sum = sum + list.get(j);
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("耗费时间：" + (end - beginTime) + "; sum=" + sum);
    }

}
