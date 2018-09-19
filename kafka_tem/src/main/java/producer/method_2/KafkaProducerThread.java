package producer.method_2;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * @Title KafkaProducerThread.java
 * @Description 多线程生产者的线程类实现
 * @Author YangYunhe
 * @url https://www.jianshu.com/p/6e6c8ea297ca
 * @Date 2018-06-25 13:54:38
 */
public class KafkaProducerThread implements Runnable {

    private KafkaProducer<String, String> producer;
    private ProducerRecord<String, String> record;

    public KafkaProducerThread(KafkaProducer<String, String> producer, ProducerRecord<String, String> record) {
        this.producer = producer;
        this.record = record;
    }

    @Override
    public void run() {
        producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception != null) {
                    System.out.println("exception occurs when sending message: " + exception);
                }
                if (metadata != null) {
                    StringBuilder result = new StringBuilder();
                    result.append("message[" + record.value() + "] has been sent successfully! ")
                            .append("send to partition ").append(metadata.partition())
                            .append(", offset = ").append(metadata.offset());
                    System.out.println(result.toString());
                }
            }
        });
    }
}