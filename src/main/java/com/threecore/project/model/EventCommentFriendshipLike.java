package com.threecore.project.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.flink.api.java.tuple.Tuple4;

import com.threecore.project.model.type.Chronological;
import com.threecore.project.model.type.Stringable;

public class EventCommentFriendshipLike extends Tuple4<Comment, Friendship, Like, Integer> implements Chronological, Stringable {
	
	private static final long serialVersionUID = 1L;
	
	public static final EventCommentFriendshipLike UNDEFINE_EVENT = new EventCommentFriendshipLike();
	
	public static final EventCommentFriendshipLike EOF = eof();
	
	public static final Integer TYPE_NONE = -1;
	public static final Integer TYPE_COMMENT = 0;
	public static final Integer TYPE_FRIENDSHIP = 1;
	public static final Integer TYPE_LIKE = 2;	
	public static final int TYPE_EOF = 3;
	
	
	public EventCommentFriendshipLike(final Comment comment) {
		super(comment, Friendship.UNDEFINED_FRIENDSHIP, Like.UNDEFINED_LIKE, TYPE_COMMENT);
	}
	
	public EventCommentFriendshipLike(final Friendship friendship) {
		super(Comment.UNDEFINED_COMMENT, friendship, Like.UNDEFINED_LIKE, TYPE_FRIENDSHIP);
	}
	
	public EventCommentFriendshipLike(final Like like) {
		super(Comment.UNDEFINED_COMMENT, Friendship.UNDEFINED_FRIENDSHIP, like, TYPE_LIKE);
	}
	
	public EventCommentFriendshipLike() {
		super(Comment.UNDEFINED_COMMENT, Friendship.UNDEFINED_FRIENDSHIP, Like.UNDEFINED_LIKE, TYPE_NONE);
	}
	
	public Comment getComment() {
		return super.f0;
	}
	
	public void setComment(final Comment comment) {
		super.f0 = comment;
	}
	
	public Friendship getFriendship() {
		return super.f1;
	}
	
	public void setComment(final Friendship friendship) {
		super.f1 = friendship;
	}
	
	public Like getLike() {
		return super.f2;
	}
	
	public void setLike(final Like like) {
		super.f2 = like;
	}
	
	public int getType() {
		return super.f3;
	}
	
	public void setType(final int type) {
		super.f3 = type;
	}
	
	public boolean isComment() {
		return super.f3 == TYPE_COMMENT;
	}
	
	public boolean isFriendship() {
		return super.f3 == TYPE_FRIENDSHIP;
	}
	
	public boolean isLike() {
		return super.f3 == TYPE_LIKE;
	}	
	
	public boolean isEOF() {
		return super.f3 == TYPE_EOF;
	}
	
	@Override
	public long getTimestamp() {
		if (this.isComment()) {
			return this.getComment().getTimestamp();
		} else if (this.isFriendship()) {
			return this.getFriendship().getTimestamp();
		} else if (this.isLike()) {
			return this.getLike().getTimestamp();
		} else if (this.isEOF()) { 
			return Long.MAX_VALUE;
		} else { 
			return -1;
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof EventCommentFriendshipLike)) {
			return false;
		}
		
		final EventCommentFriendshipLike other = (EventCommentFriendshipLike) obj;
		
		if (this.getType() != other.getType()) {
			return false;
		} else {
			if (this.isComment()) {
				return this.getComment().equals(other.getComment());
			} else if (this.isFriendship()) {
				return this.getFriendship().equals(other.getFriendship());
			} else if (this.isLike()) {
				return this.getLike().equals(other.getLike());
			} else if (this.isEOF()) {
				return true;
			} else {
				return false;
			}
		}
	}	
	
	@Override
	public int hashCode() {
		int result = this.getType();
		result = 31 * result + (this.getComment()!= null ? this.getComment().hashCode() : 0);
		result = 31 * result + (this.getFriendship()!= null ? this.getFriendship().hashCode() : 0);
		result = 31 * result + (this.getLike()!= null ? this.getLike().hashCode() : 0);
		return result;
	}
	
	@Override
	public String toString() {
		ToStringBuilder string = new ToStringBuilder(this);
		string.append("type", this.getType());
		string.append("comment", this.getComment());
		string.append("friendship", this.getFriendship());
		string.append("like", this.getLike());		
		return string.toString();
	}
	
	@Override
	public String asString() {
		if (this.isComment()) {
			return this.getComment().asString();
		} else if (this.isFriendship()) {
			return this.getFriendship().asString();
		} else if (this.isLike()) {
			return this.getLike().asString();
		} else if (this.isEOF()) {
			return "EOF";
		} else {
			return null;
		}
	}
	
	private static EventCommentFriendshipLike eof() {
		EventCommentFriendshipLike eof = new EventCommentFriendshipLike();
		eof.setType(TYPE_EOF);
		eof.f0.f0 = Long.MAX_VALUE;
		eof.f1.f0 = Long.MAX_VALUE;
		eof.f2.f0 = Long.MAX_VALUE;
		
		return eof;
	}

}
