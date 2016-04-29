package com.threecore.project.model;

import java.util.StringJoiner;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.flink.api.java.tuple.Tuple8;
import org.joda.time.DateTime;

import com.threecore.project.model.type.Keyable;
import com.threecore.project.model.type.Rankable;
import com.threecore.project.model.type.Stringable;
import com.threecore.project.tool.JodaTimeTool;

public class PostScore extends Tuple8<Long, Long, Long, Long, String, Long, Long, Long> implements Rankable<PostScore>, Keyable, Stringable {

	private static final long serialVersionUID = 1L;
	
	public static final PostScore UNDEFINED_SCORE = new PostScore();	
	
	public static final PostScore EOF = new PostScore(Long.MAX_VALUE, Long.MAX_VALUE, -1, -1, ModelCommons.UNDEFINED_STRING, 0,	0, ModelCommons.UNDEFINED_LONG);

	public PostScore(final DateTime update_ts, 
					 final DateTime post_ts, final long post_id, final long user_id, final String user, final long score, 
					 final long commenters, final DateTime last_comment_ts) {
		super(JodaTimeTool.getMillisFromDateTime(update_ts), JodaTimeTool.getMillisFromDateTime(post_ts), post_id, user_id, user, score, commenters, JodaTimeTool.getMillisFromDateTime(last_comment_ts));
	}	
	
	public PostScore(final DateTime update_ts, 
	 		 		 final DateTime post_ts, final long post_id, final long user_id, final String user, final long score) {
		super(JodaTimeTool.getMillisFromDateTime(update_ts), JodaTimeTool.getMillisFromDateTime(post_ts), post_id, user_id, user, score, ModelCommons.UNDEFINED_LONG, ModelCommons.UNDEFINED_LONG);
	}
	
	public PostScore(final DateTime update_ts, 
	 		 		 final DateTime post_ts, final long post_id, final long user_id, final String user) {
		super(JodaTimeTool.getMillisFromDateTime(update_ts), JodaTimeTool.getMillisFromDateTime(post_ts), post_id, user_id, user, ModelCommons.UNDEFINED_LONG, ModelCommons.UNDEFINED_LONG, ModelCommons.UNDEFINED_LONG);
	}
	
	public PostScore(final long update_ts, 
			 		 final long post_ts, final long post_id, final long user_id, final String user, final long score, 
			 		 final long commenters, final long last_comment_ts) {
		super(update_ts, post_ts, post_id, user_id, user, score, commenters, last_comment_ts);
	}
	
	public PostScore(final long update_ts, 
	 		 		 final long post_ts, final long post_id, final long user_id, final String user, final long score) {
		super(update_ts, post_ts, post_id, user_id, user, score, ModelCommons.UNDEFINED_LONG, ModelCommons.UNDEFINED_LONG);
	}
	
	public PostScore(final long update_ts, 
			 		 final long post_ts, final long post_id, final long user_id, final String user) {
		super(update_ts, post_ts, post_id, user_id, user, ModelCommons.UNDEFINED_LONG, ModelCommons.UNDEFINED_LONG, ModelCommons.UNDEFINED_LONG);
	}
		
	public PostScore() {
		super(ModelCommons.UNDEFINED_LONG, 
			ModelCommons.UNDEFINED_LONG, 
			ModelCommons.UNDEFINED_LONG, 
			ModelCommons.UNDEFINED_LONG, 
			ModelCommons.UNDEFINED_STRING, 
			ModelCommons.UNDEFINED_LONG, 
			ModelCommons.UNDEFINED_LONG, 
			ModelCommons.UNDEFINED_LONG);
	}
	
	@Override
	public long getId() {
		return super.f2;
	}
	
	@Override
	public long getTimestamp(){
		return super.f0;
	}
	
	public long getPostTimestamp() {
		return super.f1;
	}
	
	public long getPostId() {
		return super.f2;
	}
	
	public long getUserId() {
		return super.f3;
	}
	
	public String getUser() {
		return super.f4;
	}
	
	@Override
	public long getScore() {
		return super.f5;
	}
	
	public void setScore(final long score) {
		super.f5 = score;
	}
	
	public long getCommenters() {
		return super.f6;
	}
	
	public void setCommenters(final long commenters) {
		super.f6 = commenters;
	}
	
	public long getLastCommentTimestamp() {
		return super.f7;
	}
	
	public boolean isDefined() {
		return this.getTimestamp() != ModelCommons.UNDEFINED_LONG &&
				this.getUserId() != ModelCommons.UNDEFINED_LONG;
	}
	
	@Override
	public PostScore copy() {
		final long update_ts = this.getTimestamp();
		final long post_ts = this.getPostTimestamp();
		final long post_id = this.getPostId();
		final long user_id = this.getUserId();
		final String user = this.getUser();
		final long score = this.getScore();
		final long commenters = this.getCommenters();
		final long last_comment_ts = this.getLastCommentTimestamp();
		
		return new PostScore(update_ts, post_ts, post_id, user_id, user, score, commenters, last_comment_ts);
	}
	
	@Override
	public int compareTo(PostScore other) {
		if (this.getScore() == other.getScore()) {
			if (this.getPostTimestamp() == other.getPostTimestamp()) {			
				if (this.getLastCommentTimestamp() == other.getLastCommentTimestamp()) {
					return Long.compare(this.getPostId(), other.getPostId());
				} else {
					return Long.compare(this.getLastCommentTimestamp(), other.getLastCommentTimestamp());
				}				
			} else {
				return Long.compare(this.getPostTimestamp(), other.getPostTimestamp());
			}
		} else {
			return Long.compare(this.getScore(), other.getScore());
		}		
	}

	public boolean sameAs(PostScore other) {
		return this.getTimestamp() == other.getTimestamp() &&
				this.getPostTimestamp() == other.getPostTimestamp() &&
				this.getPostId() == other.getPostId() &&
				this.getUserId() == other.getUserId() &&
				this.getUser() == other.getUser() &&
				this.getScore() == other.getScore() &&
				this.getCommenters() == other.getCommenters() &&
				this.getLastCommentTimestamp() == other.getLastCommentTimestamp();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PostScore)) {
			return false;
		}
		
		final PostScore other = (PostScore) obj;
		
		return this.getPostId() == other.getPostId();
	}	
	
	@Override
	public int hashCode() {
		return Long.hashCode(this.getPostId());
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("update_ts", JodaTimeTool.getStringFromMillis(this.getTimestamp()))
				.append("post_ts", JodaTimeTool.getStringFromMillis(this.getPostTimestamp()))
				.append("post_id", this.getPostId())
				.append("user_id", this.getUserId())
				.append("user", this.getUser())
				.append("score", this.getScore())
				.append("commenters", this.getCommenters())
				.append("last_comment_ts", this.getLastCommentTimestamp())
				.toString();
	}
	
	@Override
	public String asString() {
		if (!this.isDefined()) {
			return "-,-,-,-";
		}
		return new StringJoiner(ModelCommons.DELIMITER_OUT)
				.add(String.valueOf(this.getPostId()))
				.add(this.getUser())
				.add(String.valueOf(this.getScore()))
				.add(String.valueOf(this.getCommenters()))
				.toString();
	}
	
}
