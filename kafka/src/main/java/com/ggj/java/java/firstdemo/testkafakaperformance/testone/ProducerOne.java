package com.ggj.java.java.firstdemo.testkafakaperformance.testone;

import com.ggj.java.java.firstdemo.otherpeople.Topic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.IOException;
import java.util.Properties;

/**
 * 发送10w数据
 * @author:gaoguangjin
 * @date 2016/6/17 10:40
 */
public class ProducerOne {
    //100w条数据
    static int testNumber=1000000;
    //partion
    static Integer PARTION=0;
    //1000000数据，耗时：11380毫秒
    public static void main(String[] args) throws Exception {
        long beginTime=System.currentTimeMillis();
        test(getProducer());
        long endTime=System.currentTimeMillis();
        System.out.println(testNumber+"数据，耗时："+(endTime-beginTime)+"毫秒");
        //等待生产者将所有消息都发送成功后退出程序 Allow the producer to complete the sending of the records before existing the program.
        Thread.sleep(1000);
        System.in.read();
    }
    private static void test(Producer<String, String> producer) {
        for (int i =testNumber; i >0 ; i--) {
            //partion 随机
//            ProducerRecord record = new ProducerRecord<String, String>(TopicOne.TOPIC, i%2, i+"", i+"");
            ProducerRecord record = new ProducerRecord<String, String>(TopicOne.TOPIC, 0, i+"", i+"");
            producer.send(record);
        }
    }

    /**
     * 构造KafkaProducer
     * @return
     */
    private static KafkaProducer<String, String> getProducer() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "123.56.118.135:9092");
        //key 和 value serializer的类
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer(properties);
    }

}

