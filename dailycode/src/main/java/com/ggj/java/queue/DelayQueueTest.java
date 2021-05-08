package com.ggj.java.queue;

import java.util.concurrent.DelayQueue;

/**
 * @author gaoguangjin
 */
public class DelayQueueTest {

    public static void main(String[] args) {
        //定义延迟队列
        DelayQueue<DelayedTask> delayQueue = new DelayQueue<DelayedTask>();
        delayQueue.add(new DelayedTask(1000L,"延迟1000ms"));
        delayQueue.add(new DelayedTask(2000L,"延迟2000ms"));
        delayQueue.add(new DelayedTask(2000L,"延迟2000ms"));
        delayQueue.add(new DelayedTask(7000L,"延迟7000ms"));
        while (delayQueue.size()!=0){
            DelayedTask task=delayQueue.poll();
            if(task==null){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            System.out.println(String.format("task:%s",task.getTaskName()));
        }
    }
}
