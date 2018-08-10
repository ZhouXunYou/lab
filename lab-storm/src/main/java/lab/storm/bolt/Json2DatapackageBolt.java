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

import com.fasterxml.jackson.databind.ObjectMapper;

import lab.storm.kqi.KQI;
import lab.storm.model.Datapackage;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class Json2DatapackageBolt extends BaseBasicBolt {

	private static final long serialVersionUID = 1L;
	private ObjectMapper om = new ObjectMapper();
	
	private JedisCluster cache;
	private int count;
	
//	Map<Integer,KpiExecutor> dynamicKpiExecutor = new HashMap<>();
	
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
		try {
			Datapackage datapackage = om.readValue(input.getString(4), Datapackage.class);
			
			List<KQI> kqis = new ArrayList<>();
			
			for(String kpiCode:datapackage.kpiCodes()) {
				List<KQI> kqiByKpi = getKqiOfKpi(kpiCode);
				if(kqiByKpi!=null) {
					kqis.addAll(kqiByKpi);
				}
			}
			if(kqis.size()>0) {
				count++;
				if(count%3==0) {
					
					collector.emit(KpiCalculateBolt.class.getSimpleName(), new Values(datapackage,kqis));
					count=0;
				}else {
					collector.emit(new Values(datapackage));
				}
			}else {
				collector.emit(new Values(datapackage));
			}
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	private List<KQI> getKqiOfKpi(String kpiCode) throws IOException, ClassNotFoundException {
		byte[] bytes = cache.get(("KQI_"+kpiCode).getBytes());
			if(bytes!=null && bytes.length>0) {
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			ois.close();
			return (List<KQI>)ois.readObject();
		}
		return null;
		
	}
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("datapackage"));
		declarer.declareStream(KpiCalculateBolt.class.getSimpleName(),new Fields("datapackage","kqis"));
	}
	
}
