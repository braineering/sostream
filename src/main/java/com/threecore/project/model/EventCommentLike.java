package com.threecore.project.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.flink.api.java.tuple.Tuple3;

import com.threecore.project.model.type.Chronological;
import com.threecore.project.model.type.Stringable;

public class EventCommentLike extends Tuple3<Comment, Like, Integer> implements Chronological, Stringable {
	
	private static final long serialVersionUID = 1L;
	
	public static final Integer TYPE_NONE = -1;
	public static final Integer TYPE_COMMENT = 0;
	public static final Integer TYPE_LIKE = 1;
	
	public static final EventCommentLike UNDEFINE_EVENT = new EventCommentLike();
	
	public EventCommentLike(final Comment comment) {
		super(comment, Like.UNDEFINED_LIKE, TYPE_COMMENT);
	}
	
	public EventCommentLike(final Like like) {
		super(Comment.UNDEFINED_COMMENT, like, TYPE_LIKE);
	}
	
	public EventCommentLike() {
		super(Comment.UNDEFINED_COMMENT, Like.UNDEFINED_LIKE, TYPE_NONE);
	}
	
	public Comment getComment() {
		return super.f0;
	}
	
	public void setComment(final Comment comment) {
		super.f0 = comment;
	}
	
	public Like getLike() {
		return super.f1;
	}
	
	public void setLike(final Like like) {
		super.f1 = like;
	}
	
	public int getType() {
		return super.f2;
	}
	
	public void setType(final int type) {
		super.f2 = type;
	}
	
	public boolean isComment() {
		return super.f2 == TYPE_COMMENT;
	}
	
	public boolean isLike() {
		return super.f2 == TYPE_LIKE;
	}	
	
	@Override
	public long getTimestamp() {
		if (this.isComment())
			return this.getComment().getTimestamp();
		else if (this.isLike())
			return this.getLike().getTimestamp();
		else 
			return -1;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof EventCommentLike)) {
			return false;
		}
		
		final EventCommentLike other = (EventCommentLike) obj;
		
		if (this.getType() != other.getType()) {
			return false;
		} else {
			if (this.isComment()) {
				return this.getComment().equals(other.getComment());
			} else if (this.isLike()) {
				return this.getLike().equals(other.getLike());
			} else {
				return false;
			}
		}
	}	
	
	@Override
	public int hashCode() {
		int result = this.getType();
		result = 31 * result + (this.getComment()!= null ? this.getComment().hashCode() : 0);
		result = 31 * result + (this.getLike()!= null ? this.getLike().hashCode() : 0);
		return result;
	}
	
	@Override
	public String toString() {
		ToStringBuilder string = new ToStringBuilder(this);
		string.append("type", this.getType());
		string.append("comment", this.getComment());
		string.append("like", this.getLike());		
		return string.toString();
	}
	
	@Override
	public String asString() {
		if (this.isComment())
			return this.getComment().asString();
		else if (this.isLike())
			return this.getLike().asString();
		else
			return null;
	}

}
