package com.threecore.project.operator.event;

import java.util.ArrayList;
import java.util.List;

import org.apache.flink.streaming.api.collector.selector.OutputSelector;

import com.threecore.project.model.EventCommentFriendshipLike;

public class FriendshipSplitter implements OutputSelector<EventCommentFriendshipLike> {

	private static final long serialVersionUID = 1;
	
	public static final String EVENT_ALL = "event_all";
	public static final String FRIENDSHIP_ONLY = "friendship_only";

	@Override
	public Iterable<String> select(EventCommentFriendshipLike event) {
		List<String> events = new ArrayList<String>();
		
		if (event.isFriendship()) {
			events.add(FRIENDSHIP_ONLY);
		} else {
			events.add(EVENT_ALL);
		}
		
		return events;
	}

}
