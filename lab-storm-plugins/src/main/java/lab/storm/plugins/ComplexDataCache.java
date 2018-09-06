package lab.storm.plugins;

import java.io.Serializable;

import redis.clients.jedis.JedisCluster;

public interface ComplexDataCache<T extends Serializable> {
	public void cache(JedisCluster cacheMapper, String key, CacheType type,T value);

}
