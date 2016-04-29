package com.threecore.project.operator.time;

import org.apache.flink.api.common.functions.MapFunction;

import com.threecore.project.model.EventQueryOne;

public class EventPostCommentTimestamper2 implements MapFunction<EventQueryOne, Long> {

	private static final long serialVersionUID = 1L;

	@Override
	public Long map(EventQueryOne event) throws Exception {
		return event.getTimestamp();
	}

}
