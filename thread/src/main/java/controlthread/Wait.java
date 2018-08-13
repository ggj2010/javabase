package controlthread;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * author:gaoguangjin
 * Description:wait（）会释放线程所，而sleep()和yield（）是不会释放线程所
 * Email:335424093@qq.com
 * Date 2016/1/6 16:45
 */
@Slf4j
public class Wait {

    //wait()和notify()必须在synchronized的代码块中使用
    // 因为只有在获取当前对象的锁时才能进行这两个操作 否则会报异常 而await()和signal()一般与Lock()配合使用

    /**
     * 开启五个线程，如果用sleep（）会让某一个线程执行完毕之后再执行其他的线程
     * 而用wait（）会释放线程锁，让某个线程在没执行完毕的时候，也可以执行
     * @param args
     */
    public static void main(String[] args) {
        Wait  wait= new Wait();

//      wait.releaseLock(wait,true);
//      wait.notReleaseLock(wait,false);
        //Thead.wait()，如果Thread结束了，是可以自动唤醒的。这个会在join中被使用。
        wait0(wait);

    }

    private static void wait0(Wait wait) {
        Thread thead = new Thread() {
            @Override
            public void run() {

            }
        };
        Thread theadTwo = new Thread() {
            @Override
            public void run() {
                wait.waitZero(thead);
            }
        };
        theadTwo.start();
        thead.start();

    }

    private void waitZero(Thread thead) {
        try {
            synchronized (thead) {
                thead.wait(0);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 不释放线程锁
     * @param wait
     * @param isRelease
     */
    public void notReleaseLock(Wait wait,Boolean isRelease){
        wait.isReleaseThreadLock(isRelease);
    }

    /**
     * 释放线程锁，当休眠5000之后，再notify（）所有线程
     * @param wait
     * @param isRelease
     */
    public void releaseLock(Wait wait,Boolean isRelease){
        wait.isReleaseThreadLock(isRelease);
    }

    /**
     * true 就执行await释放线程锁
     * @param isRelease
     */
    public void isReleaseThreadLock(Boolean isRelease) {
        Wait wait=new Wait();
        Thread thread1 = getThread("线程1",isRelease);
        Thread thread2 = getThread("线程2",isRelease);
        thread1.start();
        thread2.start();
        try {
            if(isRelease) {
                System.in.read();
                notifyAllThread(isRelease);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * wait释放线程锁
     * @param name 名称
     * @param isRelease 是否是否线程锁
     */
    public synchronized void threadWait(String name,Boolean isRelease) {
        try {
            for (int i = 0; i <=4; i++) {
                log.info("线程名称：==>"+name);
                  if(i==3) {
                      if (isRelease) {
                          this.wait();
                      } else {
                          Thread.sleep(1000);
                      }
                  }
                if(i==4){
                    log.info("线程名称：==>"+name+"==>线程执行完毕！！！");
                }
            }


        } catch (InterruptedException e) {
            log.error(""+e.getLocalizedMessage());
        }
    }

    public synchronized void notifyAllThread(Boolean isRelease){
        if(isRelease) {
                log.info("notifyAll通知所有=====");
                this.notifyAll();
        }
    }

    /**
     * 获取thread
     * @param name
     * @param isRelease
     * @return
     */
    private Thread getThread(final String name,final Boolean isRelease) {
         Thread thread = new Thread(name) {
            public void run() {
                    threadWait(Thread.currentThread().getName(),isRelease);
            }
        };
        return thread;
    }
}
