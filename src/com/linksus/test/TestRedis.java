package com.linksus.test;

import redis.clients.jedis.Jedis;

import com.linksus.common.util.RedisUtil;

public class TestRedis{

	public static void main(String[] args){
		Jedis jj = new Jedis("10.10.2.131", 6379);
		//Jedis jj = new Jedis("192.168.1.35", 6379);
		jj.auth("linksus");
		jj.hset("testh", "test", "12");
		System.out.println(jj.hget("testh", "test"));
		//System.out.println(jj.hget("relation_weibouser","194CF7C2576201CA946B4453F9655E26"));
		//System.out.println(jj.hget("relation_user_account","151#42020"));
		System.out.println(jj.hlen("testh"));
		RedisUtil.delFieldKey("testh", "test");
		System.out.println(jj.hget("testh", "test"));
		System.out.println(jj.hlen("testh"));
		RedisUtil.delKey("testh");
		System.out.println(jj.hlen("testh"));
		System.out.println(jj.hget("relation_user_account", "2210#5419161"));
	}
}
