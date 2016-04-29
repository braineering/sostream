package com.threecore.project.model.deprecated;

import java.util.Objects;
import java.util.StringJoiner;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.flink.api.java.tuple.Tuple4;

import com.threecore.project.model.interfaces.Stringable;

@Deprecated
public class OLDPostRankEntry extends Tuple4<Long, Long, Long, Long> 
						   implements Comparable<OLDPostRankEntry>, Stringable {

	private static final long serialVersionUID = -3149802936164388759L;
	
	public static final String DELIMITER = ",";	
	public static final String UNDEFINED_STRING = "-";	
	public static final Long UNDEFINED_LONG = -1L;
	
	public static final OLDPostRankEntry UNDEFINED_ENTRY = new OLDPostRankEntry();
	
	public OLDPostRankEntry(final Long post_id, final Long user_id, final Long score, final Long commenters) {
		super(post_id, user_id, score, commenters);
	}
	
	public OLDPostRankEntry() {
		super(UNDEFINED_LONG, UNDEFINED_LONG, UNDEFINED_LONG, UNDEFINED_LONG);
	}
	
	public Long getPostId() {
		return super.f0;
	}
	
	public Long getUserId() {
		return super.f1;
	}
	
	public Long getScore() {
		return super.f2;
	}
	
	public Long getCommenters() {
		return super.f3;
	}
	
	public boolean isDefined() {
		return this.getPostId() != UNDEFINED_LONG &&
				this.getUserId() != UNDEFINED_LONG &&
				this.getScore() != UNDEFINED_LONG &&
				this.getCommenters() != UNDEFINED_LONG;
	}
	
	public OLDPostRankEntry copy(final OLDPostRankEntry entry) {
		this.f0 = entry.getPostId();
		this.f1 = entry.getUserId();
		this.f2 = entry.getScore();
		this.f3 = entry.getCommenters();
		return this;
	}
	
	public OLDPostRankEntry copy() {
		return new OLDPostRankEntry(this.getPostId(), this.getUserId(), this.getScore(), this.getCommenters());
	}
	
	@Override
	public int compareTo(OLDPostRankEntry other) {
		if (this.isDefined() && other.isDefined())
			return this.getScore().compareTo(other.getScore());		
		else if (this.isDefined() && !other.isDefined())
			return 1;
		else if (!this.isDefined() && other.isDefined())
			return -1;
		else
			return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof OLDPostRankEntry))
			return false;
		
		final OLDPostRankEntry other = (OLDPostRankEntry) obj;
		
		/*if (!this.isDefined() && !other.isDefined())
			return true;
		
		if ((this.isDefined() && !other.isDefined()) ||
				!this.isDefined() && other.isDefined())
			return false;*/
		
		return this.getPostId().equals(other.getPostId()) &&
				this.getUserId().equals(other.getUserId()) &&
				this.getScore().equals(other.getScore()) &&
				this.getCommenters().equals(other.getCommenters());
	}	
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getPostId(), this.getUserId(), this.getScore(), this.getCommenters());
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("post_id", this.getPostId())
				.append("user_id", this.getUserId())
				.append("score", this.getScore())
				.append("commenters", this.getCommenters())
				.toString();
	}
	
	@Override
	public String asString() {
		return new StringJoiner(OLDPostRankEntry.DELIMITER)
				.add((this.getPostId().equals(UNDEFINED_LONG)) ? UNDEFINED_STRING : String.valueOf(this.getPostId()))
				.add((this.getUserId().equals(UNDEFINED_LONG)) ? UNDEFINED_STRING : String.valueOf(this.getUserId()))
				.add((this.getScore().equals(UNDEFINED_LONG)) ? UNDEFINED_STRING : String.valueOf(this.getScore()))
				.add((this.getCommenters().equals(UNDEFINED_LONG)) ? UNDEFINED_STRING : String.valueOf(this.getCommenters()))
				.toString();		
	}
	
	public static OLDPostRankEntry fromString(final String line) {
		final String array[] = line.split("[" + OLDPostRankEntry.DELIMITER + "]", -1);
		return OLDPostRankEntry.fromArray(array);
	}
	
	public static OLDPostRankEntry fromArray(final String array[]) {
		assert (array.length == 4) : "PostRankEntry array must have a size of 4.";
		
		final Long post_id = (array[0].equals(UNDEFINED_STRING)) ? UNDEFINED_LONG : Long.parseLong(array[0]);
		final Long user_id = (array[1].equals(UNDEFINED_STRING)) ? UNDEFINED_LONG : Long.parseLong(array[1]);
		final Long score = (array[2].equals(UNDEFINED_STRING)) ? UNDEFINED_LONG : Long.parseLong(array[2]);
		final Long commenters = (array[3].equals(UNDEFINED_STRING)) ? UNDEFINED_LONG : Long.parseLong(array[3]);
		
		return new OLDPostRankEntry(post_id, user_id, score, commenters);
	}
	
	public static OLDPostRankEntry fromPostScore(final OLDPostScore post) {
		return new OLDPostRankEntry(post.getPostId(), post.getUserId(), post.getScore(), post.getCommenters());
	}
	
}
