package com.threecore.project.model.deprecated;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.flink.api.java.tuple.Tuple7;

import com.threecore.project.model.interfaces.Chronological;
import com.threecore.project.model.interfaces.Copyable;
import com.threecore.project.model.interfaces.IScorable;
import com.threecore.project.model.interfaces.Stringable;
import com.threecore.project.tool.TimeFormat;

@Deprecated
public class OLDPostScore extends Tuple7<Long, Long, Long, Long, Long, Long, Long> 
					   implements Chronological, IScorable, Stringable, Copyable<OLDPostScore>, Comparable<OLDPostScore> {

	private static final long serialVersionUID = -4242159009842569162L;
	
	public static final String DELIMITER = ",";	
	public static final String UNDEFINED_STRING = "-";	
	public static final LocalDateTime UNDEFINED_DATE = LocalDateTime.MIN;
	public static final Long UNDEFINED_LONG = -1L;
	
	public static final OLDPostScore UNDEFINED_ENTRY = new OLDPostScore();
	
	public OLDPostScore(final LocalDateTime update_ts, 
			 final LocalDateTime post_ts, final Long post_id, final Long user_id) {
		this(update_ts, post_ts, post_id, user_id, 10L);
	}
	
	public OLDPostScore(final LocalDateTime update_ts, 
			 final LocalDateTime post_ts, final Long post_id, final Long user_id, final Long score) {
		this(update_ts, post_ts, post_id, user_id, score, 0L, null);
	}
	
	public OLDPostScore(final LocalDateTime update_ts, 
					 final LocalDateTime post_ts, final Long post_id, final Long user_id, final Long score, final Long commenters,
					 final LocalDateTime last_comment_ts) {
		super(TimeFormat.fromLocalDateTime(update_ts), TimeFormat.fromLocalDateTime(post_ts), post_id, user_id, score, commenters, (last_comment_ts == null) ? 0 : TimeFormat.fromLocalDateTime(last_comment_ts));
	}
	
	public OLDPostScore(final Long update_ts, 
			 final Long post_ts, final Long post_id, final Long user_id) {
		this(update_ts, post_ts, post_id, user_id, 10L);
	}
	
	public OLDPostScore(final Long update_ts, 
			 final Long post_ts, final Long post_id, final Long user_id, final Long score) {
		this(update_ts, post_ts, post_id, user_id, score, 0L, null);
	}
	
	public OLDPostScore(final Long update_ts, 
					 final Long post_ts, final Long post_id, final Long user_id, final Long score, final Long commenters,
					 final Long last_comment_ts) {
		super(update_ts, post_ts, post_id, user_id, score, commenters, (last_comment_ts == null) ? 0 : last_comment_ts);
	}
	
	public OLDPostScore() {}
	
	@Override
	public long getTimestamp(){
		return super.f0;
	}
	
	public Long getPostTimestamp() {
		return super.f1;
	}
	
	public Long getPostId() {
		return super.f2;
	}
	
	public Long getUserId() {
		return super.f3;
	}
	
	@Override
	public long getScore() {
		if (super.f4 == null)
			return Long.MIN_VALUE;
		return super.f4;
	}
	
	public Long getCommenters() {
		return super.f5;
	}
	
	public Long getLastCommentTimestamp() {
		return super.f6;
	}
	
	public boolean isDefined() {
		return this.getPostId() != UNDEFINED_LONG;
	}
	
	@Override
	public OLDPostScore copy() {
		final Long update_ts = this.getTimestamp();
		final Long post_ts = this.getPostTimestamp();
		final Long post_id = this.getPostId();
		final Long user_id = this.getUserId();
		final Long score = this.getScore();
		final Long commenters = this.getCommenters();
		final Long last_comment_ts = this.getLastCommentTimestamp();
		
		return new OLDPostScore(update_ts, post_ts, post_id, user_id, score, commenters, last_comment_ts);
	}
	
	@Override
	public int compareTo(OLDPostScore other) {
		if (this.getScore() == other.getScore()) {
			if (this.getPostTimestamp().equals(other.getPostTimestamp()))				
				return this.getLastCommentTimestamp().compareTo(other.getLastCommentTimestamp());
			else
				return this.getPostTimestamp().compareTo(other.getPostTimestamp());
		}
		
		return Long.compare(this.getScore(), other.getScore());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof OLDPostScore))
			return false;
		
		final OLDPostScore other = (OLDPostScore) obj;
		
		return this.getTimestamp() == other.getTimestamp() &&
				this.getPostTimestamp().equals(other.getPostTimestamp()) &&
				this.getPostId().equals(other.getPostId()) &&
				this.getUserId().equals(other.getUserId()) &&
				this.getScore() == other.getScore() &&
				this.getCommenters().equals(other.getCommenters()) &&
				this.getLastCommentTimestamp().equals(other.getLastCommentTimestamp());
	}	
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getTimestamp(), this.getPostId(), this.getScore());
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("update_ts", this.getTimestamp())
				.append("post_ts", this.getPostTimestamp())
				.append("post_id", this.getPostId())
				.append("user_id", this.getUserId())
				.append("score", this.getScore())
				.append("commenters", this.getCommenters())
				.append("last_comment_ts", this.getLastCommentTimestamp())
				.toString();
	}
	
	@Override
	public String asString() {
		return new StringJoiner(OLDPostScore.DELIMITER)
				.add(TimeFormat.stringWithoutOffset(this.getTimestamp()))
				.add(TimeFormat.stringWithoutOffset(this.getPostTimestamp()))
				.add(String.valueOf(this.getPostId()))
				.add(String.valueOf(this.getUserId()))
				.add(String.valueOf(this.getScore()))
				.add(String.valueOf(this.getCommenters()))
				.add((this.getLastCommentTimestamp() == null || this.getLastCommentTimestamp() == 0) ? OLDPostScore.UNDEFINED_STRING : TimeFormat.stringWithoutOffset(this.getLastCommentTimestamp()))
				.toString();
	}
	
	public static OLDPostScore fromString(final String line) {
		final String array[] = line.split("[" + OLDPostScore.DELIMITER + "]", -1);
		return OLDPostScore.fromArray(array);
	}
	
	public static OLDPostScore fromArray(final String array[]) {
		assert (array.length == 7) : "PostScore array must have a size of 7.";
		
		final Long update_ts = TimeFormat.parseLongWithOffset(array[0]);
		final Long post_ts = TimeFormat.parseLongWithOffset(array[1]);
		final Long post_id = Long.parseLong(array[2]);
		final Long user_id = Long.parseLong(array[3]);
		final Long score = Long.parseLong(array[4]);
		final Long commenters = Long.parseLong(array[5]);
		final Long last_comment_ts = TimeFormat.parseLongWithOffset(array[6]);
		
		return new OLDPostScore(update_ts, post_ts, post_id, user_id, score, commenters, last_comment_ts);
	}
	
}
