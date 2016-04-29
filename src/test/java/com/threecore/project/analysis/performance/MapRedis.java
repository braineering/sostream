package com.threecore.project.analysis.performance;

import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class MapRedis {
	
	private static final long OPERATIONS = 1000000;
	private static final long ITERATIONS = 10;
	
	private static final String HOSTNAME = "localhost";
	private static final int PORT = 6379;
	
	private static JedisPool POOL;
	private static Jedis JEDIS;
	private static Map<String, String> MAPPER;

	public static void main(String[] args) {
		long operations = (args.length >= 1) ? Long.valueOf(args[0]) : OPERATIONS;
		long iterations = (args.length >= 2) ? Long.valueOf(args[1]) : ITERATIONS;
		
		System.out.println("********************************************************************************");
		System.out.println("* PERFORMANCE ANALYSIS: POST-COMMENT-MAPPING");
		System.out.println("********************************************************************************");
		System.out.println("operations: " + operations);
		System.out.println("iterations: " + iterations);
		
		testStandardCommentMap(operations, iterations);
		testRedisCommentMap(operations, iterations);
	}	

	private static void testStandardCommentMap(final long operations, final long iterations) {
		System.out.println("********************************************************************************");
		System.out.println("* STANDARD-HASH-MAP");
		System.out.println("********************************************************************************");
		
		MAPPER = new HashMap<String, String>();
		
		System.out.print("operate");
		double op_latency = 0.0;
		for (long i = 0; i < iterations; i++) {
			System.out.print(".");
			long op_start = System.currentTimeMillis();		
			operateHashMap(operations);
			long op_end = System.currentTimeMillis();
			op_latency += op_end - op_start;
		}
		op_latency /= iterations;
		
		System.out.println("ok");	
		
		System.out.println("********************************************************************************");
		System.out.println("* STANDARD-HASH-MAP");
		System.out.println("********************************************************************************");
		System.out.println("* OPS: " + op_latency);
		System.out.println("********************************************************************************");
		
		
	}
	
	private static void testRedisCommentMap(long operations, long iterations) {
		System.out.println("********************************************************************************");
		System.out.println("* REDIS-HASH-MAP");
		System.out.println("********************************************************************************");
		
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
		System.out.println("* REDIS-HASH-MAP");
		System.out.println("********************************************************************************");
		System.out.println("* OPS: " + op_latency);
		System.out.println("********************************************************************************");
		
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
	
	private static void operateHashMap(final long operations) {
		for (long op = 0; op < operations; op++) {
			MAPPER.put(String.valueOf(op), String.valueOf(op+1));
		}		
		for (long op = 0; op < operations; op++) {
			long v = Long.valueOf(MAPPER.get(String.valueOf(op)));
			v = v + 1;
			MAPPER.put(String.valueOf(op), String.valueOf(v));
		}		
		for (long op = 0; op < operations; op++) {
			MAPPER.remove(String.valueOf(op));
		}
	}

}
