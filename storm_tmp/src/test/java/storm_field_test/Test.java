package storm_field_test;

import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {
    private static final Logger LOGGER = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("spout", new SplitSpoultTest(), 1);
        //指定3个，便于测试
        builder.setBolt("bolt", new PrintBolt(), 5).fieldsGrouping("spout", new Fields("field"));

        Config conf = new Config();
        conf.setDebug(false);
        LOGGER.info("----------------------");
        // LocalCluster cluster = new LocalCluster();
        StormSubmitter.submitTopology("toplogyTest", conf, builder.createTopology());
        // Utils.sleep(60000);
        // cluster.shutdown();

    }
}