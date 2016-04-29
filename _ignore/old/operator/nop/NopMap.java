package com.threecore.project.operator.nop;

import java.util.Random;

import org.apache.flink.api.common.functions.MapFunction;

public class NopMap<T> implements MapFunction<T, T> {

	private static final long serialVersionUID = 1L;
	
	private static final long MIN_LATENCY = 10;
	private static final long MAX_LATENCY = 100;
	
	private static Random RND = new Random();	

	@Override
	public T map(T value) throws Exception {
		//Thread.sleep(this.getRandomLatency());
		return value;
	}
	
	@SuppressWarnings("unused")
	private long getRandomLatency() {
		return 	MIN_LATENCY + (long)(RND.nextDouble() * (MAX_LATENCY - MIN_LATENCY));
	}

}
