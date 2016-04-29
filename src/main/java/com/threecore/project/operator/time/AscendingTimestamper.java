package com.threecore.project.operator.time;

import org.apache.flink.streaming.api.functions.AscendingTimestampExtractor;

import com.threecore.project.model.type.Chronological;

public class AscendingTimestamper<T extends Chronological> extends AscendingTimestampExtractor<T> {

	private static final long serialVersionUID = 1L;

	@Override
	public long extractAscendingTimestamp(T element) {
		return element.getTimestamp();
	}

}
