package com.ggj.java.kafka.second.quickstart;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * author:gaoguangjin
 * Description:可以参考 http://kelgon.iteye.com/blog/2287985
 * Email:335424093@qq.com
 * Date 2016/2/15 9:46
 */
@Slf4j
public class KafKaProducerAPITest {
    public final static String TOPIC_API = "topic_pig";
    public final static String PRODUCER_CLIENT_ID = "client_id_1";


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        KafkaProducer<Integer, String> producer = getProducer();
        //构造producerRecord
        ProducerRecord producerRecord=getProducerRecord();
        //普通send
        while (true) {
            Thread.sleep(500);
            //producer.send(producerRecord);
            //带callback的send
              producer.send(producerRecord, new Callback() {
                  @Override
                  public void onCompletion(RecordMetadata metadata, Exception exception) {
                      System.out.println(metadata.toString());
                  }
              });
              log.info("----");
        }

        //send发送时候加锁，一个一个发送
        //producer.send(producerRecord).get();

       // producer.close();
    }

    /**
     * ProducerRecord 提供多种构造方法  String topic, Integer partition, K key, V value  随意搭配
     *
     * @return
     */
    private static ProducerRecord getProducerRecord() {
        //ProducerRecord
        ProducerRecord producerRecord = new ProducerRecord(TOPIC_API, "ggj-2016-02-15");

//        ProducerRecord producerRecord2=new ProducerRecord(TOPIC_API,"key","value");
        //partition
//        ProducerRecord producerRecord3=new ProducerRecord(TOPIC_API,1,"key","value");
        return producerRecord;
    }


    /**
     * get kafkaProducer
     * Producer端的常用配置
     bootstrap.servers：Kafka集群连接串，可以由多个host:port组成
     acks：broker消息确认的模式，有三种：
     0：不进行消息接收确认，即Client端发送完成后不会等待Broker的确认
     1：由Leader确认，Leader接收到消息后会立即返回确认信息
     all：集群完整确认，Leader会等待所有in-sync的follower节点都确认收到消息后，再返回确认信息
     我们可以根据消息的重要程度，设置不同的确认模式。默认为1
     retries：发送失败时Producer端的重试次数，默认为0
     batch.size：当同时有大量消息要向同一个分区发送时，Producer端会将消息打包后进行批量发送。如果设置为0，则每条消息都独立发送。默认为16384字节
     linger.ms：发送消息前等待的毫秒数，与batch.size配合使用。在消息负载不高的情况下，配置linger.ms能够让Producer在发送消息前等待一定时间，以积累更多的消息打包发送，达到节省网络资源的目的。默认为0
     key.serializer/value.serializer：消息key/value的序列器Class，根据key和value的类型决定
     buffer.memory：消息缓冲池大小。尚未被发送的消息会保存在Producer的内存中，如果消息产生的速度大于消息发送的速度，那么缓冲池满后发送消息的请求会被阻塞。默认33554432字节（32MB）
     *
     *
     * @return
     */
    private static KafkaProducer<Integer, String> getProducer() {
        Properties properties = new Properties();
        //bootstrap.servers
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        //client.id
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, PRODUCER_CLIENT_ID);
        //batch.size 当同时有大量消息要向同一个分区发送时，Producer端会将消息打包后进行批量发送。如果设置为0，则每条消息都独立发送。默认为16384字节
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG,16384);
      //发送消息前等待的毫秒数，与batch.size配合使用。在消息负载不高的情况下，配置linger.ms能够让Producer在发送消息前等待一定时间，以积累更多的消息打包发送，达到节省网络资源的目的。默认为0
        properties.put(ProducerConfig.LINGER_MS_CONFIG,5000);
        //retries：发送失败时Producer端的重试次数，默认为0
        properties.put(ProducerConfig.RETRIES_CONFIG,0);
        //消息缓冲池大小。尚未被发送的消息会保存在Producer的内存中，如果消息产生的速度大于消息发送的速度，那么缓冲池满后发送消息的请求会被阻塞。默认33554432字节
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG,33554432);
        //key 和 value serializer的类
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer(properties);
    }


}
