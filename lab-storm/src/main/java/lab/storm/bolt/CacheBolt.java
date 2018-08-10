package lab.storm.bolt;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import lab.storm.ObjectByteOperations;
import lab.storm.model.Datapackage;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class CacheBolt extends BaseBasicBolt {
	private static final long serialVersionUID = 1L;
	private JedisCluster cache;
	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		// TODO Auto-generated method stub
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
		String[] redisHostPorts = stormConf.get("redisHostPorts").toString().split(",");
		for(String redisHostPort:redisHostPorts) {
			String[] hostPort = redisHostPort.split(":");
			jedisClusterNodes.add(new HostAndPort(hostPort[0], Integer.valueOf(hostPort[1])));
		}
		cache = new JedisCluster(jedisClusterNodes);
		
	}
	
	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		Datapackage datapackage = (Datapackage)input.getValueByField("datapackage");
		datapackage.getKpiDatas().forEach((kpiCode,kpiDatas)->{
			byte[] key = String.format("%s_%s", datapackage.getMoId(),kpiCode).getBytes();
			cache.lpush(key, ObjectByteOperations.object2byte(kpiDatas));
			cache.ltrim(key, 0, 9);
		});
		System.out.println(String.format("mo %s data write cache", datapackage.getMoId()));
		collector.emit(new Values(datapackage));
	}
	
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("datapackage"));
	}

}
