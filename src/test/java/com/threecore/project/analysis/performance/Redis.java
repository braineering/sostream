package com.threecore.project.analysis.performance;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class Redis {
	
	private static final long OPERATIONS = 1000;
	private static final long ITERATIONS = 10;
	
	private static final String HOSTNAME = "localhost";
	private static final int PORT = 6379;
	private static final String CONNSTRING = "redis://127.0.0.1:6379";
	
	private static JedisPool POOL;
	private static Jedis JEDIS;
	
	private static RedisClient REDIS;
	private static StatefulRedisConnection<String, String> REDISCONN;
	private static RedisCommands<String, String> REDISCMD;

	public static void main(String[] args) {		
		long operations = (args.length >= 1) ? Long.valueOf(args[0]) : OPERATIONS;
		long iterations = (args.length >= 2) ? Long.valueOf(args[1]) : ITERATIONS;
		
		System.out.println("********************************************************************************");
		System.out.println("* PERFORMANCE ANALYSIS: REDIS");
		System.out.println("********************************************************************************");
		System.out.println("operations: " + operations);
		System.out.println("iterations: " + iterations);
		
		testJedis(operations, iterations);
		testLettuce(operations, iterations);
	}
	
	private static void testJedis(final long operations, final long iterations) {		
		System.out.println("********************************************************************************");
		System.out.println("* JEDIS");
		System.out.println("********************************************************************************");
		
		System.out.print("setup");
		double setup_latency = 0.0;
		for (long i = 0; i < iterations; i++) {
			System.out.print(".");
			long setup_start = System.currentTimeMillis();
			setupJedis();			
			long setup_end = System.currentTimeMillis();
			setup_latency += setup_end - setup_start;
		}
		setup_latency /= iterations;	
		
		System.out.println("ok");
		
		System.out.print("open/close");
		double openclose_latency = 0.0;
		for (long i = 0; i < iterations; i++) {
			System.out.print(".");
			long openclose_start = System.currentTimeMillis();
			openJedis();
			closeJedis();
			long openclose_end = System.currentTimeMillis();
			openclose_latency += openclose_end - openclose_start;
		}
		openclose_latency /= iterations;
		
		System.out.println("ok");		
		
		System.out.print("operate");
		double op_latency = 0.0;
		for (long i = 0; i < iterations; i++) {
			System.out.print(".");
			openFlushJedis();
			long op_start = System.currentTimeMillis();		
			operateJedis(operations);
			long op_end = System.currentTimeMillis();
			op_latency += op_end - op_start;
			closeJedis();
		}
		op_latency /= iterations;
		
		System.out.println("ok");	
		
		System.out.println("********************************************************************************");
		System.out.println("* JEDIS");
		System.out.println("********************************************************************************");
		System.out.println("* STP: " + setup_latency);
		System.out.println("* OPC: " + openclose_latency);
		System.out.println("* OPS: " + op_latency);
		System.out.println("********************************************************************************");
	}	
	
	private static void testLettuce(final long operations, final long iterations) {		
		System.out.println("********************************************************************************");
		System.out.println("* LETTUCE");
		System.out.println("********************************************************************************");
		
		System.out.print("setup");
		double setup_latency = 0.0;
		for (long i = 0; i < iterations; i++) {
			System.out.print(".");
			long setup_start = System.currentTimeMillis();
			setupLettuce();			
			long setup_end = System.currentTimeMillis();
			setup_latency += setup_end - setup_start;
		}
		setup_latency /= iterations;	
		
		System.out.println("ok");
		
		System.out.print("open/close");
		double openclose_latency = 0.0;
		for (long i = 0; i < iterations; i++) {
			System.out.print(".");
			long openclose_start = System.currentTimeMillis();
			openLettuce();
			closeLettuce();
			long openclose_end = System.currentTimeMillis();
			openclose_latency += openclose_end - openclose_start;
		}
		openclose_latency /= iterations;
		
		System.out.println("ok");		
		
		System.out.print("operate");
		double op_latency = 0.0;
		for (long i = 0; i < iterations; i++) {
			System.out.print(".");
			openFlushLettuce();
			long op_start = System.currentTimeMillis();		
			operateLettuce(operations);
			long op_end = System.currentTimeMillis();
			op_latency += op_end - op_start;
			closeLettuce();
		}
		op_latency /= iterations;
		
		System.out.println("ok");		
		
		System.out.println("********************************************************************************");
		System.out.println("* LETTUCE");
		System.out.println("********************************************************************************");
		System.out.println("* STP: " + setup_latency);
		System.out.println("* OPC: " + openclose_latency);
		System.out.println("* OPS: " + op_latency);
		System.out.println("********************************************************************************");
	}	
	
	private static void setupJedis() {
		POOL = new JedisPool(HOSTNAME, PORT);
		JEDIS = POOL.getResource();
		JEDIS.flushAll();
		JEDIS.close();
		POOL.close();
	}
	
	private static void openJedis() {
		POOL = new JedisPool(HOSTNAME, PORT);
		JEDIS = POOL.getResource();		
	}
	
	private static void openFlushJedis() {
		POOL = new JedisPool(HOSTNAME, PORT);
		JEDIS = POOL.getResource();		
		JEDIS.flushAll();
	}
	
	private static void closeJedis() {
		JEDIS.close();
		POOL.close();
	}
	
	private static void operateJedis(final long operations) {
		for (long op = 0; op < operations; op++) {
			JEDIS.set(String.valueOf(op), String.valueOf(op + 1));
		}		
		for (long op = 0; op < operations; op++) {
			long v = Long.valueOf(JEDIS.get(String.valueOf(op)));
			v = v + 1;
			JEDIS.set(String.valueOf(op), String.valueOf(v));
		}		
		for (long op = 0; op < operations; op++) {
			JEDIS.del(String.valueOf(op));
		}
	}
	
	private static void setupLettuce() {
		REDIS = RedisClient.create(CONNSTRING);
		REDISCONN = REDIS.connect();
		REDISCMD = REDISCONN.sync();
		REDISCMD.flushall();
		REDISCONN.close();
		REDIS.shutdown();
	}
	
	private static void openLettuce() {
		REDIS = RedisClient.create(CONNSTRING);
		REDISCONN = REDIS.connect();
		REDISCMD = REDISCONN.sync();
	}
	
	private static void openFlushLettuce() {
		REDIS = RedisClient.create(CONNSTRING);
		REDISCONN = REDIS.connect();
		REDISCMD = REDISCONN.sync();
		REDISCMD.flushall();
	}
	
	private static void closeLettuce() {
		REDISCONN.close();
		REDIS.shutdown();
	}
	
	private static void operateLettuce(final long operations) {
		for (long op = 0; op < operations; op++) {
			REDISCMD.lpush(String.valueOf(op), String.valueOf(op + 1));
		}	
		for (long op = 0; op < operations; op++) {
			long v = Long.valueOf(REDISCMD.get(String.valueOf(op)));
			v = v + 1;
			REDISCMD.set(String.valueOf(op), String.valueOf(v));
		}		
		for (long op = 0; op < operations; op++) {
			REDISCMD.del(String.valueOf(op));
		}
	}

}
