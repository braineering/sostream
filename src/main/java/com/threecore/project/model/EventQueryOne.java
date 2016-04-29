package com.threecore.project.model;

import java.util.StringJoiner;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.flink.api.java.tuple.Tuple7;

import com.threecore.project.model.type.Chronological;
import com.threecore.project.model.type.Stringable;
import com.threecore.project.tool.JodaTimeTool;

// (event_type, timestamp, event_id, user_id, user, comment_replied_id, post_commented_id)
public class EventQueryOne extends Tuple7<Integer, Long, Long, Long, String, Long, Long> implements Chronological, Stringable {
	
	private static final long serialVersionUID = 1L;	
	
	public static final int TYPE_NONE = -1;
	public static final int TYPE_POST = 0;
	public static final int TYPE_COMMENT = 1;
	public static final int TYPE_EOF = 2;
	
	public static final EventQueryOne UNDEFINED_EVENT = new EventQueryOne();
	
	public static final EventQueryOne EOF = new EventQueryOne(TYPE_EOF, Long.MAX_VALUE, Long.MAX_VALUE, -1L, "", -1L, -1L);
	
	public EventQueryOne(final int type, final long timestamp, final long event_id, final long user_id, final String user, final long comment_replied_id, final long post_commented_id) {
		super(type, timestamp, event_id, user_id, user, comment_replied_id, post_commented_id);
	}
	
	public EventQueryOne(final int type) {
		super(type, -1L, -1L, -1L, "", -1L, -1L);
	}
	
	public EventQueryOne() {
		super(TYPE_NONE, -1L, -1L, -1L, "", -1L, -1L);
	}
	
	public boolean isPost() {
		return super.f0 == TYPE_POST;
	}
	
	public boolean isComment() {
		return super.f0 == TYPE_COMMENT;
	}	
	
	public boolean isReply() {
		return (super.f5 != -1L && super.f6 == -1L);
	}
	
	public boolean isEOF() {
		return super.f0 == TYPE_EOF;
	}

	@Override
	public long getTimestamp() {
		return super.f1;
	}
	
	public long getId() {
		return super.f2;
	}
	
	public long getUserId() {
		return super.f3;
	}
	
	public String getUser() {
		return super.f4;
	}
	
	public long getCommentRepliedId() {
		return super.f5;
	}
	
	public long getPostCommentedId() {
		return super.f6;
	}
	
	public void setPostCommentedId(final long postCommentedId) {
		super.f6 = postCommentedId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof EventQueryOne)) {
			return false;
		}
		
		final EventQueryOne other = (EventQueryOne) obj;
		
		if (super.f0 != other.f0) {
			return false;
		}
		
		return super.f2 == other.f2;
	}	
	
	@Override
	public int hashCode() {
		int result = this.f0;
		result = 31 * result + Long.hashCode(super.f2);
		return result;
	}
	
	@Override
	public String toString() {
		ToStringBuilder string = new ToStringBuilder(this);
		string.append("type", super.f0);
		string.append("timestamp", super.f1);
		string.append("event_id", super.f2);
		string.append("user_id", super.f3);
		string.append("user", super.f4);
		string.append("comment_replied_id", super.f5);
		string.append("post_commented_id", super.f6);
		return string.toString();
	}
	
	@Override
	public String asString() {
		if (super.f0 == TYPE_EOF) {
			return "EOF";
		} else if (super.f0 == TYPE_POST) {
			return new StringJoiner(ModelCommons.DELIMITER_IN)
					.add(JodaTimeTool.getStringFromMillis(super.f1))
					.add(String.valueOf(super.f2))
					.add(String.valueOf(super.f3))
					.add(String.valueOf(super.f4))
					.toString();
		} else if (super.f0 == TYPE_COMMENT) {
			return new StringJoiner(ModelCommons.DELIMITER_IN)
					.add(JodaTimeTool.getStringFromMillis(super.f1))
					.add(String.valueOf(super.f2))
					.add(String.valueOf(super.f3))
					.add(String.valueOf(super.f4))
					.add((this.isReply()) ? String.valueOf(super.f5) : "")
					.add((!this.isReply()) ? String.valueOf(super.f6) : "")
					.toString();
		}
		return null;
		
	}

}
