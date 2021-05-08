package com.ggj.java.queue;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author gaoguangjin
 */
public class DelayedTask implements Delayed {
    private Long delayTime;

    public String getTaskName() {
        return taskName;
    }

    private String taskName;

    public DelayedTask(Long delayTime, String taskName) {
        this.taskName = taskName;
        this.delayTime = System.currentTimeMillis() + delayTime;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return delayTime - System.currentTimeMillis();
    }

    //1 交换顺序 -1 保持顺序 0 什么都不做
    @Override
    public int compareTo(Delayed o) {
        DelayedTask task = (DelayedTask) o;
        if (this.delayTime > task.delayTime) {
            return 1;
        } else if (this.delayTime < task.delayTime) {
            return -1;
        }
        return 0;
    }
}
