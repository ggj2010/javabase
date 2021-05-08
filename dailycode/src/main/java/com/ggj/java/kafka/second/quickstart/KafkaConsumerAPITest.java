package com.ggj.java.kafka.second.quickstart;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/2/6 15:48
 */
public class KafkaConsumerAPITest {

    /**
     * 如果启动了多个 Consumer线程，Kafka也能够通过zookeeper实现多个Consumer间的调度，保证同一组下的Consumer不会重复消费消息。注 意，
     *
     * @param args
     */
    public static void main(String[] args) {
        KafkaConsumer<Integer, String> consumer = getKafkaConsumer();

//       listTopics(consumer);

//       listPartion(consumer);
        subscribe(consumer);
    }

    private static void listPartion(KafkaConsumer<Integer, String> consumer) {
        List<PartitionInfo> partitionsFor = consumer.partitionsFor(KafKaProducerAPITest.TOPIC_API);
        for (PartitionInfo partitionInfo : partitionsFor) {
            System.out.println("" + partitionInfo);
        }
    }

    /**
     * 订阅topic
     *
     * @param consumer
     */
    private static void subscribe(KafkaConsumer<Integer, String> consumer)  {
        consumer.subscribe(Arrays.asList(KafKaProducerAPITest.TOPIC_API));
        while (true) {
            ConsumerRecords<Integer, String> records = consumer.poll(100);
            if(records.count()>0) {
                for (ConsumerRecord<Integer, String> record : records) {
                    System.out.println(String.format("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value()));
                }
                try {
                    //模拟重复消费
                    //Thread.sleep(1000*60*100);
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * list topic test
     *
     * @param consumer
     */
    private static void listTopics(KafkaConsumer<Integer, String> consumer) {
        Map<String, List<PartitionInfo>> partitionInfo = consumer.listTopics();
        partitionInfo.forEach((String key, List<PartitionInfo> list) -> {
            System.out.println("key = [" + key + "]");

            list.forEach((PartitionInfo partition) -> {
                // partition.topic();
                System.out.println("PartitionInfo = [" + partition + "]");
            });
        });
    }

    /**
     * bootstrap.servers/key.deserializer/value.deserializer：和Producer端的含义一样，不再赘述
     * fetch.min.bytes：每次最小拉取的消息大小（byte）。Consumer会等待消息积累到一定尺寸后进行批量拉取。默认为1，代表有一条就拉一条
     * max.partition.fetch.bytes：每次从单个分区中拉取的消息最大尺寸（byte），默认为1M
     * group.id：Consumer的group id，同一个group下的多个Consumer不会拉取到重复的消息，不同group下的Consumer则会保证拉取到每一条消息。注意，同一个group下的consumer数量不能超过分区数。
     * enable.auto.commit：是否自动提交已拉取消息的offset。提交offset即视为该消息已经成功被消费，该组下的Consumer无法再拉取到该消息（除非手动修改offset）。默认为true
     * auto.commit.interval.ms：自动提交offset的间隔毫秒数，默认5000。
     *
     * @return
     */
    public static KafkaConsumer<Integer, String> getKafkaConsumer() {
        //配置咯
        Properties props = new Properties();
        //服务器开启两个server 一个端口号是9092 一个是9093
        //      props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "123.56.118.135:9092,123.56.118.135:9093");
        //bootstrap.servers
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        //group.id
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "gg3");
        //从何处开始消费,latest 表示消费最新消息,earliest 表示从头开始消费,none表示抛出异常,默认latest
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        //enable.auto.commit 是否自动提交已拉取消息的offset。提交offset即视为该消息已经成功被消费，该组下的Consumer无法再拉取到该消息（除非手动修改offset）。默认为true
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        //auto.commit.interval.ms 自动提交offset的间隔毫秒数，默认5000。
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "5000");

        //max.partition.fetch.bytes 每次从单个分区中拉取的消息最大尺寸（byte），默认为1M
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, 1024);
        //session.timeout.ms
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        //heartbeat.interval.ms 心跳时间
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, "3000");
        //key.deserialize
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerDeserializer");
        //value.deserializer
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        return new KafkaConsumer<>(props);
    }


}
