
package controlthread;

import lombok.extern.slf4j.Slf4j;

/**
 * author:gaoguangjin Description:jion 会让线程的执行按照一定顺序，jion的位置很重要，jion（）方法下面的代码总是会等这个线程执行完之后再执行
 * thread.join()应该是让当前线程block住，等thread执行完之后，再继续执行 join是利用wait的特性,当线程执行结束自动唤醒 Email:335424093@qq.com
 * Date 2016/1/6 16:21 https://www.cnblogs.com/xudilei/p/6867045.html
 */
@Slf4j
public class Join {

    /**
     * 让线程按照顺序执行，比如线程1执行完之后再执行线程2,3 join的本质就是 while (isAlive()) { wait(0); }
     */

    /**
     * *
     * <p>
     * This implementation uses a loop of {@code this.wait} calls conditioned on
     * {@code this.isAlive}. As a thread terminates the {@code this.notifyAll} method is invoked. It
     * is recommended that applications not use {@code wait}, {@code notify}, or {@code notifyAll}
     * on {@code Thread} instances.
     * 
     * @throws InterruptedException
     */
    public void userJion() throws InterruptedException {
        Thread thread1 = thread("线程1");
        Thread thread2 = thread("线程2");
        Thread thread3 = thread("线程3");

        /* join代码要写的位置是在它start后面 */
        thread1.start();
        /**
         * join的意思是使得放弃当前线程的执行，并返回对应的线程，例如下面代码的意思就是：
         * 程序在main线程中调用t1线程的join方法，则main线程放弃cpu控制权，并返回t1线程继续执行直到线程t1执行完毕 所以结果
         */
        // join会让主线程被堵塞，直到子线程执行完。
        thread1.join();
        thread2.start();
        thread2.join();

        thread3.start();
    }

    /**
     * 多执行几次，线程执行的顺序不一定是1,2,3，打印的结果可能2,1,3
     */
    public void notUserJion() {
        Thread thread1 = thread("线程1");
        Thread thread2 = thread("线程2");
        Thread thread3 = thread("线程3");

        thread1.start();
        thread2.start();
        thread3.start();
    }

    private static Thread thread(final String name) {
        Thread thread = new Thread(name) {
            public void run() {
                log.info("线程名称==> ：" + Thread.currentThread().getName());
            }
        };
        return thread;
    }

}
