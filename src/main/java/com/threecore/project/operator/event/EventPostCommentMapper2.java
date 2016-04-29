package com.threecore.project.operator.event;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.threecore.project.model.EventQueryOne;
import com.threecore.project.model.event.CommentMap;
import com.threecore.project.model.event.StandardCommentMap;

public class EventPostCommentMapper2 implements FlatMapFunction<EventQueryOne, EventQueryOne> {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EventPostCommentMapper2.class.getSimpleName());	
	
	private CommentMap comment2Post;
	
	public EventPostCommentMapper2() {
		this.comment2Post = new StandardCommentMap();
	}

	@Override
	public void flatMap(EventQueryOne event, Collector<EventQueryOne> out) throws Exception {
		LOGGER.debug("EVENT IN: " + event.asString());
		if (event.isPost()) {
			long postId = event.getId();
			this.comment2Post.addPost(postId);
			LOGGER.debug("POST ADDED: " + postId);
			out.collect(event);
		} else if (event.isComment()){
			long commentId = event.getId();
			long postCommentedId = -1;
			if (event.isReply()) {
				postCommentedId = this.comment2Post.addCommentToComment(commentId, event.getCommentRepliedId());
			} else {
				postCommentedId = this.comment2Post.addCommentToPost(commentId, event.getPostCommentedId());
			}
			
			if (postCommentedId == -1) {
				LOGGER.debug("COMMENT IGNORED: " + commentId);
				return;
			}
			
			event.setPostCommentedId(postCommentedId);
			LOGGER.debug("COMMENT ADDED: " + commentId + " to post " + postCommentedId);
			out.collect(event);
		} else if (event.isEOF()) {
			out.collect(event);
		}
	}
	
}
