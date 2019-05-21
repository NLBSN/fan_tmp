package producer.official;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.*;
import java.util.Properties;

/**
 * @author fan
 * @description kafka官方模板测试类
 */
public class Main {

    /**
     * kafka官方标准测试类
     */
    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.put("bootstrap.servers", "10.40.17.99:9092");
        // props.put("bootstrap.servers", "10.14.83.149:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("D:\\wis_work\\data\\MSP3_PMSC_TWO_ME_L88_CRN_201904041420_0000-0000.TXT")), "utf-8"));
        Producer<String, String> producer = new KafkaProducer<>(props);
        String tmpLine = new String();
        // StringBuffer stringBuffer = new StringBuffer();
        int i = 0;
        while ((tmpLine = bufferedReader.readLine()) != null) {
            // String[] split = tmpLine.split(",", 2);
            System.out.println(i++ + "---" + tmpLine);
            producer.send(new ProducerRecord<String, String>("twp-mpg-point1", "201904041420", tmpLine));

        }
        // for (int i = 0; i < 100; i++) {
        //     producer.send(new ProducerRecord<String, String>("twn", "11111", "主基文是王八蛋"));
        // }
        producer.close();
    }

}
