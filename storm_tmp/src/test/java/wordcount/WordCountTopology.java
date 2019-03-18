package wordcount;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

public class WordCountTopology {
    private static final String SENTENCE_SPOUT = "sentenceSpout";
    private static final String SPLIT_BOLT = "splitBolt";
    private static final String COUNT_BOLT = "countBolt";
    private static final String PRINT_BOLT = "printBolt";

    public static void main(String[] args) {
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout(SENTENCE_SPOUT, new SentenceSpout());
        topologyBuilder.setBolt(SPLIT_BOLT, new SplitBolt()).shuffleGrouping(SENTENCE_SPOUT);
        topologyBuilder.setBolt(COUNT_BOLT, new CountBolt()).fieldsGrouping(SPLIT_BOLT, new Fields("word"));
        topologyBuilder.setBolt(PRINT_BOLT, new PrintBolt()).globalGrouping(COUNT_BOLT);
        Config config = new Config();
        if (args == null || args.length == 0) {
            LocalCluster localCluster = new LocalCluster();
            localCluster.submitTopology("wordcount", config, topologyBuilder.createTopology());
        } else {
            config.setNumWorkers(1);
            try {
                StormSubmitter.submitTopology(args[0], config, topologyBuilder.createTopology());
            } catch (AlreadyAliveException e) {
                e.printStackTrace();
            } catch (InvalidTopologyException e) {
                e.printStackTrace();
            } catch (AuthorizationException e) {
                e.printStackTrace();
            }
        }

    }
}