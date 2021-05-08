package synchronizedthread;

/**
 * @author gaoguangjin
 */
public class SynchronizedAllMethod {
    public static void main(String[] args) {
        SynchronizedAllMethod synchronizedAllMethod = new SynchronizedAllMethod();
        new Thread(() -> {
            synchronizedAllMethod.test1();
        }).start();

        new Thread(() -> {
            synchronizedAllMethod.test2();
        }).start();

    }

    public synchronized void test1() {
        while (true) {
            System.out.println("test1");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            test3();
            System.out.println("test3  end");
        }
    }

    public synchronized void test2() {
        while (true) {
            System.out.println("test2");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            test3();
            System.out.println("test1  end");
        }

    }

    public synchronized void test3() {
        System.out.println("test3");

    }


}
