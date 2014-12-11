package storm.elasticity;

import storm.elasticity.bolt.AggregationBolt;
import storm.elasticity.bolt.FilterBolt;
import storm.elasticity.bolt.TestBolt;
import storm.elasticity.bolt.TransformBolt;
import storm.elasticity.spout.RandomLogSpout;
import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;

public class PageLoadTopology {
	public static void main(String[] args) throws Exception {
		//int numBolt = 3;
		int paralellism = 4;

		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("spout_head", new RandomLogSpout(), paralellism).setNumTasks(8);

		
		builder.setBolt("bolt_transform", new TransformBolt(), paralellism).setNumTasks(8).shuffleGrouping("spout_head");
		builder.setBolt("bolt_filter", new FilterBolt(), paralellism).setNumTasks(8).shuffleGrouping("bolt_transform");
		builder.setBolt("bolt_join", new TestBolt(), paralellism).setNumTasks(8).shuffleGrouping("bolt_filter");
		builder.setBolt("bolt_filter_2", new FilterBolt(), paralellism).setNumTasks(8).shuffleGrouping("bolt_join");
		builder.setBolt("bolt_aggregate", new AggregationBolt(), paralellism).setNumTasks(8).shuffleGrouping("bolt_filter_2");
		builder.setBolt("bolt_output_sink", new TestBolt(),paralellism).setNumTasks(8).shuffleGrouping("bolt_aggregate");

		Config conf = new Config();
		conf.setDebug(true);

		conf.setNumAckers(0);

		conf.setNumWorkers(28);

		StormSubmitter.submitTopologyWithProgressBar(args[0], conf,
				builder.createTopology());

	}

}
