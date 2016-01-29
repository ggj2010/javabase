package com.ggj.java.firstdemo.myself;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * author:gaoguangjin
 * Description:producer回调的
 * Email:335424093@qq.com
 * Date 2016/1/29 18:58
 */
public class ProducerCallBack implements Callback {
    private long startTime;
    private int key;
    private String message;

    public ProducerCallBack(long startTime, int key, String message) {
        this.startTime = startTime;
        this.key = key;
        this.message = message;
    }

    /**
     * 发送完回调的数据
     * @param metadata
     * @param exception
     */
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        if (metadata != null) {
            System.out.println("生产者发送message(" + key + ", " + message + ") sent to partition(" + metadata.partition() + "), " +
                            "offset(" + metadata.offset() + ") in " + elapsedTime + " ms");
        } else {
            exception.printStackTrace();
        }
    }

}
