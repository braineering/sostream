package com.threecore.project.operator.event;

import java.util.ArrayList;
import java.util.List;

import org.apache.flink.streaming.api.functions.co.CoFlatMapFunction;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.threecore.project.model.EventQueryOne;
import com.threecore.project.model.event.BucketId;
import com.threecore.project.model.event.CommentMap;
import com.threecore.project.model.event.StandardCommentMap;

public class ActiveEventPostCommentMapper3 implements CoFlatMapFunction<EventQueryOne, BucketId, EventQueryOne> {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ActiveEventPostCommentMapper3.class.getSimpleName());	
	
	private CommentMap comment2Post;
	
	private List<Long> bufferRemoval;
	
	public ActiveEventPostCommentMapper3(){
		this.comment2Post = new StandardCommentMap();
		this.bufferRemoval = new ArrayList<Long>();
	}

	@Override
	public void flatMap1(EventQueryOne event, Collector<EventQueryOne> out) throws Exception {
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
		if (this.bufferRemoval.size() > 10000) {
			for (long id : this.bufferRemoval) {
				this.comment2Post.removePost(id);
			}
			this.bufferRemoval.clear();
		}
	}

	@Override
	public void flatMap2(BucketId bucket, Collector<EventQueryOne> out) throws Exception {
		this.bufferRemoval.addAll(bucket);
		//this.comment2Post.removePost(postId);
	}	
	
}