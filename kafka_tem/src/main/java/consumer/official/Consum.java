package consumer.official;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;
import org.junit.Test;


import java.io.*;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author fan
 * @description 官方kafka消费测试类
 */
public class Consum {
    private final static Logger LOGGER = Logger.getLogger(Consum.class);

    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        // props.put("bootstrap.servers", "10.14.83.148:9092");
        props.put("bootstrap.servers", "10.40.17.99:9092,10.40.17.100:9092");
        props.put("group.id", "risk321");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "latest");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("gld-link"));
        int i = 0;//023510114201901301530201233200
        BufferedWriter bufferedWriter = bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("d:\\data\\test.txt")), "utf-8"));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                String key = record.key();
                String value = record.value();
                // if (key.substring(0, 1).equals("1")) {
                //     if (key.substring(2, 3).equals("0")) {
                System.out.println(i++ + "---" + "offset = " + record.offset() + ", key = " + key + ", value = " + value);
                if (key.substring(0, 1).equals("1") && key.substring(1, 2).equals("1") && key.substring(2, 3).contains("0")) {
                    bufferedWriter.write(value);
                    bufferedWriter.write("\n");
                    bufferedWriter.flush();
                }
                //     }
                // }

            }
        }
    }


}
