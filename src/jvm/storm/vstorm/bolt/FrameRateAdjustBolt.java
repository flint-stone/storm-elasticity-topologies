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
import java.io.File;
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
import storm.vstorm.spout.VServerInfo;

public class FrameRateAdjustBolt extends BaseRichBolt {

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
    	//_collector.emit(new Values(time, info.getId(), info.getIp(), bandwidth, buffer_size, framerate, qoe, "good"))
    	//declarer.declare(new Fields("time", "id", "ip", "bw", "bs", "fr", "result"));
    	Long time =tuple.getLongByField("time");
    	String id = tuple.getStringByField("id");
    	String ip = tuple.getStringByField("ip");
    	int bandwidth = tuple.getIntegerByField("bw");
    	int buffer_size = tuple.getIntegerByField("bs");
    	int framerate = tuple.getIntegerByField("fr");
    	String result = tuple.getStringByField("result");
    	if(result.equals("bad")){//adjust framerate
    		Double qoe = (double)bandwidth/5000.0+(double)buffer_size/5000.0+(double)30/30.0;
    		if(qoe>=3.0){
    			_collector.emit(new Values(time, id, ip, bandwidth, buffer_size, 30, qoe, "good"));
    		}
    		else{
    			_collector.emit(new Values(time, id, ip, bandwidth, buffer_size, framerate, qoe, "bad"));
    		}
    	}
    	
    	//collector.emit(new Values(obj, count, actualWindowLengthInSeconds));
        //_collector.ack(tuple);
      
    }
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("time", "id", "ip", "bw", "bs", "fr", "qoe", "result"));
	}
}
