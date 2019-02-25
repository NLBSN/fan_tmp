package redisWis;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.Properties;
import java.util.Set;

/**
 * @Description: traffic_fan
 * @author: fan
 * @Date: Created in 2019/1/15 9:02
 * @Modified By:
 */
public class Consumer_wis {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "10.14.83.148:9092");
        props.put("group.id", "risk");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("twn"));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                String key = record.key();
                String value = record.value();
                System.out.println("offset = " + record.offset() + ", key = " + key + ", value = " + value);
                // jedis.set(key, value);
                // MyJedisPool.returnJedisOjbect(jedis);
            }
        }
    }

    @Test
    public void test(){
        MyJedisPool.getWriteJedisObject().set("11111","1111111111111111" );
        Jedis readJedisObject = MyJedisPool.getReadJedisObject();
        readJedisObject.select(3);
        Set<String> riskIndexRevision = readJedisObject.smembers("riskIndexRevision");
        System.out.println(riskIndexRevision);
    }
}
