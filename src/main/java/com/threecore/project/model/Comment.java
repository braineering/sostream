package com.threecore.project.model;

import java.util.StringJoiner;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.flink.api.java.tuple.Tuple7;
import org.joda.time.DateTime;

import com.threecore.project.model.type.Chronological;
import com.threecore.project.model.type.Stringable;
import com.threecore.project.tool.JodaTimeTool;

public class Comment extends Tuple7<Long, Long, Long, String, String, Long, Long> 
					 implements Chronological, Stringable, Comparable<Comment> {
	
	private static final long serialVersionUID = 1L;
	
	public static final Comment UNDEFINED_COMMENT = new Comment();
	
	public Comment(final DateTime ts, final long comment_id, final long user_id, final String comment, final String user, final long comment_replied_id, final long post_commented_id) {
		super(JodaTimeTool.getMillisFromDateTime(ts), comment_id, user_id, comment, user, comment_replied_id, post_commented_id);
	}
	
	public Comment(final long ts, final long comment_id, final long user_id, final String comment, final String user, final long comment_replied_id, final long post_commented_id) {
		super(ts, comment_id, user_id, comment, user, comment_replied_id, post_commented_id);
	}
	
	public Comment() {
		super(ModelCommons.UNDEFINED_LONG, 
			  ModelCommons.UNDEFINED_LONG, 
			  ModelCommons.UNDEFINED_LONG, 
			  ModelCommons.UNDEFINED_STRING, 
			  ModelCommons.UNDEFINED_STRING, 
			  ModelCommons.UNDEFINED_LONG, 
			  ModelCommons.UNDEFINED_LONG);
	}
	
	@Override
	public long getTimestamp(){
		return super.f0;
	}
	
	public void setTimestamp(final Long ts) {
		super.f0 = ts;
	}
	
	public Long getCommentId(){
		return super.f1;
	}
	
	public void setCommentId(final Long commentId) {
		super.f1 = commentId;
	}
	
	public Long getUserId(){
		return super.f2;
	}
	
	public void setUserId(final Long userId) {
		super.f2 = userId;
	}
	
	public String getComment(){
		return super.f3;
	}
	
	public void setComment(final String comment) {
		super.f3 = comment;
	}
	
	public String getUser(){
		return super.f4;
	}
	
	public void setUser(final String user) {
		super.f4 = user;
	}
	
	public Long getCommentRepliedId(){
		return super.f5;
	}
	
	public void setCommentRepliedId(final Long commentRepliedId) {
		super.f5 = commentRepliedId;
	}
	
	public Long getPostCommentedId(){
		return super.f6;
	}	
	
	public boolean isReply() {
		return (this.getPostCommentedId() == ModelCommons.UNDEFINED_LONG && 
				this.getCommentRepliedId() != ModelCommons.UNDEFINED_LONG);
	}
	
	public void setPostCommentedId(final Long postCommentedId) {
		super.f6 = postCommentedId;
	}
	
	@Override
	public int compareTo(final Comment other) {
		return Long.compare(this.getTimestamp(), other.getTimestamp());
	}
	
	@Override
	public boolean equals(Object obj) {		
		if (!(obj instanceof Comment)) {
			return false;
		}
		
		final Comment other = (Comment) obj;
		
		return this.getTimestamp() == other.getTimestamp() &&
				this.getCommentId().equals(other.getCommentId()) &&
				this.getUserId().equals(other.getUserId()) &&
				this.getCommentRepliedId().equals(other.getCommentRepliedId()) &&
				this.getPostCommentedId().equals(other.getPostCommentedId()) &&
				this.getComment().equals(other.getComment()) &&
				this.getUser().equals(other.getUser());
	}	
	
	@Override
	public int hashCode() {
		int result = this.getCommentId() != null ? this.getCommentId().hashCode() : 0;
		return result;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("ts", this.getTimestamp())
				.append("comment_id", this.getCommentId())
				.append("user_id", this.getUserId())				
				.append("comment_replied_id", this.getCommentRepliedId())
				.append("post_commented_id", this.getPostCommentedId())
				.append("comment", this.getComment())
				.append("user", this.getUser())
				.toString();
	}
	
	@Override
	public String asString() {
		return new StringJoiner(ModelCommons.DELIMITER_IN)
				.add(JodaTimeTool.getStringFromMillis(this.getTimestamp()))
				.add(String.valueOf(this.getCommentId()))
				.add(String.valueOf(this.getUserId()))
				.add(String.valueOf(this.getComment()))
				.add(String.valueOf(this.getUser()))
				.add((this.isReply()) ? String.valueOf(this.getCommentRepliedId()) : "")
				.add((!this.isReply()) ? String.valueOf(this.getPostCommentedId()) : "")
				.toString();
	}
	
	public static Comment fromString(final String line) {
		final String array[] = line.split("[" + ModelCommons.DELIMITER_IN + "]", -1);
		
		final long ts = JodaTimeTool.getMillisFromString(array[0]);
		final long comment_id = Long.parseLong(array[1]);
		final long user_id = Long.parseLong(array[2]);
		final String comment = array[3];
		final String user = array[4];
		
		final long comment_replied_id = (array[5].isEmpty()) ? ModelCommons.UNDEFINED_LONG : Long.parseLong(array[5]);
		final long post_commented_id = (array[6].isEmpty()) ? ModelCommons.UNDEFINED_LONG : Long.parseLong(array[6]);
		
		return new Comment(ts, comment_id, user_id, comment, user, comment_replied_id, post_commented_id);
	}
	
}