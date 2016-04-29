package com.threecore.project.operator.time;

import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

import com.threecore.project.model.type.Chronological;

public class CustomTimestamper<T extends Chronological> implements AssignerWithPeriodicWatermarks<T> {

	private static final long serialVersionUID = 1L;

	@Override
	public long extractTimestamp(T element, long previousElementTimestamp) {
		return element.getTimestamp();
	}

	@Override
	public Watermark getCurrentWatermark() {
		// TODO Auto-generated method stub
		return null;
	}

}
