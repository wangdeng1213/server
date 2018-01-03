package com.linksus.common.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisUtil{
	private static RedisTemplate redisTemplate = (RedisTemplate) ContextUtil.getBean("redisTemplate");
	protected static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);

	/**
	 * ȡ��Redis�е�Hash���
	 * @param hashKey
	 * @param mapKey
	 * @return
	 */
	public static String getRedisHash(final String hashKey,final String fieldKey){
		return (String) redisTemplate.execute(new RedisCallback<String>() {

			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException{
				byte[] key1 = redisTemplate.getStringSerializer().serialize(hashKey);
				byte[] key2 = redisTemplate.getStringSerializer().serialize(fieldKey);
				if(connection.exists(key1)){
					byte[] value = connection.hGet(key1, key2);
					String rsData = (String) redisTemplate.getHashValueSerializer().deserialize(value);
					return rsData;
				}
				return null;
			}
		});
	}

	/**
	 * ��Redis�м���Hash���
	 * @param hashKey
	 * @param mapKey
	 * @param value
	 */
	public static void setRedisHash(final String hashKey,final String fieldKey,final String value){
		redisTemplate.execute(new RedisCallback<Object>() {

			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException{
				connection.hSet(redisTemplate.getStringSerializer().serialize(hashKey), redisTemplate
						.getStringSerializer().serialize(fieldKey), redisTemplate.getStringSerializer()
						.serialize(value));
				return null;
			}
		});
	}

	/**
	 * �������
	 * @param hashKey
	 * @param mapKey
	 * @param value
	 */
	public static void setRedisHashIncrby(final String hashKey,final String fieldKey,final long value){
		redisTemplate.execute(new RedisCallback<Object>() {

			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException{
				connection.hIncrBy(redisTemplate.getStringSerializer().serialize(hashKey), redisTemplate
						.getStringSerializer().serialize(fieldKey), value);
				return null;
			}
		});
	}

	/**
	 * ȡ��Redis�е�Hash�������
	 * @param hashKey
	 * @param mapKey
	 * @return
	 */
	public static Map getRedisHashAll(final String hashKey){
		return (Map) redisTemplate.execute(new RedisCallback<Map>() {

			@Override
			public Map doInRedis(RedisConnection connection) throws DataAccessException{
				Map rsMap = new HashMap();
				byte[] key1 = redisTemplate.getStringSerializer().serialize(hashKey);
				if(connection.exists(key1)){
					Map<byte[], byte[]> tmpMap = connection.hGetAll(key1);
					for(Iterator itor = tmpMap.entrySet().iterator(); itor.hasNext();){
						Entry<byte[], byte[]> entry = (Entry) itor.next();
						String key = (String) redisTemplate.getHashValueSerializer().deserialize(entry.getKey());
						String value = (String) redisTemplate.getHashValueSerializer().deserialize(entry.getValue());
						rsMap.put(key, value);
					}
					return rsMap;
				}
				return null;
			}
		});
	}

	public static void delKey(final String hashKey){
		redisTemplate.delete(hashKey);
	}

	/**
	 * ɾ��Hash���ض����
	 * @param hashKey
	 * @param fieldKey
	 */
	public static void delFieldKey(final String hashKey,final String fieldKey){
		redisTemplate.execute(new RedisCallback<Object>() {

			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException{
				connection.hDel(redisTemplate.getStringSerializer().serialize(hashKey), redisTemplate
						.getStringSerializer().serialize(fieldKey));
				return null;
			}
		});
	}

	/**
	 * ��Redis�м���Set���
	 * @param setName
	 * @param value 
	 */
	public static void setRedisSet(final String setName,final String value){
		redisTemplate.execute(new RedisCallback<Object>() {

			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException{
				connection.sAdd(redisTemplate.getStringSerializer().serialize(setName), redisTemplate
						.getStringSerializer().serialize(value));
				return null;
			}
		});
	}

	/**
	 * �ж�set���Ƿ���ڴ�Ԫ��
	 * 
	 */
	public static Boolean getRedisSet(final String hashKey,final String value){
		return (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {

			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException{
				boolean flag = false;
				byte[] key1 = redisTemplate.getStringSerializer().serialize(hashKey);
				byte[] key2 = redisTemplate.getStringSerializer().serialize(value);
				if(connection.exists(key1)){
					flag = connection.sIsMember(key1, key2);
					return flag;
				}
				return flag;
			}
		});
	}

}
