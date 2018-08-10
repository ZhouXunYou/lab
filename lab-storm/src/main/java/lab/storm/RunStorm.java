package lab.storm;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.topology.TopologyBuilder;

import lab.storm.bolt.CacheBolt;
import lab.storm.bolt.Json2DatapackageBolt;
import lab.storm.bolt.KpiCalculateBolt;
import lab.storm.bolt.TimeSeriesBolt;

public class RunStorm {
	public static void main(String[] args) {
		TopologyBuilder topologyBuilder = new TopologyBuilder();
		KafkaSpoutConfig<String,String> kafkaSpoutConfig = 
				KafkaSpoutConfig
				.builder("10.1.1.11:9092,10.1.1.12:9092,10.1.1.13:9092", "DATAPACKAGE_QUEUE")
//				.setProp(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
				.setProp(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
//				.setProp(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"true")
				.build();
//		String topicNames;
		topologyBuilder.setSpout("kafka_spout", new KafkaSpout<>(kafkaSpoutConfig), 1);
		String json2DatapackageBolt = Json2DatapackageBolt.class.getSimpleName();
		String kpiCalculateBolt = KpiCalculateBolt.class.getSimpleName();
		String cacheBolt = CacheBolt.class.getSimpleName();
		String timeSeriesBolt = TimeSeriesBolt.class.getSimpleName();
		topologyBuilder.setBolt(json2DatapackageBolt, new Json2DatapackageBolt(),5).shuffleGrouping("kafka_spout");
		
		topologyBuilder.setBolt(kpiCalculateBolt, new KpiCalculateBolt()).shuffleGrouping(json2DatapackageBolt,kpiCalculateBolt);
		
		topologyBuilder.setBolt(cacheBolt, new CacheBolt(),5)
			.shuffleGrouping(json2DatapackageBolt)
			.shuffleGrouping(kpiCalculateBolt)
			;
//		
		topologyBuilder.setBolt(timeSeriesBolt, new TimeSeriesBolt(),5).shuffleGrouping(cacheBolt);
		
		
//		topologyBuilder.setBolt("2", new MoCounter(),9).globalGrouping("1");
//		topologyBuilder.setBolt("1", new TestBolt(),5).group
		
		
		Config config = new Config();
//		Map<String,String> config = new HashMap<>();
		config.put("redisHostPorts", "10.1.1.11:6379,10.1.1.11:6380,10.1.1.12:6379,10.1.1.12:6380,10.1.1.13:6379,10.1.1.13:6380");
		new LocalCluster().submitTopology("datapackageTopology", config, topologyBuilder.createTopology());
//		Config config = new Config();
//		StormSubmitter.submittop
//		config.set
	}
}
