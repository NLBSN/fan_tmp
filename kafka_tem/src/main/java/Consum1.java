import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;
import org.junit.Test;
import redis.clients.jedis.*;

import java.io.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;

/**
 * @author fan
 * @description 官方kafka消费测试类
 */
public class Consum1 {
    private final static Logger LOGGER = Logger.getLogger(Consum1.class);
    private static JedisPool redisPool = null;

    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        props.put("bootstrap.servers", "10.40.17.99:9092,10.40.17.100:9092");
        // props.put("bootstrap.servers", "10.14.83.148:9092");
        props.put("group.id", "fan");
        props.put("enable.auto.commit", "true");
        props.put("auto.offset.reset", "latest");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("gld-link"));

        // BufferedWriter bufferedWriter = bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("d:\\data\\test.txt")), "utf-8"));
        int i = 0;
        Jedis resource = redisPool.getResource();
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                String key = record.key();
                String value = record.value();
                System.out.println(i++ + "---" + "offset = " + record.offset() + ", key = " + key.substring(0, key.length() - 2) + ", value = " + value);
                resource.hset(key.substring(0, key.length() - 2), "00", value);
                // if (key.substring(0, 1).equals("0")&& key.substring(23, 24).equals("1") && key.substring(2, 3).equals("6")) {
                // bufferedWriter.write(value);
                // bufferedWriter.write("\n");
                // bufferedWriter.flush();
                // }
            }
        }
    }

    @Test
    public void guoLv() {
        Jedis jedis = redisPool.getResource();
        Set<String> keys = jedis.keys("20190122");
        System.out.println(keys);
    }

    static {
        Properties props = new Properties();
        InputStream in = null;
        try {
            // in = Consum1.class.getClassLoader().getResourceAsStream("risk.properties");
            in = Consum1.class.getResourceAsStream("risk.properties");
            // in = new FileInputStream(new File("").getAbsoluteFile() + "/" + "kafka_tem/src/main/resources/risk.properties");
            // in = new FileInputStream(new File("").getAbsoluteFile() + "/" + "risk.properties");
            // in = new ReaderInputStream(new FileReader(new File("").getAbsoluteFile() + "/" + "src/main/resources/redis.properties"), "utf8");
            //创建jedis池配置实例
            JedisPoolConfig config = new JedisPoolConfig();
            props.load(in);
            //设置池配置项值
            config.setMaxTotal(Integer.valueOf(props.getProperty("jedis.pool.maxActive")));
            config.setMaxIdle(Integer.valueOf(props.getProperty("jedis.pool.maxIdle")));
            config.setMaxWaitMillis(Long.valueOf(props.getProperty("jedis.pool.maxWait")));
            config.setTestOnBorrow(Boolean.valueOf(props.getProperty("jedis.pool.testOnBorrow")));
            config.setTestOnReturn(Boolean.valueOf(props.getProperty("jedis.pool.testOnReturn")));

            //根据配置实例化jedis池
            redisPool = new JedisPool(config, props.getProperty("redis.ip"), Integer.valueOf(props.getProperty("redis.port")));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
