package synchronizedthread;

/**
 * @author gaoguangjin
 */
public class DeadLockTest {
    private static Object objectA = new Object();
    private static Object objectB = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            testLockA();
        }).start();
        new Thread(() -> {
            testLockB();
        }).start();

    }

    public static void testLockA() {
        synchronized (objectA) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (objectB) {

            }

        }

    }


    public static void testLockB() {
        synchronized (objectB) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (objectA) {
            }
        }
    }
}
