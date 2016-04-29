package com.threecore.project.model.event;

import java.io.Serializable;

import com.threecore.project.model.Comment;
import com.threecore.project.model.Post;

public interface CommentMap extends Serializable {
	
	public void addPost(final Post post);
	
	public void addPost(final long postId);
	
	public void removePost(final long postId);
	
	public long addComment(final Comment comment);
	
	public long addCommentToPost(final long commentId, final long postId);
	
	public long addCommentToComment(final long commentId, final long commentedId);
	
	public long getPostCommented(final long commentId);

}
