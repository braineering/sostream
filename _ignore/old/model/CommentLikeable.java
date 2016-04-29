package com.threecore.project.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.flink.api.java.tuple.Tuple7;

import com.threecore.project.model.type.Chronological;
import com.threecore.project.model.type.Scorable;
import com.threecore.project.model.type.Stringable;
import com.threecore.project.tool.TimeFormat;

public class CommentLikeable extends Tuple7<Long, Long, Long, String, String, Long, Long> 
							 implements Chronological, Scorable, Stringable, Comparable<CommentLikeable> {
	
	private static final long serialVersionUID = -3797715749946460207L;

	public static final long INITIAL_SCORE = 10L;
	
	public static final String DELIMITER = "|";
	
	public Long score;
	
	public Set<Like> likes;
	
	public CommentLikeable(final LocalDateTime ts, final Long comment_id, final Long user_id, final String comment, final String user, final Long comment_replied_id, final Long post_commented_id) {
		this(TimeFormat.fromLocalDateTime(ts), comment_id, user_id, comment, user, comment_replied_id, post_commented_id, INITIAL_SCORE);
	}
	
	public CommentLikeable(final Long ts, final Long comment_id, final Long user_id, final String comment, final String user, final Long comment_replied_id, final Long post_commented_id) {
		this(ts, comment_id, user_id, comment, user, comment_replied_id, post_commented_id, INITIAL_SCORE);
	}
	
	public CommentLikeable(final Long ts, final Long comment_id, final Long user_id, final String comment, final String user, final Long comment_replied_id, final Long post_commented_id, final Long score) {
		super(ts, comment_id, user_id, comment, user, comment_replied_id, post_commented_id);
		
		assert (ts >= 0) : "ts must be >= 0.";
		assert (comment_id >= 0) : "comment_id must be >= 0.";
		assert (user_id >= 0) : "user_id must be >= 0.";
		assert (comment != null) : "comment must be != null.";
		assert (user != null) : "user must be != null.";
		assert (comment_replied_id >= 0 || comment_replied_id == null) : "comment_replied_id must be >= 0, or == null.";
		assert (post_commented_id >= 0 || post_commented_id == null) : "post_commented_id must be >= 0, or == null.";		
		assert (comment_replied_id >= 0 && post_commented_id == null ||
				comment_replied_id == null && post_commented_id >= 0) : "comment_replied_id and post_commented_id must not be both >= 0, or == null ";
		assert (score >= 0) : "score must be >= 0.";
		
		this.score = score;
		this.likes = new HashSet<Like>();
	}
	
	public CommentLikeable() {}
	
	@Override
	public long getTimestamp(){
		return this.f0;
	}
	
	public void setTimestamp(final Long ts) {
		super.f0 = ts;
	}
	
	public Long getCommentId(){
		return this.f1;
	}
	
	public void setCommentId(final Long commentId) {
		super.f1 = commentId;
	}
	
	public Long getUserId(){
		return this.f2;
	}
	
	public void setUserId(final Long userId) {
		super.f2 = userId;
	}
	
	public String getComment(){
		return this.f3;
	}
	
	public void setComment(final String comment) {
		super.f3 = comment;
	}
	
	public String getUser(){
		return this.f4;
	}
	
	public void setUser(final String user) {
		super.f4 = user;
	}
	
	public Long getCommentRepliedId(){
		return this.f5;
	}
	
	public void setCommentRepliedId(final Long commentRepliedId) {
		super.f5 = commentRepliedId;
	}
	
	public Long getPostCommentedId(){
		return this.f6;
	}	
	
	public long getScore() {
		return this.score;
	}
	
	public boolean isReply() {
		return this.getCommentRepliedId() != null;
	}
	
	public void setPostCommentedId(final Long postCommentedId) {
		this.f6 = postCommentedId;
	}
	
	public void addLike(Like like){
		this.likes.add(like);
	}
	
	public Set<Long> getUserLikes(){
		Set<Long> userLikes = new HashSet<Long>();
		
		for(Like l : likes){
			userLikes.add(l.getUserId());
		}
		
		return userLikes;
	}
	
	@Override
	public int compareTo(final CommentLikeable other) {
		return Long.compare(this.getScore(), other.getScore());
	}
	
	@Override
	public boolean equals(Object obj) {		
		if (!(obj instanceof CommentLikeable)) 
			return false;
		
		final CommentLikeable other = (CommentLikeable) obj;
		
		return this.getCommentId().equals(other.getCommentId());
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
				.append("score", this.getScore())
				.append("likes", this.getUserLikes().toString())
				.toString();
	}
	
	@Override
	public String asString() {
		return new StringJoiner(CommentLikeable.DELIMITER)
				.add(TimeFormat.stringWithoutOffset(this.getTimestamp()))
				.add(String.valueOf(this.getCommentId()))
				.add(String.valueOf(this.getUserId()))
				.add(String.valueOf(this.getComment()))
				.add(String.valueOf(this.getCommentRepliedId()))
				.add(String.valueOf(this.getPostCommentedId()))
				.add(String.valueOf(this.getUserLikes()))
				.toString();
	}
	
	public static CommentLikeable fromString(final String line) {
		final String array[] = line.split("[" + CommentLikeable.DELIMITER + "]", -1);
		return CommentLikeable.fromArray(array);
	}
	
	public static CommentLikeable fromArray(final String[] array) {
		assert (array.length == 7) : "Comment array must have a size of 7.";
		
		final Long ts = TimeFormat.parseLongWithOffset(array[0]);
		final Long comment_id = Long.parseLong(array[1]);
		final Long user_id = Long.parseLong(array[2]);
		final String comment = array[3];
		final String user = array[4];
		
		final Long comment_replied_id = (array[5].isEmpty()) ? null : Long.parseLong(array[5]);
		final Long post_commented_id = (array[6].isEmpty()) ? null : Long.parseLong(array[6]);
		
		return new CommentLikeable(ts, comment_id, user_id, comment, user, comment_replied_id, post_commented_id);
	}
	
}