package com.threecore.project.operator.key;

import org.apache.flink.api.java.functions.KeySelector;

import com.threecore.project.model.EventPostComment;

public class EventPostCommentKeyer implements KeySelector<EventPostComment, Long> {

	private static final long serialVersionUID = 1L;

	@Override
	public Long getKey(EventPostComment event) throws Exception {
		if (event.isPost()) {
			return event.getPost().getPostId();
		} else if (event.isComment()) {
			return event.getComment().getPostCommentedId();
		} else {
			return -1L;
		}
	}

}
