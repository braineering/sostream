package com.threecore.project.operator.time;

import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

import com.threecore.project.model.type.Chronological;

public class Timestamper<T extends Chronological> implements AssignerWithPeriodicWatermarks<T> {

	private static final long serialVersionUID = 1L;
	
	private static final long DELAY = 864000000000L; //1000
	
	private long delay;
	private long maxts;
	
	public Timestamper() {
		this(DELAY);
	}
	
	public Timestamper(final long delay) {
		this.delay = delay;
	}

	@Override
	public long extractTimestamp(Chronological element, long currentTimestamp) {
		this.maxts = Math.max(maxts, element.getTimestamp());
        return element.getTimestamp();
	}

	@Override
	public Watermark getCurrentWatermark() {
		return null;
		//return new Watermark(this.maxts - this.delay);
	}

}
