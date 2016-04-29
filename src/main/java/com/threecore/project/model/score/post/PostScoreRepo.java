package com.threecore.project.model.score.post;

import java.io.Serializable;

import org.apache.flink.util.Collector;

import com.threecore.project.model.Comment;
import com.threecore.project.model.Post;
import com.threecore.project.model.PostScore;

public interface PostScoreRepo extends Serializable {
	
	public PostScore addPost(Post post);
	
	public PostScore addComment(Comment comment);
	
	public void update(long timestamp, Collector<PostScore> out);	
	
	public long getTimestamp();
	
	public boolean isActivePost(long postId);

	public void executeEOF(Collector<PostScore> out);

}
