package storm.elasticity;

import storm.elasticity.bolt.TestBolt;
import storm.elasticity.spout.TestSpout;
import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.BoltDeclarer;
import backtype.storm.topology.TopologyBuilder;

public class NewTopology {
	public static void main(String[] args) throws Exception {
		int numSpout = 2;
		int numBolt = 2;
		int paralellism = 4;

		TopologyBuilder builder = new TopologyBuilder();

		BoltDeclarer center = builder.setBolt("center", new TestBolt(),
				paralellism).setNumTasks(8);

		for (int i = 0; i < numSpout; i++) {
			builder.setSpout("spout_" + i, new TestSpout(), paralellism).setNumTasks(8);
			center.shuffleGrouping("spout_" + i);
		}

		for (int i = 0; i < numBolt; i++) {
			builder.setBolt("bolt_output_" + i, new TestBolt(), paralellism).setNumTasks(8)
					.shuffleGrouping("center");
		}
		
		
		Config conf = new Config();
		conf.setDebug(true);
		
		conf.setNumAckers(0);

		conf.setNumWorkers(24);
		
	

		StormSubmitter.submitTopologyWithProgressBar(args[0], conf,
				builder.createTopology());

	}
}
