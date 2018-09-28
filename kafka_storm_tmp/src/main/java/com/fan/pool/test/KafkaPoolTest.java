package com.fan.pool.test;

import com.fan.pool.KafkaPool;
import com.fan.pool.KafkaProducerApp;
import org.junit.Test;

import java.util.Properties;

/**
 * @Description: kafka生产者连接池功能的测试
 * @author: fan
 * @Date: Created in 2018/9/20 14:26
 * @Modified By: fan，注意，只有生产者有连接池
 */
public class KafkaPoolTest {

    @Test
    public void producerTest() throws Exception {
        Properties props = new Properties();
        props.put("bootstrap.servers", "10.16.48.44:9092,10.16.48.44:9093");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaPool<String, String> pool = new KafkaPool<String, String>(props);
        for (int i = 0; i < 1000; i++) {
            KafkaProducerApp<String, String> producer = pool.borrowProducer();
            producer.send("vdata", "test" + i);
            pool.returnProducer(producer);
            System.out.println("-------------" + i);
        }
    }
}