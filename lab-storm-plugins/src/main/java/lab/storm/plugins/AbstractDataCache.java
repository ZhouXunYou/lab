package lab.storm.plugins;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

import redis.clients.jedis.JedisCluster;

public abstract class AbstractDataCache implements SimpleDataCache<Serializable>,ComplexDataCache<Serializable> {

	@Override
	public void cache(JedisCluster cacheMapper, String key, Serializable value) {
		cache(cacheMapper,key,CacheType.SINGLE_VALUE,value);
	}

	@Override
	public void cache(JedisCluster cacheMapper, String key, CacheType type, Serializable value) {
		try {
			switch(type) {
			case SINGLE_VALUE:
				cacheMapper.set(key.getBytes(), getValueBytes(value));
				break;
			case COLLECTION:
				cacheMapper.lpush(key.getBytes(), getValueBytes(value));
				break;
			case MAP:
				@SuppressWarnings("unchecked")
				Map<byte[],byte[]> mapValue = (Map<byte[],byte[]>)value;
				cacheMapper.hmset(key.getBytes(), mapValue);
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private byte[] getValueBytes(Serializable value) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(value);
		return bos.toByteArray();
	}
	
}
