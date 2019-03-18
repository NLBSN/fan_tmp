package wordcount;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Random;

public class SentenceSpout extends BaseRichSpout {
    private static final Logger logger= LoggerFactory.getLogger(SentenceSpout.class);
    private SpoutOutputCollector collector;
    //制造数据
    private static final String[] SENTENCES={
      "hadoop oozie storm hive",
      "hadoop spark sqoop hbase",
      "error flume yarn mapreduce"
    };
    //初始化collector
    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.collector=spoutOutputCollector;
    }
    //Key的设置
    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("sentence"));
    }
    //Tuple的组装
    @Override
    public void nextTuple() {
        String sentence=SENTENCES[new Random().nextInt(SENTENCES.length)];
        if(sentence.contains("error")){
            logger.error("记录有问题"+sentence);
        }else{
            System.out.println("sentence----------"+sentence);
            this.collector.emit(new Values(sentence));
        }
        try{
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public SentenceSpout() {
        super();
    }

    @Override
    public void close() {

    }

    @Override
    public void activate() {
        super.activate();
    }

    @Override
    public void deactivate() {
        super.deactivate();
    }

    @Override
    public void ack(Object msgId) {
        super.ack(msgId);
    }

    @Override
    public void fail(Object msgId) {
        super.fail(msgId);
    }

}