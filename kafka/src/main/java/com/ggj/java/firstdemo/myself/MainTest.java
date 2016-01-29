package com.ggj.java.firstdemo.myself;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/1/29 19:03
 */
public class MainTest {

    public static void main(String[] args) {

        //第一步启动服务端，一次只能启动一个~
        new KafkaProducerTest().sendMessage();


        //第二步启动消费者
       // new KafkaConsumerTest().start();
    }
}
