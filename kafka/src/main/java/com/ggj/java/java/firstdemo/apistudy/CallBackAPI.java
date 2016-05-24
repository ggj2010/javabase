package com.ggj.java.java.firstdemo.apistudy;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/2/15 10:03
 */
public class CallBackAPI implements Callback {
    private int key;
    private String message;


    public CallBackAPI( String message) {
        this.message = message;
    }

    @Override
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        System.out.println("生产者发送message(" + message + ") sent to partition分区(" + metadata.partition() + "), " + "消息offset(" + metadata.offset() + ")");
    }
}
