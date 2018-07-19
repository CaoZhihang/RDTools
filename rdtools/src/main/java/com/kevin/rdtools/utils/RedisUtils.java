package com.kevin.rdtools.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;

/**
 * Created by yangdengwu on 17-9-19.
 * 
 * @author dwyang redis 工具类
 */
@Component
public class RedisUtils {

	private volatile JedisSentinelPool pool;

	@Value("${redis.sentinels.hosts}")
	private String sentinelHosts;

	@Value("${redis.maxActive}")
	private Integer maxActive;

	@Value("${redis.maxIdel}")
	private Integer maxIdel;

	@Value("${redis.maxWait}")
	private Integer maxWait;

	@Value("${redis.testOnBorrow}")
	private boolean testOnBorrow;

	@Value("${redis.masterName}")
	private String masterName;

	@Value("${redis.keyExpire:600}")
	private Integer keyExpireTime;
	
	@Value("${redis.password:123456}")
	private String password;

	@PostConstruct
	public void init() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(maxActive);
		poolConfig.setMaxIdle(maxIdel);
		poolConfig.setMaxWaitMillis(maxWait);
		poolConfig.setTestOnBorrow(testOnBorrow);

		Set<String> sentinels = new HashSet<String>();
		sentinels.addAll(Arrays.asList(sentinelHosts.split(",")));

		//pool = new JedisSentinelPool(masterName, sentinels, poolConfig, password);
		pool = new JedisSentinelPool(masterName, sentinels, poolConfig);
	}

	/**
	 * 执行比　较对应key值大小，更新较大值　的lua脚本
	 * @param key
	 * @param time
	 * @return
	 */
	public Object updateHigherIntWithLua(String key, long time){
		String script = "if redis.call('EXISTS',KEYS[1])~=0 then\n"
				+"local tmp = redis.call('get',KEYS[1])\n"
				+"if tonumber(tmp)<tonumber(KEYS[2]) then\n"
				+"redis.call('set',KEYS[1],KEYS[2])\n"
				+"end\n"
				+"return 1\n"
				+"else\n"
				+"redis.call('set',KEYS[1],KEYS[2])\n"
				+"return 0\n"
				+"end";
		return executeLua(script, 2, key, String.valueOf(time));
	}
	
	/**
	 * redis执行lua脚本
	 * @param lua
	 * @param keyCount
	 * @param params
	 * @return
	 */
	public Object executeLua(String lua, int keyCount, String... params){
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.eval(lua, keyCount, params);
		} finally {
			if (null != jedis){
				jedis.close();
			}
		}
	}
	
	public Set<String> keys(String pattern) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.keys(pattern);
		} finally {
			if (null != jedis) {
				jedis.close();
			}
		}
	}

	public String get(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.get(key);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	public void setForever(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.set(key, value);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	public void setex(String key, int seconds, String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.setex(key, seconds, value);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}		
	}
	
	public void set(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.setex(key, keyExpireTime, value);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public void remove(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.del(key);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public void incr(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.incr(key);
			jedis.expire(key, keyExpireTime);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public void hmset(String key, Map<String, String> hash) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.hmset(key, hash);
			jedis.expire(key, keyExpireTime);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public String hmget(String key, String field) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			List<String> list = jedis.hmget(key, field);
			if (list == null || list.size() == 0) {
				return null;
			} else {
				return list.get(0);
			}
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public Map<String, String> hmget(String key, String... fields) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			List<String> list = jedis.hmget(key, fields);
			Map<String, String> map = new HashMap<String, String>();
			for (int i = 0; i < fields.length; i++) {
				map.put(fields[i], list.get(i));
			}

			return map;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public void lpush(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.lpush(key, value);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public void rpush(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.rpush(key, value);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public void lrem(String key, int count, String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.lrem(key, count, value);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public List<String> lrange(String key, long start, long end) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			List<String> result = jedis.lrange(key, start, end);
			return result;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public Object executeLua(String luaScript, List<String> keys, List<String> args) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.eval(luaScript, keys, args);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public boolean hexists(String key, String field) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.hexists(key, field);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public boolean exists(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.exists(key);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@PreDestroy
	public void close() {
		if (pool != null) {
			pool.close();
		}
	}

}
