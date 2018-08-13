package controlthread;

import lombok.extern.slf4j.Slf4j;

/**
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
