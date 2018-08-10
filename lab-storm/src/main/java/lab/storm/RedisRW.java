package lab.storm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lab.storm.kqi.KQI;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class RedisRW {
	private static final JedisCluster cache;
	static{
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
		String[] redisHostPorts = new String[] {
			"10.1.1.11:6379","10.1.1.11:6380",
			"10.1.1.12:6379","10.1.1.12:6380",
			"10.1.1.13:6379","10.1.1.13:6380"
		};
		for(String redisHostPort:redisHostPorts) {
			String[] hostPort = redisHostPort.split(":");
			jedisClusterNodes.add(new HostAndPort(hostPort[0], Integer.valueOf(hostPort[1])));
		}
		cache = new JedisCluster(jedisClusterNodes);
	}
	public static void main(String[] args) {
		writeKqi("52010010081", "52910010081");
		readKqi("10010030031");
	}
	
	public static void writeKqi(String kpiCode,String kqiCode) {
		KQI kqi = new KQI();
		kqi.setKqiCode(kqiCode);
		kqi.setVersion(3);
		kqi.setScript("System.out.println(\"ccccccccccccccccccc\");");
		List<KQI> kqis = new ArrayList<>();
		kqis.add(kqi);
		cache.set(("KQI_"+kpiCode).getBytes(), object2byte(kqis));
	}
	@SuppressWarnings("unchecked")
	public static void readKqi(String kpiCode) {
		
		List<KQI> kqis = (List<KQI>)byte2Object(cache.get(("KQI_"+kpiCode).getBytes()));
		kqis.forEach(kqi->{
			System.out.println(kqi.getKqiCode());
			System.out.println(kqi.getScript());
			System.out.println(kqi.getVersion());
		});
	}
	private static byte[] object2byte(Object obj) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	private static Object byte2Object(byte[] cacheValue) {
		ByteArrayInputStream bis = new ByteArrayInputStream(cacheValue);
		try {
			ObjectInputStream ois = new ObjectInputStream(bis);
			return ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
