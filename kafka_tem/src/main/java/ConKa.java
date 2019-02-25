import com.sun.org.apache.xml.internal.security.Init;
import consumer.method_1.ConsumerGroup;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.KafkaFuture;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @Description: fan_tmp
 * @author: fan
 * @Date: Created in 2019/1/25 14:55
 * @Modified By:
 */
public class ConKa {


    public void init() {
        Properties properties = new Properties();
        properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "10.40.17.100:9092");
        AdminClient adminClient = AdminClient.create(properties);
        NewTopic newTopic = new NewTopic("twp_bean", 1, (short) 1);
        Collection<NewTopic> collections = new ArrayList<NewTopic>();
        collections.add(newTopic);
        CreateTopicsResult topics = adminClient.createTopics(collections);
        // ListTopicsOptions options = new ListTopicsOptions();
        // ListTopicsResult listTopicsResult = newAdminClient.listTopics();
        adminClient.close();
    }

    public static void main(String[] args) throws Exception {
        // new ConKa().init();
        new ConKa().getTopic();
    }

    private void getTopic() throws Exception {
        Properties properties = new Properties();
        properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "10.40.17.99:9092,10.40.17.100:9092");
        AdminClient adminClient = AdminClient.create(properties);
        ListTopicsResult listTopicsResult = adminClient.listTopics();
        Set<String> strings = listTopicsResult.names().get();
        System.out.println("发现 " + strings.size() + "个kafka topic ：" + strings.toString());
        adminClient.close();
    }
}
