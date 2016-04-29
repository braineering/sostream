package com.threecore.project.control;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import com.threecore.project.model.conf.AppConfiguration;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.embedded.RedisServer;

public class EnvConfigurator {

	public static final StreamExecutionEnvironment setupExecutionEnvironment(AppConfiguration config) {
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();	
		env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
		//env.getConfig().setAutoWatermarkInterval(1000000);
		env.setBufferTimeout(config.getBufferTimeout());
		
		return env;
	}
	
	public static void initializeRedis(final String hostname, final int port) {
		try {
			RedisServer server = new RedisServer(port);
			server.start();
			JedisPool pool = new JedisPool(hostname, port);
			Jedis jedis = pool.getResource();
			jedis.flushAll();
			jedis.close();	
			pool.close();
		} catch (IOException | URISyntaxException exc) {
			exc.printStackTrace();
		}
	}
}
