package futurethread.testtimeout;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author gaoguangjin
 */
@Slf4j
public class TimeoutFutureTest {


    public static void main(String[] args) {
        ExecutorService excutors = Executors.newSingleThreadExecutor();

        Future<?> future = excutors.submit(new MyTimeOutThread());
        try {
            future.get(3000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }


    private static class MyTimeOutThread extends Thread {
        @Override
        public void run() {
            for (;;) {
                try {
                    //System.out.println("11");
                    log.info("sleep");
                } catch (Exception e) {
                    log.error("e", e);
                    break;
                }
            }
        }
    }
}
