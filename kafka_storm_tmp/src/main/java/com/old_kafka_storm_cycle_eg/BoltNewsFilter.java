package com.old_kafka_storm_cycle_eg;

import com.alibaba.fastjson.JSONObject;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.IBasicBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;

public class BoltNewsFilter implements IBasicBolt {

    private static final long serialVersionUID = 9131709924588239662L;

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream("stream-keywords-filter", new Fields("message"));
    }

    public Map<String, Object> getComponentConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        // TODO Auto-generated method stub

    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        int type = (Integer) input.getValueByField("TYPE");
        JSONObject object = (JSONObject) input.getValueByField("JSONOBJECT");
        int iscomment = object.getIntValue("ISCOMMENT");
        //内容
        String content = object.getString("CONTENT");

        collector.emit("stream-keywords-filter", new Values(type + "," + object.getLong("ID") + "," + iscomment + "," + content));
    }

    public void cleanup() {
        // TODO Auto-generated method stub

    }

}
