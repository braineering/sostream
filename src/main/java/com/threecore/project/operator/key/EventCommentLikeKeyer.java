package com.threecore.project.operator.key;

import org.apache.flink.api.java.functions.KeySelector;
import com.threecore.project.model.EventCommentLike;

public class EventCommentLikeKeyer implements KeySelector<EventCommentLike, Long> {

	private static final long serialVersionUID = 1L;

	@Override
	public Long getKey(EventCommentLike event) throws Exception {
		if (event.isComment()){
			return event.getComment().getCommentId();
		}
		else{
			return event.getLike().getCommentId();
		}
	}

}
