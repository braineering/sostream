package com.threecore.project.operator.nop;

import org.apache.flink.api.common.functions.ReduceFunction;

public class NopReduce<T> implements ReduceFunction<T> {

	private static final long serialVersionUID = 1L;

	@Override
	public T reduce(T value1, T value2) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
