package com.threecore.project.control;

import static org.junit.Assert.*;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import com.threecore.project.SimpleTest;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class TestRedisJedis extends SimpleTest {
	
	@Test
	public void insertionRemoval() throws NumberFormatException, InterruptedException, ExecutionException {
		JedisPool pool = new JedisPool("localhost", 6379);
		Jedis jedis = pool.getResource();
		
		long iterations = 1000;
		
		for (long i = 0; i < iterations; i++) {
			jedis.set(String.valueOf(i), String.valueOf(i + 1));
		}
		
		for (long i = 0; i < iterations; i++) {
			long v = Long.valueOf(jedis.get(String.valueOf(i)));
			assertEquals(i + 1, v);
		}
		
		for (long i = 0; i < iterations; i++) {
			jedis.del(String.valueOf(i));
		}
		
		jedis.close();
		pool.close();
	}	

}
