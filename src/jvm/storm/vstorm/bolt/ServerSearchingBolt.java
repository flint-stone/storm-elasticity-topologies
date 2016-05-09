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

public class ServerSearchingBolt extends BaseRichBolt {

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
    	//client
    	Long time =tuple.getLongByField("time");
    	String id = tuple.getStringByField("id");
    	String ip = tuple.getStringByField("ip");
    	int bandwidth = tuple.getIntegerByField("bw");
    	int buffer_size = tuple.getIntegerByField("bs");
    	int framerate = tuple.getIntegerByField("fr");
    	String result = tuple.getStringByField("result");
    	if(result.equals("bad")){
    		//server
       	 File file = new File("server_list");
       	 File currentServerFile = new File(id+"_c_servertaget");
       	    try (BufferedReader br3 = new BufferedReader(new FileReader(currentServerFile))) {
       	        String cur_server_line = br3.readLine();
       	        String[] parts2 = cur_server_line.split(":");
       	        String c_sid = parts2[0];
   				String c_sip = parts2[1];
   				String c_port = parts2[2];
       	    	String line;
       	        BufferedReader br = new BufferedReader(new FileReader(file));
       			while ((line = br.readLine()) != null) {
       				   // process the line.
       					String[] parts = line.split(":");
       					String sid = parts[0];
       					String sip = parts[1];
       					String sport = parts[2];
       					//read server resource
       					if(!sid.equals(c_sid)){
       						String resource_filename = sid+"_s_resource.txt";
           					File fr = new File(resource_filename);
           					BufferedReader br2 = new BufferedReader(new FileReader(fr));
           					String line2 = br2.readLine();
           					int sbandwidth = Integer.valueOf(line2);
           					Double qoe = (double)sbandwidth/5000.0+(double)buffer_size/5000.0+(double)30/30.0;
           					if(qoe>=3.0){
           						//use this new machine
           						_collector.emit(new Values(time, id, framerate, "change", sid, sip, sport));
           					}
           					else{
           						//dont do anything, add the latency log line
           						_collector.emit(new Values(time, id, 30.0, "nochange", c_sid, c_sip, c_port));
           					}
           	    			br2.close();
       					}				
       			}
       			
       			br.close();
       			br3.close();
       	    } catch (FileNotFoundException e1) {
       			// TODO Auto-generated catch block
       			e1.printStackTrace();
       		} catch (IOException e1) {
       			// TODO Auto-generated catch block
       			e1.printStackTrace();
       		}
    	}   	
    	//collector.emit(new Values(obj, count, actualWindowLengthInSeconds));
        //_collector.ack(tuple);
      
    }
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("time", "id", "fr", "result","sid","sip","sport"));
	}
}
