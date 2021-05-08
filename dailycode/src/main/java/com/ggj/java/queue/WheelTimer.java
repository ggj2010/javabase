package com.ggj.java.queue;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;

import java.util.concurrent.TimeUnit;

/**
 * @author gaoguangjin
 */
public class WheelTimer {
    public static void main(String[] args) {
        HashedWheelTimer timer = new HashedWheelTimer();

        TimerTask timerTask= new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                System.out.println("123");
            }
        };

        timer.newTimeout(timerTask,2, TimeUnit.SECONDS);
        timer.start();
    }
}
