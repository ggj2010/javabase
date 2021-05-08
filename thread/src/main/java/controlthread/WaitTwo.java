
package controlthread;

import lombok.extern.slf4j.Slf4j;

/**
 * wait(0)是可以自己苏醒的，当Thread执行结束时候，可以自动唤醒
 * <p>
 * * <p> This implementation uses a loop of {@code this.wait} calls
 * conditioned on {@code this.isAlive}. As a thread terminates the
 * {@code this.notifyAll} method is invoked. It is recommended that
 * applications not use {@code wait}, {@code notify}, or
 * {@code notifyAll} on {@code Thread} instances.
 *
 * 所以不推荐在Thread实例里面用wait()和notifyall
 * @author:gaoguangjin
 * @date:2018/4/4
 */
@Slf4j
public class WaitTwo extends Thread {
    @Override
    public void run() {
        try {
            log.info("thread 执行");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void waitZero() {
        try {
            //获取WaitTwo实例锁，block主线程
            log.info(Thread.currentThread().getName()+"线程 wait");
            wait();
            log.info("thread 执行完毕，自动苏醒");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WaitTwo waitTwo = new WaitTwo();
        //注释 start就可以看到效果
        waitTwo.start();//执行完后会主动调用 waitTwo.notify()
        //主线程执行
        waitTwo.waitZero();
    }
}
