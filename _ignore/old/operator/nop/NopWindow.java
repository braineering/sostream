package com.threecore.project.operator.nop;

import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class NopWindow<T> implements AllWindowFunction<T, T, TimeWindow> {

	private static final long serialVersionUID = 1L;

	@Override
	public void apply(TimeWindow window, Iterable<T> values, Collector<T> out) throws Exception {
		for (T value : values)
			out.collect(value);
	}

}
