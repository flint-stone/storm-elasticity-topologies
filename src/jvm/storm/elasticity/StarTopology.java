package storm.elasticity;

import storm.elasticity.bolt.TestBolt;
import storm.elasticity.spout.TestSpout;
import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.BoltDeclarer;
import backtype.storm.topology.TopologyBuilder;

public class StarTopology {
	public static void main(String[] args) throws Exception {
		int numSpout = 5;
		int numBolt = 5;
		int paralellism = 2;

		TopologyBuilder builder = new TopologyBuilder();

		BoltDeclarer center = builder.setBolt("center", new TestBolt(),
				paralellism);

		for (int i = 0; i < numSpout; i++) {
			builder.setSpout("spout_" + i, new TestSpout(), paralellism);
			center.shuffleGrouping("spout_" + i);
		}

		for (int i = 0; i < numBolt; i++) {
			builder.setBolt("bolt_" + i, new TestBolt(), paralellism)
					.shuffleGrouping("center");
		}
		Config conf = new Config();
		conf.setDebug(true);

		conf.setNumWorkers(3);

		StormSubmitter.submitTopologyWithProgressBar(args[0], conf,
				builder.createTopology());

	}
}
