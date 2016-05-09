/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package storm.vstorm.bolt;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import storm.vstorm.spout.VClientInfo;

public class FinalCollectSink extends BaseRichBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	OutputCollector _collector;
	

    @Override
    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
      _collector = collector;
    }
    @Override
    public void execute(Tuple tuple) {
    	//VServerInfo info = (VServerInfo) tuple.getValueByField("info");
    	//int id = Integer.valueOf(info.getId());
    	//File file = new File("");
    	Long time = tuple.getLongByField("time");
    	int framerate =tuple.getIntegerByField("fr");
    	String result = tuple.getStringByField("result");
    	if(result.equals("good")){
    		//qoe satisfied, do nothing
    		//log latency
    		Long current = System.currentTimeMillis();
    		Long latency = current-time;
    		//write latency
    		//write framerate if changed
    		_collector.emit(new Values(time));
    	}  	
    	//collector.emit(new Values(obj, count, actualWindowLengthInSeconds));
        //_collector.ack(tuple);
      
    }
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("time"));
	}
}
