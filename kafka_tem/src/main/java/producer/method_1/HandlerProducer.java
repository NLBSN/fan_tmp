package producer.method_1;

/**
 * @description 多线程实例
 * @author fan
 */
public class HandlerProducer implements Runnable {

    private String message;

    /**
     * @description 构造函数
     * @param message   消息
     */
    public HandlerProducer(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        KafkaProducerSingleton kafkaProducerSingleton = KafkaProducerSingleton.getInstance();
        kafkaProducerSingleton.init("test_find", 3);
        System.out.println("当前线程:" + Thread.currentThread().getName()
                + ",获取的kafka实例:" + kafkaProducerSingleton);
        kafkaProducerSingleton.sendKafkaMessage("发送消息" + message);
    }

}
