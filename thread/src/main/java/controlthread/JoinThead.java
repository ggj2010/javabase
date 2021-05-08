package controlthread;

import lombok.extern.slf4j.Slf4j;

/**
 * join的底层其实就用利用wait的特性，join同步方法获取到的主线程对象的内置锁，只有当该thread执行结束后会
 *  自动唤醒当前wait的主线程,从而实现当某个线程执行join的时候，会等待这个线程完全执行完后再执行别的线程。
 * join的原理
 * @author:gaoguangjin
 * @date:2018/4/3
 */
@Slf4j
public class JoinThead extends Thread{
    public static void main(String[] args) throws InterruptedException {
        JoinThead thead=new JoinThead();
        JoinThead thead2=new JoinThead();
        thead.start();
        //堵塞当前线程
        thead.myjoin();
        //
        System.out.println("结束");

        thead2.start();
        //堵塞当前线程
        thead2.myjoin();
        System.out.println("结束2");

    }

    @Override
    public void run() {
        for (int i = 0; i <10 ; i++) {
            try {
                Thread.sleep(10);
                log.info(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void myjoin() {
        while (isAlive()){
            try {
                wait(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
