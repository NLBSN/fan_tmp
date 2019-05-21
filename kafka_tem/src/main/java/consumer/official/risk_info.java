package consumer.official;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author fan
 * @description 官方kafka消费测试类
 */
public class risk_info {
    private final static Logger LOGGER = Logger.getLogger(risk_info.class);

    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        props.put("bootstrap.servers", "10.40.17.99:9092,10.40.17.100:9092");
        // props.put("bootstrap.servers", "10.254.5.202:9092");
        props.put("group.id", "risk");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("twn_test"));
        int i = 0;
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                String value = record.value();
                // if (value.split(",", -1).length > 12)
                System.out.print(i++ + "---" + "offset = " + record.offset() + ", key = " + record.key() + ", value = " + value);
            }
        }
    }
}
