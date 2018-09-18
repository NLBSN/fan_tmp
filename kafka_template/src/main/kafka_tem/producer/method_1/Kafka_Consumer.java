package producer.method_1;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public final class Kafka_Consumer {

    /**
     * kafka消费者不是线程安全的
     */
    private final KafkaConsumer<String, String> consumer;

    private ExecutorService executorService;

    public Kafka_Consumer() {
        Properties props = new Properties();
        props.put("bootstrap.servers",
                "ip,port");
        props.put("group.id", "group");
        // 关闭自动提交
        props.put("enable.auto.commit", "false");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList("test_find"));
    }

    public void execute() {
        executorService = Executors.newFixedThreadPool(3);
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(10);
            if (null != records) {
                executorService.submit(new ConsumerThread(records, consumer));
            }
        }
    }

    public void shutdown() {
        try {
            if (consumer != null) {
                consumer.close();
            }
            if (executorService != null) {
                executorService.shutdown();
            }
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                System.out.println("Timeout");
            }
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

}
