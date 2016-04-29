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
public class OLDPostRank extends Tuple4<Long, OLDPostRankEntry, OLDPostRankEntry, OLDPostRankEntry> 
					  implements Chronological, Stringable, Copyable<OLDPostRank> {

	private static final long serialVersionUID = -4261601273021853123L;
	
	public static final String DELIMITER = ",";
	
	public static final Long TS_UNDEFINED = 0L;
	
	public OLDPostRank() {
		this(TS_UNDEFINED);
	}
	
	public OLDPostRank(final LocalDateTime ts) {
		this(TimeFormat.fromLocalDateTime(ts));
	}
	
	public OLDPostRank(final Long ts) {		
		this(ts, OLDPostRankEntry.UNDEFINED_ENTRY, OLDPostRankEntry.UNDEFINED_ENTRY, OLDPostRankEntry.UNDEFINED_ENTRY);
	}
	
	public OLDPostRank(final LocalDateTime ts, final OLDPostRankEntry first) {		
		this(TimeFormat.fromLocalDateTime(ts), first, OLDPostRankEntry.UNDEFINED_ENTRY, OLDPostRankEntry.UNDEFINED_ENTRY);
	}
	
	public OLDPostRank(final Long ts, final OLDPostRankEntry first) {		
		this(ts, first, OLDPostRankEntry.UNDEFINED_ENTRY, OLDPostRankEntry.UNDEFINED_ENTRY);
	}
	
	public OLDPostRank(final LocalDateTime ts, final OLDPostRankEntry first, final OLDPostRankEntry second) {		
		this(TimeFormat.fromLocalDateTime(ts), first, second, OLDPostRankEntry.UNDEFINED_ENTRY);
	}
	
	public OLDPostRank(final Long ts, final OLDPostRankEntry first, final OLDPostRankEntry second) {		
		this(ts, first, second, OLDPostRankEntry.UNDEFINED_ENTRY);
	}
	
	public OLDPostRank(final LocalDateTime ts, final OLDPostRankEntry first, final OLDPostRankEntry second, final OLDPostRankEntry third) {		
		this(TimeFormat.fromLocalDateTime(ts), first, second, third);
	}
	
	public OLDPostRank(final Long ts, final OLDPostRankEntry first, final OLDPostRankEntry second, final OLDPostRankEntry third) {		
		super(ts, first, second, third);
	}	
	
	@Override
	public long getTimestamp(){
		return super.f0;
	}
	
	protected void setTimestamp(final Long ts) {
		super.f0 = ts;
	}
	
	public OLDPostRankEntry getFirst() {
		return super.f1;
	}
	
	protected void setFirst(final OLDPostRankEntry first) {
		super.f1 = first;
	}
	
	public OLDPostRankEntry getSecond() {
		return super.f2;
	}
	
	protected void setSecond(final OLDPostRankEntry second) {
		super.f2 = second;
	}
	
	public OLDPostRankEntry getThird() {
		return super.f3;
	}
	
	protected void setThird(final OLDPostRankEntry third) {
		super.f3 = third;
	}
	
	public OLDPostRankEntry get(final int pos) {
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
	
	public OLDPostRank copy(final OLDPostRank rank) {
		this.f0 = rank.getTimestamp();
		this.f1 = rank.getFirst();
		this.f2 = rank.getSecond();
		this.f3 = rank.getThird();
		return this;
	}	
	
	@Override
	public OLDPostRank copy() {
		return this.touch(this.getTimestamp());
	}
	
	public OLDPostRank touch(final Long ts) {
		return new OLDPostRank(ts, this.getFirst(), this.getSecond(), this.getThird());
	}	
	
	public void updateWith(final OLDPostRank other) {
		if (TimeFormat.isAfter(other.getTimestamp(), this.getTimestamp()))
			this.setTimestamp(other.getTimestamp());
		
		Set<OLDPostRankEntry> set = new HashSet<OLDPostRankEntry>();
		set.add(this.getFirst());
		set.add(this.getSecond());
		set.add(this.getThird());
		set.add(other.getFirst());
		set.add(other.getSecond());
		set.add(other.getThird());
		
		List<OLDPostRankEntry> list = new ArrayList<OLDPostRankEntry>(set);
		
		java.util.Collections.sort(list);
		java.util.Collections.reverse(list);
		
		this.setFirst((list.size() >= 1) ? list.get(0) : OLDPostRankEntry.UNDEFINED_ENTRY);
		this.setSecond((list.size() >= 2) ? list.get(1) : OLDPostRankEntry.UNDEFINED_ENTRY);
		this.setThird((list.size() >= 3) ? list.get(2) : OLDPostRankEntry.UNDEFINED_ENTRY);
	}
	
	public boolean isUpper(final OLDPostRank other) {
		if (this.getThird().isDefined())
			return this.getThird().compareTo(other.getFirst()) == 1;
		else if (this.getSecond().isDefined())
			return this.getSecond().compareTo(other.getFirst()) == 1;
		else if (this.getFirst().isDefined())
			return this.getFirst().compareTo(other.getFirst()) == 1;
		else
			return false;
	}
	
	public boolean isLower(final OLDPostRank other) {
		if (other.getThird().isDefined())
			return this.getFirst().compareTo(other.getThird()) == -1;
		else if (other.getSecond().isDefined())
			return this.getFirst().compareTo(other.getSecond()) == -1;
		else if (other.getFirst().isDefined())
			return this.getFirst().compareTo(other.getFirst()) == -1;
		else
			return false;
	}
	
	public boolean isEquivalent(final OLDPostRank other) {
		return this.getFirst().equals(other.getFirst()) &&
				this.getSecond().equals(other.getSecond()) &&
				this.getThird().equals(other.getThird());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof OLDPostRank))
			return false;
		
		final OLDPostRank other = (OLDPostRank) obj;
		
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
		return new StringJoiner(OLDPostRank.DELIMITER)
				.add(TimeFormat.stringWithoutOffset(this.getTimestamp()))
				.add(this.getFirst().asString())
				.add(this.getSecond().asString())
				.add(this.getThird().asString())
				.toString();			
	}
	
	public static OLDPostRank fromString(final String line) {
		final String array[] = line.split("[" + OLDPostRank.DELIMITER + "]", -1);
		return OLDPostRank.fromArray(array);
	}
	
	public static OLDPostRank fromArray(final String array[]) {
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
		
		OLDPostRankEntry first = new OLDPostRankEntry(top1_post_id, top1_user_id, top1_score, top1_commenters);
		OLDPostRankEntry second = new OLDPostRankEntry(top2_post_id, top2_user_id, top2_score, top2_commenters);
		OLDPostRankEntry third = new OLDPostRankEntry(top3_post_id, top3_user_id, top3_score, top3_commenters);
		
		return new OLDPostRank(ts, first, second, third);
	}

}
