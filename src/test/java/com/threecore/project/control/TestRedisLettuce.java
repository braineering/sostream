package com.threecore.project.control;

import static org.junit.Assert.*;
import java.util.concurrent.ExecutionException;
import org.junit.Test;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.async.RedisAsyncCommands;
import com.threecore.project.SimpleTest;

public class TestRedisLettuce extends SimpleTest {	
	
	@Test
	public void insertionRemoval() throws NumberFormatException, InterruptedException, ExecutionException {
		RedisClient redis = RedisClient.create("redis://127.0.0.1:6379");
		StatefulRedisConnection<String, String> redisConn = redis.connect();
		RedisAsyncCommands<String, String> redisCmd = redisConn.async();
		
		long iterations = 1000;
		
		for (long i = 0; i < iterations; i++) {
			redisCmd.set(String.valueOf(i), String.valueOf(i + 1));
		}
		
		for (long i = 0; i < iterations; i++) {
			long v = Long.valueOf(redisCmd.get(String.valueOf(i)).get());
			assertEquals(i + 1, v);
		}
		
		for (long i = 0; i < iterations; i++) {
			redisCmd.del(String.valueOf(i));
		}
		
		redisConn.close();
		redis.shutdown();
	}
	
	/*
	 private static final double LATENCIES[] = getLatencies();
	@Test
	@Ignore
	public void simple() throws InterruptedException, ExecutionException {
		RedisClient redis = RedisClient.create("redis://127.0.0.1:6379");
		StatefulRedisConnection<String, String> redisConn = redis.connect();
		RedisAsyncCommands<String, String> redisCmd = redisConn.async();
		
		long counter;
		double average;
		
		redisCmd.set("counter", "0");
		redisCmd.set("average", "0.0");
		
		counter = Long.valueOf(redisCmd.get("counter").get());
		average = Double.valueOf(redisCmd.get("average").get());
		assertEquals(0, counter);
		assertEquals(0.0, average, 0.0);
		
		redisCmd.set("counter", "1");
		redisCmd.set("average", "1.0");
		
		counter = Long.valueOf(redisCmd.get("counter").get());
		average = Double.valueOf(redisCmd.get("average").get());
		assertEquals(1, counter);
		assertEquals(1.0, average, 0.0);
		
		redisConn.close();
		redis.shutdown();
	}	

	@Test
	@Ignore
	public void latency() throws InterruptedException, ExecutionException {
		RedisClient redis = RedisClient.create("redis://127.0.0.1:6379");
		StatefulRedisConnection<String, String> redisConn = redis.connect();
		RedisAsyncCommands<String, String> redisCmd = redisConn.async();
		
		DescriptiveStatistics stats = new DescriptiveStatistics();
		
		double mean = 0.0;
		
		for (double latency : LATENCIES) {
			stats.addValue(latency);
			mean = stats.getMean();
			redisCmd.set("mean", String.valueOf(mean));
		}
		
		assertEquals(5.5, mean, 0.0);
		
		redisConn.close();
		redis.shutdown();
	}
	
	private static double[] getLatencies() {
		double latencies[] = new double[10];
		
		for (int i = 1; i <= 10; i++) {
			latencies[i-1] = i;
		}
		
		return latencies;
	}*/

}
