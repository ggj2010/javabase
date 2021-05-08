package synchronizedthread;

import java.util.concurrent.TimeUnit;

/**
 * @author gaoguangjin
 */
public class VolatileByteCode {

    private static volatile int a;

    public static void main(String[] args) throws InterruptedException {
       int d=2;
        a = 2;
        int c=2;

        TimeUnit.MILLISECONDS.sleep(100);
    }
}
