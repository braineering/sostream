package com.threecore.project.operator.event;

import org.apache.flink.api.common.functions.RichMapFunction;
import com.threecore.project.model.EventCommentFriendshipLike;
import com.threecore.project.model.EventCommentLike;

public class EventCommentLikeExtractor extends RichMapFunction<EventCommentFriendshipLike, EventCommentLike> {

	private static final long serialVersionUID = 1L;

	@Override
	public EventCommentLike map(EventCommentFriendshipLike event) throws Exception {
		if (event.isComment()) {
			return new EventCommentLike(event.getComment());
		} else {
			return new EventCommentLike(event.getLike());
		}
	}

}
