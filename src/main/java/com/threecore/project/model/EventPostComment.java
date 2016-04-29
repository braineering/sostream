package com.threecore.project.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.flink.api.java.tuple.Tuple3;

import com.threecore.project.model.type.Chronological;
import com.threecore.project.model.type.Stringable;

public class EventPostComment extends Tuple3<Post, Comment, Integer> implements Chronological, Stringable {
	
	private static final long serialVersionUID = 1L;	
	
	public static final EventPostComment UNDEFINED_EVENT = new EventPostComment();
	
	public static final EventPostComment EOF = eof();
	
	public static final int TYPE_NONE = -1;
	public static final int TYPE_POST = 0;
	public static final int TYPE_COMMENT = 1;
	public static final int TYPE_EOF = 2;
	
	public EventPostComment(final Post post, final Comment comment, final int type) {
		super(post, comment, type);
	}
	
	public EventPostComment(final Post post) {
		super(post, Comment.UNDEFINED_COMMENT, TYPE_POST);
	}
	
	public EventPostComment(final Comment comment) {
		super(Post.UNDEFINED_POST, comment, TYPE_COMMENT);
	}
	
	public EventPostComment() {
		super(Post.UNDEFINED_POST, Comment.UNDEFINED_COMMENT, TYPE_NONE);
	}
	
	public Post getPost() {
		return super.f0;
	}
	
	public void setPost(final Post post) {
		super.f0 = post;
	}
	
	public Comment getComment() {
		return super.f1;
	}
	
	public void setComment(final Comment comment) {
		super.f1 = comment;
	}
	
	public int getType() {
		return super.f2;
	}
	
	public void setType(final int type) {
		super.f2 = type;
	}
	
	public boolean isPost() {
		return super.f2 == TYPE_POST;
	}
	
	public boolean isComment() {
		return super.f2 == TYPE_COMMENT;
	}	
	
	public boolean isEOF() {
		return super.f2 == TYPE_EOF;
	}

	@Override
	public long getTimestamp() {
		if (this.isPost()) {
			return this.getPost().getTimestamp();
		} else if (this.isComment()) {
			return this.getComment().getTimestamp();
		} else if (this.isEOF()) { 
			return Long.MAX_VALUE;
		} else {
			return -1;
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof EventPostComment)) {
			return false;
		}
		
		final EventPostComment other = (EventPostComment) obj;
		
		if (this.getType() != other.getType()) {
			return false;
		} else {
			if (this.isPost()) {
				return this.getPost().equals(other.getPost());
			} else if (this.isComment()) {
				return this.getComment().equals(other.getComment());
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
		result = 31 * result + (this.getPost()!= null ? this.getPost().hashCode() : 0);
		result = 31 * result + (this.getComment()!= null ? this.getComment().hashCode() : 0);
		return result;
	}
	
	@Override
	public String toString() {
		ToStringBuilder string = new ToStringBuilder(this);
		string.append("type", this.getType());
		string.append("post", this.getPost());
		string.append("comment", this.getComment());
		return string.toString();
	}
	
	@Override
	public String asString() {
		if (this.isPost()) {
			return this.getPost().asString();
		} else if (this.isComment()) {
			return this.getComment().asString();
		} else if (this.isEOF()) {
			return "EOF";
		} else {
			return null;
		}			
	}
	
	private static EventPostComment eof() {
		EventPostComment eof = new EventPostComment();
		eof.setType(TYPE_EOF);
		eof.f0.f0 = Long.MAX_VALUE;
		eof.f1.f0 = Long.MAX_VALUE;
		
		return eof;
	}

}
