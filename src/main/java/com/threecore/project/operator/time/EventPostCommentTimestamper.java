package com.threecore.project.operator.time;

import org.apache.flink.api.common.functions.MapFunction;

import com.threecore.project.model.EventPostComment;

public class EventPostCommentTimestamper implements MapFunction<EventPostComment, Long> {

	private static final long serialVersionUID = 1L;

	@Override
	public Long map(EventPostComment event) throws Exception {
		return event.getTimestamp();
	}

}
