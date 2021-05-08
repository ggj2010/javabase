package controlthread;

/**
 * @author gaoguangjin
 */
public class Notify {

    static Object o1 = new Object();
    static Object o2 = new Object();

    public static void main(String[] args) {


        notWakeUp();
    }

    private static void notWakeUp() {

        new Thread() {
            @Override
            public void run() {
                synchronized (o1) {
                    try {
                        System.out.println("wait");
                        o1.wait();
                        System.out.println("唤醒了");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
               /* synchronized (o1) {
                    o1.notify();
                     System.out.println("notify");
                }*/

                synchronized (o2) {
                    o2.notify();
                    System.out.println("notify");
                }
            }
        }.start();

    }
}
