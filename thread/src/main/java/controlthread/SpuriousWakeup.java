
package controlthread;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 防止虚假唤醒最好加循环
 * 如果等待线程在没有通知被调用的情况下唤醒
 * @author:gaoguangjin
 * @date:2018/4/18
 */
@Slf4j
public class SpuriousWakeup {

    private static volatile boolean flag = true;

    private  synchronized void waitZero() {
       // while (flag) {
            try {
                log.info("wait 开始");
                wait();
                log.info("wait 结束");
            } catch (Exception e) {
                log.error("error", e);
            }
        //}
    }

    public static void main(String[] args) throws IOException {
        new Thread() {
            @Override
            public void run() {
                new SpuriousWakeup().waitZero();
            }
        }.start();
    }
}
