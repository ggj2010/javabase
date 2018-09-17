
package concurrent;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author:gaoguangjin
 * @date:2018/9/3
 */
public class AtomicBooleanTest {
    private static AtomicBoolean flag = new AtomicBoolean();
    private static volatile boolean  notSafeflag ;

    public static void main(String[] args) {
        testNotSafe();
//        test();
    }

    private static void test() {
        for (int i = 0; i < 10; i++) {
            getThread().start();
        }
    }
    private static void testNotSafe() {
        for (int i = 0; i < 10; i++) {
            getNotSafeThread().start();
        }
    }

    public static Thread getThread() {
        return new Thread() {
            @Override
            public void run() {
                if (flag.compareAndSet(false, true)) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("do job");
                }else{
                    System.out.println("not do job");
                }
            }
        };
    }
    public static Thread getNotSafeThread() {
        return new Thread() {
            @Override
            public void run() {
                if (!notSafeflag) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    notSafeflag=true;
                    System.out.println("do notSafeflag job");
                }else{
                    System.out.println("not do job");
                }
            }
        };
    }
}
