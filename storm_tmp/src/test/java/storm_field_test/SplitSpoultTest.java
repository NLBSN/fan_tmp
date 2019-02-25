package storm_field_test;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

import java.util.Map;

public class SplitSpoultTest extends BaseRichSpout {
    int count = 0;
    private SpoutOutputCollector collector;
    //多数是除以3余数为1,27是整除，便于测试 
    // String[] array2 =  {"1","4","7","10","13","16","19","22","25","27","28","16"};
    // String[] array2 = {"我的我","我的你你","我的","我的","你的","我的","我的","我的","他的","我的","你的","他的"};
    // String[] array2 = {"我的","我的","我的","我的","你的","我的","我的","我的","他的","我的","你的","他的"};
    String[] array2 = {"wd","wd","wd","wd","df","df","df","wd","td","wd","df","tp"};
    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.collector = spoutOutputCollector;
    }

    @Override
    public void nextTuple() {


        if (count >= array2.length) {
            Utils.sleep(10000);
            count = 0;
        }
        collector.emit(new Values(array2[count]), array2[count]);
        count++;


    }


    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("field"));
    }
}