package com.threecore.project.operator.key;

import org.apache.flink.api.java.functions.KeySelector;

import com.threecore.project.model.EventCommentFriendshipLike;

public class EventCommentFriendshipLikeKeyer implements KeySelector<EventCommentFriendshipLike, Long> {

	private static final long serialVersionUID = 1L;

	@Override
	public Long getKey(EventCommentFriendshipLike event) throws Exception {
		if (event.isComment()) {
			return event.getComment().getCommentId();
		} else if (event.isFriendship()) {
			return 0L;
		} else if (event.isLike()) {
			return event.getLike().getCommentId();
		} else {
			return -1L;
		}
	}

}
