package consumer.official;

import com.sun.webkit.event.WCChangeEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;
import org.junit.Test;
import redis.clients.jedis.JedisCluster;


import java.io.*;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author fan
 * @description 官方kafka消费测试类
 */
public class Consum {
    private final static Logger LOGGER = Logger.getLogger(Consum.class);
    // private static JedisCluster redisClusterObject = null;

    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        // props.put("bootstrap.servers", "10.16.48.231:9092");
        props.put("bootstrap.servers", "10.40.17.99:9092");
        props.put("group.id", "wis_tw");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "latest");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("two_bean"));
        int i = 0;//023510114201901301530201233200
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("d:\\wis_work\\data\\bean02.txt")), "utf-8"));
        // redisClusterObject = MyJedisClusterPool.getRedisClusterObject();
        wc:
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                String key = record.key();
                String value = record.value();
                if (value.contains("over") || value.contains("jie")) {
                    i++;
                    bufferedWriter.write("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    bufferedWriter.write("\n");
                    bufferedWriter.flush();
                    System.out.println("---------------------------------结束了一个时次的---------------------------------------");
                    // redisClusterObject.hset(key.substring(0, key.length() - 2), "00", "");
                } else /*if (key.substring(0, 1).equals("1") && key.substring(1, 2).equals("1"))*/ {
                    if (i > 5) break wc;
                    System.out.println("---" + "offset = " + record.offset() + ", key = " + key + ", value = " + value);
                    // redisClusterObject.hset(key.substring(0, key.length() - 2), "00", value);
                    bufferedWriter.write(key + "=====" + value);
                    bufferedWriter.write("\n");
                    bufferedWriter.flush();
                }
            }
        }
    }
}
