package com.threecore.project.model;

import java.util.StringJoiner;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.flink.api.java.tuple.Tuple4;
import org.joda.time.DateTime;

import com.threecore.project.model.type.Keyable;
import com.threecore.project.model.type.Rankable;
import com.threecore.project.model.type.Stringable;
import com.threecore.project.tool.JodaTimeTool;

public class CommentScore extends Tuple4<Long, Long, String, Long> implements Rankable<CommentScore>, Keyable, Stringable {

	private static final long serialVersionUID = 1L;
	
	public static final CommentScore UNDEFINED_SCORE = new CommentScore();	

	public CommentScore(final DateTime update_ts, final long comment_id, final String comment, final long range) {
		super(JodaTimeTool.getMillisFromDateTime(update_ts), comment_id, comment, range);
	}	
	
	public CommentScore(final long update_ts, final long comment_id, final String comment, final long range) {
		super(update_ts, comment_id, comment, range);
	}
		
	public CommentScore() {
		super(ModelCommons.UNDEFINED_LONG, 
			ModelCommons.UNDEFINED_LONG, 
			ModelCommons.UNDEFINED_STRING, 
			ModelCommons.UNDEFINED_LONG);
	}
	
	public long getId() {
		return super.f1;
	}
	
	@Override
	public long getTimestamp(){
		return super.f0;
	}
	
	public long getCommentId() {
		return super.f1;
	}
	
	public String getComment() {
		return super.f2;
	}
	
	public long getRange() {
		return super.f3;
	}
	
	@Override
	public long getScore() {
		return super.f3;
	}
	
	public boolean isDefined() {
		return this.getTimestamp() != ModelCommons.UNDEFINED_LONG &&
				this.getCommentId() != ModelCommons.UNDEFINED_LONG &&
				this.getComment() != ModelCommons.UNDEFINED_STRING &&
				this.getScore() != ModelCommons.UNDEFINED_LONG;
	}
	
	@Override
	public CommentScore copy() {
		final long update_ts = this.getTimestamp();
		final long comment_id = this.getCommentId();
		final String comment = this.getComment();
		final long score = this.getScore();
		
		return new CommentScore(update_ts, comment_id, comment, score);
	}
	
	@Override
	public int compareTo(CommentScore other) {
		if (this.getScore() == other.getScore()) {
			if (this.getTimestamp() == other.getTimestamp()) {
				return this.getComment().compareTo(other.getComment());
			} else {
				return Long.compare(this.getTimestamp(),other.getTimestamp());
			}			
		} else {
			return Long.compare(this.getScore(), other.getScore());
		}	
	}

	public boolean sameAs(CommentScore other) {
		return this.getTimestamp() == other.getTimestamp() &&
				this.getCommentId() == other.getCommentId() &&
				this.getComment() == other.getComment() &&
				this.getScore() == other.getScore();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CommentScore)) {
			return false;
		}
		
		final CommentScore other = (CommentScore) obj;
		
		return this.getCommentId() == other.getCommentId();
	}	
	
	@Override
	public int hashCode() {
		return Long.hashCode(this.getCommentId());
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("update_ts", JodaTimeTool.getStringFromMillis(this.getTimestamp()))
				.append("comment_id", this.getCommentId())
				.append("comment", this.getComment())
				.append("score", this.getScore())
				.toString();
	}
	
	@Override
	public String asString() {
		return new StringJoiner(ModelCommons.DELIMITER_OUT)
				.add((this.getCommentId() == ModelCommons.UNDEFINED_LONG) ? ModelCommons.UNDEFINED_STRING: String.valueOf(this.getCommentId()))
				.add(this.getComment())
				.add((this.getScore() == ModelCommons.UNDEFINED_LONG) ? ModelCommons.UNDEFINED_STRING : String.valueOf(this.getScore()))
				.toString();
	}
	
}
