package fuckingJavaConcurrency;

/**
 * 对称锁死锁   死锁例子
 * @author Jerry Lee (oldratlee at gmail dot com)
 */
public class SymmetricLockDeadlockDemo {
    static final Object lock1 = new Object();
    static final Object lock2 = new Object();

    public static void main(String[] args) throws Exception {
        Thread thread1 = new Thread(new ConcurrencyCheckTask1());
        thread1.start();
        Thread thread2 = new Thread(new ConcurrencyCheckTask2());
        thread2.start();


        while (true){
            System.out.println(thread1.getState());
            System.out.println(thread2.getState());
        }
    }

    private static class ConcurrencyCheckTask1 implements Runnable {
        @Override
        public void run() {
            System.out.println("ConcurrencyCheckTask1 started!");
            while (true) {
                synchronized (lock1) {
                    synchronized (lock2) {
                        System.out.println("Hello1");
                    }
                }
            }
        }
    }

    private static class ConcurrencyCheckTask2 implements Runnable {
        @Override
        public void run() {
            System.out.println("ConcurrencyCheckTask2 started!");
            while (true) {
                synchronized (lock2) {
                    synchronized (lock1) {
                        System.out.println("Hello2");
                    }
                }
            }
        }
    }
}
