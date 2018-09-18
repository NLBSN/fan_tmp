package producer.method_1;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description 生产者，消费者都有
 * @url 参考文章  https://blog.csdn.net/charry_a/article/details/79621324
 */
public class Main {

    /**
     * @throws InterruptedException
     * @description 生产者测试类入口
     */
    @Test
    public void pro_test() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        for (int i = 1; i <= 10; i++) {
            Thread.sleep(1000);
            executor.submit(new HandlerProducer(":" + i));
        }

    }

    /**
     * @description 消费者测试类入口
     */
    @Test
    public void cum_test() {
        Kafka_Consumer kafka_Consumer = new Kafka_Consumer();
        try {
            kafka_Consumer.execute();
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            kafka_Consumer.shutdown();
        }
    }
}
