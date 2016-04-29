package com.threecore.project.control;

import java.util.ArrayList;
import java.util.List;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.joda.time.DateTime;

import com.threecore.project.model.Comment;
import com.threecore.project.model.EventCommentFriendshipLike;
import com.threecore.project.model.Friendship;
import com.threecore.project.model.Like;
import com.threecore.project.model.ModelCommons;
import com.threecore.project.model.conf.AppConfiguration;
import com.threecore.project.operator.source.EventCommentFriendshipLikeSource;
import com.threecore.project.operator.time.AscendingTimestamper;

public class EventCommentFriendshipLikeStreamgen {
	
	public static final DateTime START = new DateTime(2016, 1, 1, 12, 0, 0, 0);
	
	public static DataStream<EventCommentFriendshipLike> getEvents(StreamExecutionEnvironment env, AppConfiguration config) {
		String commentSource = config.getComments();
		String friendshipSource = config.getFriendships();
		String likeSource = config.getLikes();		
		
		DataStream<EventCommentFriendshipLike> events = null;
		
		if (commentSource == null || friendshipSource == null || likeSource == null) {
			List<EventCommentFriendshipLike> list = EventCommentFriendshipLikeStreamgen.getDefault();
			events = env.fromCollection(list); 
		} else {
			events = env.addSource(new EventCommentFriendshipLikeSource(commentSource, friendshipSource, likeSource), "events-cfl-source");
		}			
		
		events.assignTimestampsAndWatermarks(new AscendingTimestamper<EventCommentFriendshipLike>());
		
		return events;		
	}
	
	public static final List<EventCommentFriendshipLike> getDefault() {
		List<EventCommentFriendshipLike> list = new ArrayList<EventCommentFriendshipLike>();
		
		Comment comment1 = new Comment(START.plusMinutes(20), 10L, 1L, "comment->post-1", "user_1", ModelCommons.UNDEFINED_LONG, 1L);
		Comment comment2 = new Comment(START.plusMinutes(25), 20L, 1L, "comment->comment->post-1", "user 1", 10L, ModelCommons.UNDEFINED_LONG);
		Comment comment3 = new Comment(START.plusMinutes(30), 30L, 1L, "comment->comment->post-1", "user 1", 20L, ModelCommons.UNDEFINED_LONG);
		
		Friendship friendship1 = new Friendship(START.plusMinutes(35), 1L, 2L);
		Friendship friendship2 = new Friendship(START.plusMinutes(40), 1L, 3L);
		Friendship friendship3 = new Friendship(START.plusMinutes(45), 2L, 3L);		
		
		Like like1 = new Like(START.plusMinutes(50), 1L, 10L);
		Like like2 = new Like(START.plusMinutes(55), 1L, 20L);
		Like like3 = new Like(START.plusMinutes(60), 1L, 30L);
		
		EventCommentFriendshipLike event1 = new EventCommentFriendshipLike(comment1);
		EventCommentFriendshipLike event2 = new EventCommentFriendshipLike(comment2);
		EventCommentFriendshipLike event3 = new EventCommentFriendshipLike(comment3);
		EventCommentFriendshipLike event4 = new EventCommentFriendshipLike(friendship1);
		EventCommentFriendshipLike event5 = new EventCommentFriendshipLike(friendship2);
		EventCommentFriendshipLike event6 = new EventCommentFriendshipLike(friendship3);
		EventCommentFriendshipLike event7 = new EventCommentFriendshipLike(like1);
		EventCommentFriendshipLike event8 = new EventCommentFriendshipLike(like2);
		EventCommentFriendshipLike event9 = new EventCommentFriendshipLike(like3);
		
		list.add(event1);
		list.add(event2);
		list.add(event3);
		list.add(event4);
		list.add(event5);
		list.add(event6);
		list.add(event7);
		list.add(event8);
		list.add(event9);
		
		return list;
	}

}
