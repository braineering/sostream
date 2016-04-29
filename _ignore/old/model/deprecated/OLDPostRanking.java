package com.threecore.project.model.deprecated;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.flink.api.java.tuple.Tuple4;

import com.threecore.project.model.interfaces.Chronological;
import com.threecore.project.model.interfaces.Copyable;
import com.threecore.project.model.interfaces.Stringable;
import com.threecore.project.tool.TimeFormat;


@Deprecated
public class OLDPostRanking extends Tuple4<Long, OLDPostScore, OLDPostScore, OLDPostScore> 
					  implements Chronological, Stringable, Copyable<OLDPostRanking> {
	
	private static final long serialVersionUID = -8067949112246406822L;

	public static final String DELIMITER = ",";
	
	public static final Long TS_UNDEFINED = 0L;
	
	public OLDPostRanking() {
		this(TS_UNDEFINED);
	}
	
	public OLDPostRanking(final LocalDateTime ts) {
		this(TimeFormat.fromLocalDateTime(ts));
	}
	
	public OLDPostRanking(final Long ts) {		
		this(ts, OLDPostScore.UNDEFINED_ENTRY, OLDPostScore.UNDEFINED_ENTRY, OLDPostScore.UNDEFINED_ENTRY);
	}
	
	public OLDPostRanking(final LocalDateTime ts, final OLDPostScore first) {		
		this(TimeFormat.fromLocalDateTime(ts), first, OLDPostScore.UNDEFINED_ENTRY, OLDPostScore.UNDEFINED_ENTRY);
	}
	
	public OLDPostRanking(final Long ts, final OLDPostScore first) {		
		this(ts, first, OLDPostScore.UNDEFINED_ENTRY, OLDPostScore.UNDEFINED_ENTRY);
	}
	
	public OLDPostRanking(final LocalDateTime ts, final OLDPostScore first, final OLDPostScore second) {		
		this(TimeFormat.fromLocalDateTime(ts), first, second, OLDPostScore.UNDEFINED_ENTRY);
	}
	
	public OLDPostRanking(final Long ts, final OLDPostScore first, final OLDPostScore second) {		
		this(ts, first, second, OLDPostScore.UNDEFINED_ENTRY);
	}
	
	public OLDPostRanking(final LocalDateTime ts, final OLDPostScore first, final OLDPostScore second, final OLDPostScore third) {		
		this(TimeFormat.fromLocalDateTime(ts), first, second, third);
	}
	
	public OLDPostRanking(final Long ts, final OLDPostScore first, final OLDPostScore second, final OLDPostScore third) {		
		super(ts, first, second, third);
	}	
	
	@Override
	public long getTimestamp(){
		return super.f0;
	}
	
	protected void setTimestamp(final Long ts) {
		super.f0 = ts;
	}
	
	public OLDPostScore getFirst() {
		return super.f1;
	}
	
	protected void setFirst(final OLDPostScore first) {
		super.f1 = first;
	}
	
	public OLDPostScore getSecond() {
		return super.f2;
	}
	
	protected void setSecond(final OLDPostScore second) {
		super.f2 = second;
	}
	
	public OLDPostScore getThird() {
		return super.f3;
	}
	
	protected void setThird(final OLDPostScore third) {
		super.f3 = third;
	}
	
	public OLDPostScore get(final int pos) {
		assert (pos >= 1 && pos <= 3) : "pos must be >= 1 or <= 3.";
		
		if (pos == 1)
			return this.getFirst();
		else if (pos == 2)
			return this.getSecond();
		else if (pos == 3)
			return this.getThird();
		else
			return null;
	}
	
	public OLDPostRanking copy(final OLDPostRanking rank) {
		this.f0 = rank.getTimestamp();
		this.f1 = rank.getFirst();
		this.f2 = rank.getSecond();
		this.f3 = rank.getThird();
		return this;
	}	
	
	@Override
	public OLDPostRanking copy() {
		return this.touch(this.getTimestamp());
	}
	
	public OLDPostRanking touch(final Long ts) {
		return new OLDPostRanking(ts, this.getFirst(), this.getSecond(), this.getThird());
	}	
	
	public void updateWith(final OLDPostRanking other) {
		if (TimeFormat.isAfter(other.getTimestamp(), this.getTimestamp()))
			this.setTimestamp(other.getTimestamp());
		
		Set<OLDPostScore> set = new HashSet<OLDPostScore>();
		set.add(this.getFirst());
		set.add(this.getSecond());
		set.add(this.getThird());
		set.add(other.getFirst());
		set.add(other.getSecond());
		set.add(other.getThird());
		
		List<OLDPostScore> list = new ArrayList<OLDPostScore>(set);
		
		java.util.Collections.sort(list);
		java.util.Collections.reverse(list);
		
		this.setFirst((list.size() >= 1) ? list.get(0) : OLDPostScore.UNDEFINED_ENTRY);
		this.setSecond((list.size() >= 2) ? list.get(1) : OLDPostScore.UNDEFINED_ENTRY);
		this.setThird((list.size() >= 3) ? list.get(2) : OLDPostScore.UNDEFINED_ENTRY);
	}
	
	public boolean isUpper(final OLDPostRanking other) {
		if (this.getThird().isDefined())
			return this.getThird().compareTo(other.getFirst()) == 1;
		else if (this.getSecond().isDefined())
			return this.getSecond().compareTo(other.getFirst()) == 1;
		else if (this.getFirst().isDefined())
			return this.getFirst().compareTo(other.getFirst()) == 1;
		else
			return false;
	}
	
	public boolean isLower(final OLDPostRanking other) {
		if (other.getThird().isDefined())
			return this.getFirst().compareTo(other.getThird()) == -1;
		else if (other.getSecond().isDefined())
			return this.getFirst().compareTo(other.getSecond()) == -1;
		else if (other.getFirst().isDefined())
			return this.getFirst().compareTo(other.getFirst()) == -1;
		else
			return false;
	}
	
	public boolean isEquivalent(final OLDPostRanking other) {
		return this.getFirst().equals(other.getFirst()) &&
				this.getSecond().equals(other.getSecond()) &&
				this.getThird().equals(other.getThird());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof OLDPostRanking))
			return false;
		
		final OLDPostRanking other = (OLDPostRanking) obj;
		
		return this.getTimestamp() == other.getTimestamp() &&
				this.getFirst().equals(other.getFirst()) &&
				this.getSecond().equals(other.getSecond()) &&
				this.getThird().equals(other.getThird());
	}	
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getFirst(), this.getSecond(), this.getThird());
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("timestamp", this.getTimestamp())
				.append("top1", this.getFirst())
				.append("top2", this.getSecond())
				.append("top3", this.getThird())
				.toString();
	}
	
	@Override
	public String asString() {
		return new StringJoiner(OLDPostRanking.DELIMITER)
				.add(TimeFormat.stringWithoutOffset(this.getTimestamp()))
				.add(this.getFirst().asString())
				.add(this.getSecond().asString())
				.add(this.getThird().asString())
				.toString();			
	}
	
	public static OLDPostRanking fromString(final String line) {
		final String array[] = line.split("[" + OLDPostRanking.DELIMITER + "]", -1);
		return OLDPostRanking.fromArray(array);
	}
	
	public static OLDPostRanking fromArray(final String array[]) {
		assert (array.length == 13) : "PostRank array must have a size of 13.";
		
		final Long ts = TimeFormat.parseLongWithoutOffset(array[0]);
		final Long top1_post_id = (array[1].equals(OLDPostRankEntry.UNDEFINED_STRING)) ? OLDPostRankEntry.UNDEFINED_LONG : Long.parseLong(array[1]);
		final Long top1_user_id = (array[2].equals(OLDPostRankEntry.UNDEFINED_STRING)) ? OLDPostRankEntry.UNDEFINED_LONG : Long.parseLong(array[2]);
		final Long top1_score = (array[3].equals(OLDPostRankEntry.UNDEFINED_STRING)) ? OLDPostRankEntry.UNDEFINED_LONG : Long.parseLong(array[3]);
		final Long top1_commenters = (array[4].equals(OLDPostRankEntry.UNDEFINED_STRING)) ? OLDPostRankEntry.UNDEFINED_LONG : Long.parseLong(array[4]);
		final Long top2_post_id = (array[5].equals(OLDPostRankEntry.UNDEFINED_STRING)) ? OLDPostRankEntry.UNDEFINED_LONG : Long.parseLong(array[5]);
		final Long top2_user_id = (array[6].equals(OLDPostRankEntry.UNDEFINED_STRING)) ? OLDPostRankEntry.UNDEFINED_LONG : Long.parseLong(array[6]);
		final Long top2_score = (array[7].equals(OLDPostRankEntry.UNDEFINED_STRING)) ? OLDPostRankEntry.UNDEFINED_LONG : Long.parseLong(array[7]);
		final Long top2_commenters = (array[8].equals(OLDPostRankEntry.UNDEFINED_STRING)) ? OLDPostRankEntry.UNDEFINED_LONG : Long.parseLong(array[8]);
		final Long top3_post_id = (array[9].equals(OLDPostRankEntry.UNDEFINED_STRING)) ? OLDPostRankEntry.UNDEFINED_LONG : Long.parseLong(array[9]);
		final Long top3_user_id = (array[10].equals(OLDPostRankEntry.UNDEFINED_STRING)) ? OLDPostRankEntry.UNDEFINED_LONG : Long.parseLong(array[10]);
		final Long top3_score = (array[11].equals(OLDPostRankEntry.UNDEFINED_STRING)) ? OLDPostRankEntry.UNDEFINED_LONG : Long.parseLong(array[11]);
		final Long top3_commenters = (array[12].equals(OLDPostRankEntry.UNDEFINED_STRING)) ? OLDPostRankEntry.UNDEFINED_LONG : Long.parseLong(array[12]);
		
		OLDPostScore first = new OLDPostScore(top1_post_id, top1_user_id, top1_score, top1_commenters);
		OLDPostScore second = new OLDPostScore(top2_post_id, top2_user_id, top2_score, top2_commenters);
		OLDPostScore third = new OLDPostScore(top3_post_id, top3_user_id, top3_score, top3_commenters);
		
		return new OLDPostRanking(ts, first, second, third);
	}

}
