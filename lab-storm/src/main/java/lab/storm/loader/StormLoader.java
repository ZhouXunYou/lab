package lab.storm.loader;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.topology.TopologyBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import lab.storm.bolt.CacheBolt;
import lab.storm.bolt.InfluxBolt;
import lab.storm.bolt.Json2DatapackageBolt;
import lab.storm.bolt.KpiCalculateBolt;
import lab.storm.bolt.TimeSeriesBolt;


@Component
public class StormLoader implements ApplicationListener<ContextRefreshedEvent> {
	private String kafkaServers;
	private String topic;
	private String redisServers;
	private boolean debug;
	private String influxdbServer;
	private String influxdbUser;
	private String influxdbPassword;

	public StormLoader(
			@Value("${kafka.broker.servers}") String kafkaServers, 
			@Value("${kafka.topic}") String topic,
			@Value("${redis.servers}") String redisServers,
			@Value("${debug.mode}") String debugMode,
			@Value("${influxdb.server}") String influxdbServer,
			@Value("${influxdb.user}") String influxdbUser,
			@Value("${influxdb.password}") String influxdbPassword) {
		this.kafkaServers = kafkaServers;
		this.topic = topic;
		this.redisServers = redisServers;
		this.debug = Boolean.parseBoolean(debugMode);
		this.influxdbServer = influxdbServer;
		this.influxdbUser = influxdbUser;
		this.influxdbPassword = influxdbPassword;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent context) {
		TopologyBuilder topologyBuilder = new TopologyBuilder();
		KafkaSpoutConfig<String,String> kafkaSpoutConfig = 
				KafkaSpoutConfig
				.builder(kafkaServers, topic)
				.setProp(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
				.setProp(ConsumerConfig.GROUP_ID_CONFIG,"STORM_GROUP")
//				ConsumerConfig.PART
//				.setProp(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
//				.setProp(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"true")
				.build();
		topologyBuilder.setSpout("kafka_spout", new KafkaSpout<>(kafkaSpoutConfig), 1);
		
		
		String json2DatapackageBolt = Json2DatapackageBolt.class.getSimpleName();
		String kpiCalculateBolt = KpiCalculateBolt.class.getSimpleName();
		String cacheBolt = CacheBolt.class.getSimpleName();
//		String timeSeriesBolt = TimeSeriesBolt.class.getSimpleName();
		String influxBolt = InfluxBolt.class.getSimpleName();
		
		topologyBuilder.setBolt(json2DatapackageBolt, new Json2DatapackageBolt(),5).shuffleGrouping("kafka_spout");
		
		topologyBuilder.setBolt(kpiCalculateBolt, new KpiCalculateBolt()).shuffleGrouping(json2DatapackageBolt,kpiCalculateBolt);
		
		topologyBuilder.setBolt(cacheBolt, new CacheBolt(),5)
			.shuffleGrouping(json2DatapackageBolt)
			.shuffleGrouping(kpiCalculateBolt);
		
		topologyBuilder.setBolt(influxBolt, new InfluxBolt(),5).shuffleGrouping(cacheBolt);
		
		
//		topologyBuilder.setBolt(timeSeriesBolt, new TimeSeriesBolt(),5).shuffleGrouping(cacheBolt);
		
		
		
		
		
//		topologyBuilder.setBolt("2", new MoCounter(),9).globalGrouping("1");
//		topologyBuilder.setBolt("1", new TestBolt(),5).group
		
		
		Config config = new Config();
//		Map<String,String> config = new HashMap<>();
		config.put("redisHostPorts", redisServers);
		config.put("influxdbServer", influxdbServer);
		config.put("influxdbUser", influxdbUser);
		config.put("influxdbPassword", influxdbPassword);
		
		if(debug) {
			new LocalCluster().submitTopology("datapackageTopology", config, topologyBuilder.createTopology());
		}else {
			try {
				StormSubmitter.submitTopology("datapackageTopology", config, topologyBuilder.createTopology());
			} catch (AlreadyAliveException | InvalidTopologyException | AuthorizationException e) {
				e.printStackTrace();
			}
		}
		
	}

}
