package storm.elasticity.bolt;

import java.util.Map;

import storm.elasticity.ExclamationTopology.ExclamationBolt;
import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.testing.TestWordSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class AggregationBolt extends BaseRichBolt{
	OutputCollector _collector;

    @Override
    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
      _collector = collector;
    }
    @Override
    public void execute(Tuple tuple) {
    	String word = tuple.getString(0);
    	Integer length=word.length();
    	int x=0;
    	for(int i=0;i<10*length;i++){
    		x++;
    	}
    	byte b=0x10;
    	//word=word+Byte.toString(b)+Byte.toString(b)+Byte.toString(b)+Byte.toString(b);
    	word=word+"word";
    	//word+=Byte.toString(b);
    	//word+=Byte.toString(b);
    	//word+=Byte.toString(b);
      _collector.emit(tuple, new Values(word));
      //_collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
      declarer.declare(new Fields("word"));
    }
}

