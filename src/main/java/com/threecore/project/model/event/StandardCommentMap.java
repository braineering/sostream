package com.threecore.project.model.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.threecore.project.model.Comment;
import com.threecore.project.model.Post;

public class StandardCommentMap implements CommentMap {

	private static final long serialVersionUID = 1L;
	
	private Map<Long, List<Long>> map;
	
	public StandardCommentMap() {
		this.map = new HashMap<Long, List<Long>>();
	}
	
	@Override
	public void addPost(final Post post) {
		this.map.putIfAbsent(post.getPostId(), new ArrayList<Long>());
	}

	@Override
	public void addPost(final long postId) {
		this.map.putIfAbsent(postId, new ArrayList<Long>());
	}

	@Override
	public void removePost(final long postId) {
		this.map.remove(postId);
	}
	
	@Override
	public long addComment(final Comment comment) {
		if (comment.isReply()) {
			return this.addCommentToComment(comment.getCommentId(), comment.getCommentRepliedId());
		} else {
			return this.addCommentToPost(comment.getCommentId(), comment.getPostCommentedId());
		}	
	}

	@Override
	public long addCommentToPost(final long commentId, final long postId) {
		if (this.map.containsKey(postId)) {
			this.map.get(postId).add(commentId);
			return postId;
		}		
		return -1;			
	}

	@Override
	public long addCommentToComment(final long commentId, final long commentedId) {
		for (long pId : this.map.keySet()) {
			List<Long> l = this.map.get(pId);
			if (l.contains(commentedId)) {
				l.add(commentId);
				return pId;
			}
		}
		return -1;
	}

	@Override
	public long getPostCommented(final long commentId) {
		for (long pId : this.map.keySet()) {
			List<Long> l = this.map.get(pId);
			if (l.contains(commentId)) {
				return pId;
			}
		}
		return -1;
	}
	
	@Override
	public String toString() {
		return this.map.toString();
	}

}
