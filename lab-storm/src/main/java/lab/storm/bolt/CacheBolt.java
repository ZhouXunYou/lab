package lab.storm.bolt;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import lab.storm.model.AlarmRule;
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
		String moId = datapackage.getMoId();
		/*
		 * 从缓存获取对象的告警规则包
		 */
		
		List<AlarmRule> alarmRules = new ArrayList<>();
		datapackage.getKpiDatas().forEach((kpiCode,kpiDatas)->{
			List<byte[]> bytes = cache.hmget(moId.getBytes(), kpiCode.getBytes());
			if(bytes!=null && bytes.size()>0) {
				
				try {
					for(byte[] objectByte:bytes) {
						ByteArrayInputStream bis = new ByteArrayInputStream(objectByte);
						ObjectInputStream ois = new ObjectInputStream(bis);
						AlarmRule alarmRule = (AlarmRule)ois.readObject();
						alarmRules.add(alarmRule);
					}
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			/*
			 * KPI数据写入缓存的同时，判断是否有告警规则生效，避免之后又重复做KPI的遍历
			 */
			byte[] key = String.format("%s_%s", datapackage.getMoId(),kpiCode).getBytes();
			cache.lpush(key, ObjectByteOperations.object2byte(kpiDatas));
//			cache.set
			cache.ltrim(key, 0, 9);
//			cache.expire
		});
		System.out.println(String.format("mo %s data write cache", datapackage.getMoId()));
		collector.emit(new Values(datapackage));
		
		/*
		 * 将告警规则及数据包发送至AlarmBolt
		 */
		if(alarmRules.size()>0) {
			collector.emit(ThresholdBolt.class.getSimpleName(),new Values(datapackage,alarmRules));
		}
	}
	
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("datapackage","alarmRules"));
	}

}
