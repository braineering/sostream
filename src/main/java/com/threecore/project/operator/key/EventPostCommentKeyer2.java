package com.threecore.project.operator.key;

import org.apache.flink.api.java.functions.KeySelector;

import com.threecore.project.model.EventQueryOne;

public class EventPostCommentKeyer2 implements KeySelector<EventQueryOne, Long> {

	private static final long serialVersionUID = 1L;

	@Override
	public Long getKey(EventQueryOne event) throws Exception {
		return event.getId();
	}

}
