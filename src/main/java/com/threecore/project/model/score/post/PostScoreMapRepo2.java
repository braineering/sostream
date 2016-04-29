package com.threecore.project.model.score.post;

import java.io.Serializable;

import org.apache.flink.util.Collector;
import com.threecore.project.model.PostScore;

public interface PostScoreMapRepo2 extends Serializable {
	
	public PostScore addPost(final long postTimestamp, final long postId, final long postUserId, final String postUser);
	
	public PostScore addCommentToPost(final long commentTimestamp, final long commentId, final long commentUserId, final long postCommentedId);
	
	public PostScore addCommentToComment(final long commentTimestamp, final long commentId, final long commentUserId, final long commentRepliedId);
	
	public void update(long timestamp, Collector<PostScore> out);	
	
	public long getTimestamp();
	
	public boolean isActivePost(long postId);
	
	public void executeEOF(Collector<PostScore> out);

}
