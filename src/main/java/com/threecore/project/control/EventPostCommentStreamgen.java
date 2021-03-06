package com.threecore.project.control;

import java.util.ArrayList;
import java.util.List;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.joda.time.DateTime;

import com.threecore.project.model.Comment;
import com.threecore.project.model.EventPostComment;
import com.threecore.project.model.EventQueryOne;
import com.threecore.project.model.ModelCommons;
import com.threecore.project.model.Post;
import com.threecore.project.model.conf.AppConfiguration;
import com.threecore.project.operator.source.EventPostCommentSource;
import com.threecore.project.operator.source.EventQueryOneSource1;
import com.threecore.project.operator.time.AscendingTimestamper;

public class EventPostCommentStreamgen {
	
	public static final DateTime START = new DateTime(2016, 1, 1, 12, 0, 0, 0);
	
	public static DataStream<EventQueryOne> getStreamOfEvents(StreamExecutionEnvironment env, AppConfiguration config) {
		String posts = config.getPosts();
		String comments = config.getComments();

		DataStream<EventQueryOne> events = env.addSource(
				new EventQueryOneSource1(posts, comments),
				"events-pc-source");
		//events.assignTimestampsAndWatermarks(new CustomTimestamper<EventQueryOne>());
		//events.assignTimestampsAndWatermarks(new AscendingTimestamper<EventQueryOne>());
		//events.assignTimestampsAndWatermarks(new Timestamper<EventQueryOne>());
		return events;
	}
	
	public static DataStream<EventPostComment> getEvents(StreamExecutionEnvironment env, AppConfiguration config) {
		String postSource = config.getPosts();
		String commentSource = config.getComments();
		
		DataStream<EventPostComment> events = null;
		
		if (postSource == null || commentSource == null) {
			List<EventPostComment> list = EventPostCommentStreamgen.getDefault();
			events = env.fromCollection(list); 
		} else {
			events = env.addSource(new EventPostCommentSource(postSource, commentSource), "events-pc-source");
		}			
		
		events.assignTimestampsAndWatermarks(new AscendingTimestamper<EventPostComment>());
		
		return events;		
	}
	
	public static final List<EventPostComment> getDefault() {
		List<EventPostComment> list = new ArrayList<EventPostComment>();
		
		Post post1 = new Post(START, 1L, 1L, "Hello World 1!", "User 1");
		Comment comment1 = new Comment(START.plusMinutes(5), 10L, 2L, "Comment->Post1", "User 2", ModelCommons.UNDEFINED_LONG, 1L);
		
		Post post2 = new Post(START.plusMinutes(10), 2L, 1L, "Hello World 2!", "User 1");
		Post post3 = new Post(START.plusMinutes(15), 3L, 1L, "Hello World 3!", "User 1");
		
		Comment comment2 = new Comment(START.plusMinutes(20), 20L, 2L, "Comment->Post2", "User 2", ModelCommons.UNDEFINED_LONG, 2L);
		Comment comment3 = new Comment(START.plusMinutes(25), 30L, 2L, "Comment->Comment->Post2", "User 2", 20L, ModelCommons.UNDEFINED_LONG);
		Comment comment4 = new Comment(START.plusMinutes(30), 40L, 2L, "Comment->Comment->Post2", "User 2", 30L, ModelCommons.UNDEFINED_LONG);
		
		Post post4 = new Post(START.plusMinutes(35), 4L, 1L, "Hello World 4!", "User 1");
		Comment comment5 = new Comment(START.plusMinutes(40), 50L, 2L, "Comment->Post4", "User 2", ModelCommons.UNDEFINED_LONG, 4L);
		
		EventPostComment event1 = new EventPostComment(post1);
		EventPostComment event2 = new EventPostComment(comment1);
		EventPostComment event3 = new EventPostComment(post2);
		EventPostComment event4 = new EventPostComment(post3);
		EventPostComment event5 = new EventPostComment(comment2);
		EventPostComment event6 = new EventPostComment(comment3);
		EventPostComment event7 = new EventPostComment(comment4);
		EventPostComment event8 = new EventPostComment(post4);
		EventPostComment event9 = new EventPostComment(comment5);		
		
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
	
	public static final List<EventPostComment> get(final long nPosts, final long nCommentsToPost, final long nRepliesToComment) {
		List<EventPostComment> events = new ArrayList<EventPostComment>();
		
		long time = START.getSecondOfDay();
		Post post = new Post();
		Comment commentToPost = new Comment();
		Comment replyToComment = new Comment();
		EventPostComment event = new EventPostComment();
		
		for (long p = 1; p <= nPosts; p++) {
			time += 60 * 60 * 1000 * p;
			post.setTimestamp(time);
			post.setPostId(p);
			post.setUserId(1L);
			post.setPost("sample_post");
			post.setUser("user_1");
			event.setPost(post);
			event.setComment(null);
			event.setType(EventPostComment.TYPE_POST);
			events.add(event);			
			for (long ctp = 1; ctp <= nCommentsToPost; ctp++) {
				time += 60 * 1000 * ctp;
				commentToPost.setTimestamp(time);
				commentToPost.setCommentId(ctp);
				commentToPost.setUserId(2L);
				commentToPost.setComment("comment_to_post_" + p);
				commentToPost.setUser("user_2");
				commentToPost.setCommentRepliedId(null);
				commentToPost.setPostCommentedId(p);
				event.setPost(null);
				event.setComment(commentToPost);
				event.setType(EventPostComment.TYPE_COMMENT);
				events.add(event);	
				for (long rtc = 1; rtc <= nRepliesToComment; rtc++) {
					time += 1000 * rtc;
					replyToComment.setTimestamp(time);
					replyToComment.setCommentId(rtc);
					replyToComment.setUserId(3L);
					replyToComment.setComment("reply_to_comment_" + ctp + "_to_post" + p);
					replyToComment.setUser("user_3");
					replyToComment.setCommentRepliedId(ctp);
					replyToComment.setPostCommentedId(null);
					event.setPost(null);
					event.setComment(replyToComment);
					event.setType(EventPostComment.TYPE_COMMENT);
					events.add(event);	
				}
			}
		}		
		
		return events;
	}	

}
