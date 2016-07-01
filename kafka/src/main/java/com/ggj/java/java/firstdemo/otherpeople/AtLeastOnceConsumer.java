package com.ggj.java.java.firstdemo.otherpeople;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;


/**
 * 最少消费一次，消息绝不会丢，但可能会重复传输
 * 例中采用的是自动提交offset，Kafka client会启动一个线程定期将offset提交至broker。
 * 假设在自动提交的间隔内发生故障（比如整个JVM进程死掉），那么有一部分消息是会被重复消费的。
 * 要避免这一问题，可使用手动提交offset的方式。
 * 构造consumer时将enable.auto.commit设为false，并在代 码中用consumer.commitSync()来手动提交
 */
public class AtLeastOnceConsumer {

    public static void main(String[] str) throws InterruptedException {
        System.out.println("Starting AutoOffsetGuranteedAtLeastOnceConsumer ...");
        execute();
    }
    private static void execute() throws InterruptedException {
        KafkaConsumer<String, String> consumer = createConsumer();
        consumer.subscribe(Arrays.asList("normal-topic"));
        processRecords(consumer);
    }

    private static KafkaConsumer<String, String> createConsumer() {

        Properties props = new Properties();
        props.put("bootstrap.servers", "123.56.118.135:9092");
        String consumeGroup = "cg1";
        props.put("group.id", consumeGroup);

        //是否自动提交已拉取消息的offset。提交offset即视为该消息已经成功被消费，
        // 该组下的Consumer无法再拉取到该消息（除非手动修改offset）。默认为true
        props.put("enable.auto.commit", "true");

        // Make Auto commit interval to a big number so that auto commit does not happen,
        //自动提交offset的间隔毫秒数，默认5000。
        props.put("auto.commit.interval.ms", "999999999999");

        // 每次从单个分区中拉取的消息最大尺寸（byte），默认为1M
        props.put("max.partition.fetch.bytes", "35");

        props.put("heartbeat.interval.ms", "3000");
        props.put("session.timeout.ms", "6001");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return new KafkaConsumer<String, String>(props);
    }


    private static void processRecords(KafkaConsumer<String, String> consumer) throws InterruptedException {
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            long lastOffset = 0;
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("\n\roffset = %d, key = %s, value = %s", record.offset(), record.key(), record.value());
                lastOffset = record.offset();
            }
            System.out.println("lastOffset read: " + lastOffset);
            process();

            //如果我们注释下面这行，消费者消费消息的话不会提交offset  会重复消费信息
            consumer.commitSync();
        }
    }
    private static void process() throws InterruptedException {
        Thread.sleep(500);
    }
}
