package concurrent;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 自旋锁
 * https://mp.weixin.qq.com/s/F8YDVB407vPr4Pp6KCk0vA
 *
 * @author gaoguangjin
 */
public class SpinLock {
    static AtomicReference<Thread> cas = new AtomicReference<>();
    private static int count;

    public static void main(String[] args) throws InterruptedException {
        while (true){
            System.out.println("args = [" + args + "]");
        }
      //  notReetrantLockTest();
//        reetrantLockTest();
    }

    private static void reetrantLockTest() throws InterruptedException {
        reetrantLock();
        reetrantLock();
    }

    /**
     * 自旋锁不可重入
     *
     * @throws InterruptedException
     */
    private static void notReetrantLockTest() throws InterruptedException {
        //获得锁
        notReetrantLock();
        // 不能再次获取锁
        notReetrantLock();
    }

    /**
     * notReetrantLock（)方法利用的CAS，
     * 当第一个线程A获取锁的时候，能够成功获取到，不会进入while循环，
     * 如果此时线程A没有释放锁，另一个线程B又来获取锁，此时由于不满足CAS，
     * 所以就会进入while循环，不断判断是否满足CAS，直到A线程调用unlock方法释放了该锁。
     *
     * @throws InterruptedException
     */
    public static void notReetrantLock() throws InterruptedException {
        Thread current = Thread.currentThread();
        // 利用CAS
        while (!cas.compareAndSet(null, current)) {
            System.out.println(" wait getlock");
            Thread.sleep(1000);
        }
    }


    public static void reetrantLock() throws InterruptedException {
        Thread current = Thread.currentThread();
        if (current == cas.get()) { // 如果当前线程已经获取到了锁，线程数增加一，然后返回
            count++;
            return;
        }
        while (!cas.compareAndSet(null, current)) {
            System.out.println("wait getlock");
            Thread.sleep(1000);
        }
    }


    public void unlock() {
        Thread current = Thread.currentThread();
        cas.compareAndSet(current, null);
    }

    public void unlock2() {
        Thread current = Thread.currentThread();
        if (current == cas.get()) {
            if (count > 0) {// 如果大于0，表示当前线程多次获取了该锁，释放锁通过count减一来模拟
                count--;
            } else {// 如果count==0，可以将锁释放，这样就能保证获取锁的次数与释放锁的次数是一致的了。
                cas.compareAndSet(current, null);
            }
        }
    }
}
