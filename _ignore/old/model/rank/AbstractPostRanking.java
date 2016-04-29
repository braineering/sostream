package com.threecore.project.model.rank;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.threecore.project.model.PostRank;
import com.threecore.project.model.PostScore;
import com.threecore.project.tool.TimeFormat;

public abstract class AbstractPostRanking implements PostRanking {

	private static final long serialVersionUID = 1L;
	
	public static final String DELIMITER = "|";
	
	public static final Long TS_UNDEFINED = 0L;
	
	protected int rankMaxSize;
	
	protected Long ts;
	
	public AbstractPostRanking() {
		this.rankMaxSize = 3;
		this.ts = TS_UNDEFINED;
	}
	
	public AbstractPostRanking(final PostRanking other) {
		assert (other != null) : "other must be != null.";
		
		this.rankMaxSize = 3;
		this.ts = other.getTimestamp();
	}	
	
	@Override
	public long getTimestamp() {
		return this.ts;
	}
	
	protected void setTimestamp(final Long ts) {
		this.ts = ts;
	}
	
	@Override
	public abstract int getRankSize();
	
	@Override
	public abstract int getRankOf(final Long postId);
	
	@Override
	public PostScore getFirst() {
		return this.getRanking().get(0);
	}
	
	protected void setFirst(final PostScore score) {
		this.getRanking().add(0, score);
	}
	
	@Override
	public PostScore getSecond() {
		return this.getRanking().get(1);
	}
	
	protected void setSecond(final PostScore score) {
		this.getRanking().add(1, score);
	}
	
	@Override
	public PostScore getThird() {
		return this.getRanking().get(2);
	}
	
	protected void setThird(final PostScore score) {
		this.getRanking().add(2, score);
	}
	
	@Override
	public abstract List<PostScore> getRanking();	
	
	@Override
	public abstract void updateWithRank(final PostRanking rank);
	
	@Override
	public abstract void updateWithScore(final PostScore score);	
	
	@Override
	public PostRank toPostRank() {
		PostRank rank = null;
		
		if (this.getRankSize() == 1) {
			PostScore first = this.getFirst();
			rank = new PostRank(this.getTimestamp(), first); 
		} else if (this.getRankSize() == 2) {
			PostScore first = this.getFirst();
			PostScore second = this.getSecond();
			rank = new PostRank(this.getTimestamp(), first, second); 
		} else if (this.getRankSize() >= 3) {
			PostScore first = this.getFirst();
			PostScore second = this.getSecond();
			PostScore third = this.getThird();
			rank = new PostRank(this.getTimestamp(), first, second, third); 
		} else {
			rank = new PostRank(this.getTimestamp());
		}
		
		return rank;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AbstractPostRanking))
			return false;
		
		final AbstractPostRanking other = (AbstractPostRanking) obj;
		
		return this.getFirst().equals(other.getFirst()) &&
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
				.append("first", (this.getRankSize() >= 1) ? this.getFirst() : "none")
				.append("second", (this.getRankSize() >= 2) ? this.getSecond() : "none")
				.append("third", (this.getRankSize() >= 3) ? this.getThird() : "none")
				.toString();
	}
	
	@Override
	public String asString() {
		return this.toPostRank().asString();	
	}
	
	protected void touchWithTimestamp(final long ts) {
		if (TimeFormat.isAfter(ts, this.getTimestamp()))
			this.setTimestamp(ts);
	}
	
	@Override
	public boolean isSynchronized() {	
		/*if (this.getRankSize() <= 1) {
			return true;
		} else if (this.getRankSize() == 2) {
			return (TimeFormat.daysPassed(this.getFirst().getTimestamp(), this.getTimestamp()) < 1) &&
					(TimeFormat.daysPassed(this.getSecond().getTimestamp(), this.getTimestamp()) < 1);
		} else {
			return (TimeFormat.daysPassed(this.getFirst().getTimestamp(), this.getTimestamp()) < 1) &&
					(TimeFormat.daysPassed(this.getSecond().getTimestamp(), this.getTimestamp()) < 1) &&
							(TimeFormat.daysPassed(this.getThird().getTimestamp(), this.getTimestamp()) < 1);
		}*/
		if (this.getRankSize() <= 1) {
			return true;
		} else if (this.getRankSize() == 2) {
			return (this.getFirst().getTimestamp() == this.getTimestamp() &&
					this.getSecond().getTimestamp() == this.getTimestamp());
		} else {
			return (this.getFirst().getTimestamp() == this.getTimestamp() &&
					this.getSecond().getTimestamp() == this.getTimestamp() &&
					this.getThird().getTimestamp() == this.getTimestamp());
		}
	}

}
