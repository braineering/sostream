package com.threecore.project.operator.source.test;

import com.threecore.project.model.Comment;
import com.threecore.project.model.event.CommentMap;
import com.threecore.project.model.event.StandardCommentMap;

@Deprecated
public class CommentSource extends BaseFileSource<Comment> {

	private static final long serialVersionUID = 1L;
	
	private static transient volatile CommentMap map = new StandardCommentMap();

	public CommentSource(final String path) {
		super(path);
	}
	
	@Override
	public Comment parseElement(String line) {
		Comment comment = Comment.fromString(line);
		return comment;		
	}
	
	@Override
	public void doSomethingWithElementBeforeEmitting(Comment comment) {
		long postCommentedId = map.addComment(comment);
		comment.setPostCommentedId(postCommentedId);
	}

	@Override
	public long getElementTimestampMillis(Comment comment) {
		return comment.getTimestamp();
	}	

}
