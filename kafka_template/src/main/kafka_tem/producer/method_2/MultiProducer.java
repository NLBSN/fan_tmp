package producer.method_2;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * @Title MultiProducer.java
 * @Description 多线程生产者的测试代码
 * @Author YangYunhe
 * @Date 2018-06-25 14:30:58
 */
public class MultiProducer {

    private static final int THREADS_NUMS = 10;

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(THREADS_NUMS);

        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.42.89:9092,192.168.42.89:9093,192.168.42.89:9094");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        ProducerRecord<String, String> record;

        try {
            for (int i = 0; i < 100; i++) {
                record = new ProducerRecord<>("dev3-yangyunhe-topic001", "hello" + i);
                executor.submit(new KafkaProducerThread(producer, record));
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("exception occurs when sending message: " + e);
        } finally {
            producer.close();
            executor.shutdown();
        }
    }
}