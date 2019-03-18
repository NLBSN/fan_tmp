package producer.official;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;

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
        props.put("bootstrap.servers", "10.40.17.99:9092,10.40.17.100:9092");
        // props.put("bootstrap.servers", "10.14.83.148:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("D:\\业务\\交管天气风险管控平台\\样例数据\\预报twp\\MSP3_PMSC_TWP_ME_L88_CRN_201812020800_0000-7200.TXT")), "gbk"));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("D:\\业务\\交管天气风险管控平台\\样例数据\\实况two\\MSP3_PMSC_TWO_ME_L88_CRN_201901301530_0000-0000.TXT")), "utf-8"));
        Producer<String, String> producer = new KafkaProducer<>(props);
        String tmpLine = new String();
        // StringBuffer stringBuffer = new StringBuffer();
        int i = 0;
        while ((tmpLine = bufferedReader.readLine()) != null) {
            // String[] split = tmpLine.split(",", 2);
            System.out.println(i++ + "---" + tmpLine);
            producer.send(new ProducerRecord<String, String>("two_test", "201901220850", tmpLine));

        }
        // for (int i = 0; i < 100; i++) {
        //     producer.send(new ProducerRecord<String, String>("twn", "11111", "主基文是王八蛋"));
        // }
        producer.close();
    }

}
