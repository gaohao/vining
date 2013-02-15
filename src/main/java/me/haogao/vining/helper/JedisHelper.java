package me.haogao.vining.helper;

import redis.clients.jedis.JedisPool;

public class JedisHelper {
	public static JedisPool getPool(String host, int port) {
		return new JedisPool(host, port);
	}
	
	public static void destroy(JedisPool pool) {
		pool.destroy();
	}
}
