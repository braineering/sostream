package com.threecore.project.model;

import java.util.StringJoiner;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.flink.api.java.tuple.Tuple3;
import org.joda.time.DateTime;

import com.threecore.project.model.type.Chronological;
import com.threecore.project.model.type.Stringable;
import com.threecore.project.tool.JodaTimeTool;

public class Like extends Tuple3<Long, Long, Long> implements Chronological, Stringable {
	
	private static final long serialVersionUID = 1L;	
	
	public static final Like UNDEFINED_LIKE = new Like();
	
	public Like(final DateTime ts, final long user_id, final long comment_id) {
		super(JodaTimeTool.getMillisFromDateTime(ts), user_id, comment_id);
	}

	public Like(final long ts, final long user_id, final long comment_id) {
		super(ts, user_id, comment_id);		
	}
	
	public Like() {
		super(ModelCommons.UNDEFINED_LONG, 
			  ModelCommons.UNDEFINED_LONG, 
			  ModelCommons.UNDEFINED_LONG);
	}
	
	@Override
	public long getTimestamp() {
		return this.f0;
	}
	
	public long getUserId(){
		return this.f1;
	}
	
	public long getCommentId(){
		return this.f2;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Like)) {
			return false;
		}
		
		final Like other = (Like) obj;
		
		return this.getUserId() == other.getUserId() &&
				this.getCommentId() == other.getCommentId();
	}	
	
	@Override
	public int hashCode() {
		int result = Long.hashCode(this.getUserId());
		result = 31 * result + Long.hashCode(this.getCommentId());
		return result;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("ts", JodaTimeTool.getStringFromMillis(this.getTimestamp()))
				.append("user_id", this.getUserId())
				.append("comment_id", this.getCommentId())
				.toString();
	}
	
	@Override
	public String asString() {
		return new StringJoiner(ModelCommons.DELIMITER_IN)
				.add(JodaTimeTool.getStringFromMillis(this.getTimestamp()))
				.add(String.valueOf(this.getUserId()))
				.add(String.valueOf(this.getCommentId()))
				.toString();
	}
	
	public static Like fromString(final String line) {
		final String array[] = line.split("[" + ModelCommons.DELIMITER_IN + "|]", -1);
		
		final long ts = JodaTimeTool.getMillisFromString(array[0]);
		final long user_id = Long.parseLong(array[1]);
		final long comment_id = Long.parseLong(array[2]);
		
		return new Like(ts, user_id, comment_id);
	}

}