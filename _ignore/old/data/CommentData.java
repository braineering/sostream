package com.threecore.project.tool.data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.threecore.project.model.Comment;

public final class CommentData {

	private static final LocalDateTime TIME = LocalDateTime.of(2016, 1, 1, 12, 0, 0);
	private static final long INTERVAL = 5;
	private static final long NUMCOMMENTS = 10;
	
	public static final List<Comment> getDefault() {
		return get(TIME, INTERVAL, NUMCOMMENTS);
	}
	
	public static final List<Comment> get(final LocalDateTime start, final long minutes, final long numcomments) {
		List<Comment> comments = new ArrayList<Comment>();
		
		for (long comment_id = 0; comment_id < numcomments; comment_id++) {
			Comment comment = new Comment(start.plusMinutes(comment_id * minutes), Long.valueOf(comment_id), Long.valueOf(comment_id * 10), "sample-comment", "username", null, Long.valueOf(comment_id * 20));
			comments.add(comment);
		}
		
		return comments;
	}
}
