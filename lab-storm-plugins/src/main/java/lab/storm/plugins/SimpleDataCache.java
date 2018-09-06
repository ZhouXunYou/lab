package lab.storm.plugins;

import java.io.Serializable;

import redis.clients.jedis.JedisCluster;

public interface SimpleDataCache<T extends Serializable> {
	public void cache(JedisCluster cacheMapper,String key,T value);
}
