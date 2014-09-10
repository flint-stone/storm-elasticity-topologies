package storm.elasticity;

import storm.elasticity.ExclamationTopology.ExclamationBolt;
import storm.elasticity.bolt.TestBolt;
import storm.elasticity.spout.TestSpout;
import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.testing.TestWordSpout;
import backtype.storm.topology.BoltDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

public class StarTopology {
	public static void main(String[] args) throws Exception {
		int numSpout = 5;
		int numBolt = 5;
		int paralellism = 5;

		TopologyBuilder builder = new TopologyBuilder();

		BoltDeclarer center = builder.setBolt("center", new TestBolt(),
				paralellism);

		for (int i = 0; i < numSpout; i++) {
			builder.setSpout("spout" + i, new TestSpout(), paralellism);
			center.shuffleGrouping("spout" + 1);
		}

		for (int i = 0; i < numBolt; i++) {
			builder.setBolt("bolt" + i, new TestBolt(), paralellism)
					.shuffleGrouping("center");
		}
		Config conf = new Config();
		conf.setDebug(true);

		conf.setNumWorkers(3);

		StormSubmitter.submitTopologyWithProgressBar(args[0], conf,
				builder.createTopology());

	}
}
