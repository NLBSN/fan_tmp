package storm_field_test;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintBolt extends BaseBasicBolt {
    private static final Logger LOGGER = LoggerFactory.getLogger(PrintBolt.class);

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        LOGGER.info("tuple0->" + input.getString(0) + "  " + Thread.currentThread().getName());
        System.out.println("tuple0->" + input.getString(0) + "  " + Thread.currentThread().getName());

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
