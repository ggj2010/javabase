package productandconsume.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author gaoguangjin
 */
public class AwaitSignal {
    private static ReentrantLock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();
    private static volatile boolean flag = false;


    private static ReentrantLock lock1 = new ReentrantLock();
    private static Condition condition1 = lock1.newCondition();
    private static volatile boolean flag1 = false;

    public static void main(String[] args) {
        Thread waiter = new Thread(new waiter());
        waiter.start();

        Thread signaler = new Thread(new signaler());
        signaler.start();

        Thread waiter1 = new Thread(new waiter1());
        waiter1.start();

        Thread signaler1 = new Thread(new signaler1());
        signaler1.start();
    }

    static class waiter implements Runnable {

        @Override
        public void run() {
            lock.lock();
            try {
                while (!flag) {
                    System.out.println(Thread.currentThread().getName() + "当前条件不满足等待");
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Thread.currentThread().getName() + "接收到通知条件满足");
            } finally {
                lock.unlock();
            }
        }
    }

    static class signaler implements Runnable {

        @Override
        public void run() {
            lock.lock();
            try {
                flag = true;
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }


    static class waiter1 implements Runnable {

        @Override
        public void run() {
            lock1.lock();
            try {
                while (!flag1) {
                    System.out.println(Thread.currentThread().getName() + "当前条件不满足等待"+1);
                    try {
                        condition1.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Thread.currentThread().getName() + "接收到通知条件满足"+1);
            } finally {
                lock1.unlock();
            }
        }
    }

    static class signaler1 implements Runnable {

        @Override
        public void run() {
            lock1.lock();
            try {
                flag1 = true;
                condition1.signalAll();
            } finally {
                lock1.unlock();
            }
        }
    }
}
