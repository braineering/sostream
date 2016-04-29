package com.threecore.project.draft.michele.model;

import java.util.Objects;
import java.util.StringJoiner;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.flink.api.java.tuple.Tuple4;

import com.threecore.project.draft.michele.model.type.Chronological2;
import com.threecore.project.model.type.Copyable;
import com.threecore.project.model.type.Scorable;
import com.threecore.project.model.type.Stringable;
import com.threecore.project.tool.TimeFormat;

@Deprecated
public class CommentScore extends Tuple4<Long, Long, String, Long> 
					   implements Chronological2, Scorable, Stringable, Copyable<CommentScore>, Comparable<CommentScore> {
	
	private static final long serialVersionUID = -5405441837438186071L;
	
	public static final String DELIMITER = ",";	
	public static final String UNDEFINED_STRING = "-";
	public static final Long UNDEFINED_LONG = -1L;
	
	public static final CommentScore UNDEFINED_ENTRY = new CommentScore();
	
	public CommentScore(final Long update_ts, final Long comment_id, final String comment, final Long score) {
		super(update_ts, comment_id, comment, score);
	}
	
	public CommentScore() {
		super(UNDEFINED_LONG, UNDEFINED_LONG, UNDEFINED_STRING, UNDEFINED_LONG);
	}
	
	@Override
	public Long getTimestamp(){
		return super.f0;
	}
	
	public Long getCommentId() {
		return super.f1;
	}
	
	public String getCommentString() {
		return super.f2;
	}
	
	@Override
	public long getScore() {
		if (super.f3 == null)
			return Long.MIN_VALUE;
		return super.f3;
	}
	
	public boolean isDefined() {
		return this.getTimestamp() != UNDEFINED_LONG &&
				this.getCommentId() != UNDEFINED_LONG &&
				this.getCommentString() != UNDEFINED_STRING &&
				this.getScore() != UNDEFINED_LONG;
	}

	@Override
	public CommentScore copy() {
		final Long update_ts = this.getTimestamp();
		final Long comment_id = this.getCommentId();
		final String comment = this.getCommentString();
		final Long score = this.getScore();
		
		return new CommentScore(update_ts, comment_id, comment, score);
	}
	
	@Override
	public int compareTo(CommentScore other) {
		if (this.getScore() == other.getScore())
				return this.getTimestamp().compareTo(other.getTimestamp());
		
		return Long.compare(this.getScore(), other.getScore());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CommentScore))
			return false;
		
		final CommentScore other = (CommentScore) obj;
		
		return this.getTimestamp() == other.getTimestamp() &&
				this.getCommentId().equals(other.getCommentId()) &&
				this.getScore() == other.getScore() &&
				this.getCommentString().equals(other.getCommentString());
	}	
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getTimestamp(), 
							this.getCommentId(),
							this.getCommentString(),
							this.getScore());
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("update_ts", this.getTimestamp())
				.append("comment_id", this.getCommentId())
				.append("comment", this.getCommentString())
				.append("score", this.getScore())
				.toString();
	}
	
	@Override
	public String asString() {
		return new StringJoiner(CommentScore.DELIMITER)
				.add((this.getCommentId().equals(UNDEFINED_LONG)) ? UNDEFINED_STRING : String.valueOf(this.getCommentId()))
				.add((this.getScore() == UNDEFINED_LONG) ? UNDEFINED_STRING : String.valueOf(this.getScore()))
				.add((this.getCommentString().equals(UNDEFINED_STRING)) ? UNDEFINED_STRING : String.valueOf(this.getCommentString()))
				.toString();
	}
	
	public static CommentScore fromString(final String line) {
		final String array[] = line.split("[" + CommentScore.DELIMITER + "]", -1);
		return CommentScore.fromArray(array);
	}
	
	public static CommentScore fromArray(final String array[]) {
		assert (array.length == 4) : "PostScore array must have a size of 7.";
		
		final Long update_ts = TimeFormat.parseLongWithOffset(array[0]);
		final Long comment_id = Long.parseLong(array[1]);
		final String comment = array[2];
		final Long score = Long.parseLong(array[3]);
		
		return new CommentScore(update_ts, comment_id, comment, score);
	}
	
}
