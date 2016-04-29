package com.threecore.project.model;

import java.util.StringJoiner;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.flink.api.java.tuple.Tuple5;
import org.joda.time.DateTime;

import com.threecore.project.model.type.Chronological;
import com.threecore.project.model.type.Stringable;
import com.threecore.project.tool.JodaTimeTool;

public class Post extends Tuple5<Long, Long, Long, String, String> implements Chronological, Stringable, Comparable<Post> {
	
	private static final long serialVersionUID = 1L;
	
	public static final Post UNDEFINED_POST = new Post();
	
	public Post(final DateTime ts, final long post_id, final long user_id, final String post, final String user) {
		super(JodaTimeTool.getMillisFromDateTime(ts), post_id, user_id, post, user);
	}	
	
	public Post(final long ts, final long post_id, final long user_id, final String post, final String user) {
		super(ts, post_id, user_id, post, user);
	}
	
	public Post() {
		super(ModelCommons.UNDEFINED_LONG, 
			  ModelCommons.UNDEFINED_LONG, 
			  ModelCommons.UNDEFINED_LONG, 
			  ModelCommons.UNDEFINED_STRING, 
			  ModelCommons.UNDEFINED_STRING);
	}
	
	@Override
	public long getTimestamp(){
		return super.f0;
	}
	
	public void setTimestamp(final long ts) {
		super.f0 = ts;
	}
	
	public Long getPostId(){
		return super.f1;
	}
	
	public void setPostId(final long postId) {
		super.f1 = postId;
	}
	
	public Long getUserId(){
		return super.f2;
	}
	
	public void setUserId(final long userId) {
		super.f2 = userId;
	}
	
	public String getPost(){
		return super.f3;
	}
	
	public void setPost(final String post) {
		super.f3 = post;
	}
	
	public String getUser(){
		return super.f4;
	}
	
	public void setUser(final String user) {
		super.f4 = user;
	}
	
	@Override
	public int compareTo(Post other) {
		return Long.compare(this.getTimestamp(), other.getTimestamp());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Post)) {
			return false;
		}
		
		final Post other = (Post) obj;
		
		return this.getTimestamp() == other.getTimestamp() &&
				this.getPostId().equals(other.getPostId()) &&
				this.getUserId().equals(other.getUserId()) &&
				this.getPost().equals(other.getPost()) &&
				this.getUser().equals(other.getUser());
	}	
	
	@Override
	public int hashCode() {
		int result = this.getPostId() != null ? this.getPostId().hashCode() : 0;
		return result;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("ts", this.getTimestamp())
				.append("post_id", this.getPostId())
				.append("user_id", this.getUserId())
				.append("post", this.getPost())
				.append("user", this.getUser())
				.toString();
	}
	
	@Override
	public String asString() {
		return new StringJoiner(ModelCommons.DELIMITER_IN)
				.add(JodaTimeTool.getStringFromMillis(this.getTimestamp()))
				.add(String.valueOf(this.getPostId()))
				.add(String.valueOf(this.getUserId()))
				.add(String.valueOf(this.getPost()))
				.add(String.valueOf(this.getUser()))
				.toString();
	}
		
	public static Post fromString(final String line) {
		final String array[] = line.split("[" + ModelCommons.DELIMITER_IN + "]", -1);

		final long ts = JodaTimeTool.getMillisFromString(array[0]);
		final long post_id = Long.parseLong(array[1]);
		final long user_id = Long.parseLong(array[2]);
		final String post = array[3];
		final String user = array[4];
		
		return new Post(ts, post_id, user_id, post, user);
	}
	
}
