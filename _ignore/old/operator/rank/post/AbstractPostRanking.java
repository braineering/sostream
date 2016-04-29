package com.threecore.project.model.rank.post;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.threecore.project.model.PostRank;
import com.threecore.project.model.PostScore;

@Deprecated
public abstract class AbstractPostRanking implements PostRanking {

	private static final long serialVersionUID = 1L;
	
	public static final int RANK_SIZE = 3;
	
	protected long ts;
	protected int rankMaxSize;	
	protected List<PostScore> ranking;
	
	public AbstractPostRanking() {
		this.rankMaxSize = RANK_SIZE;
		this.ts = Long.MIN_VALUE;
	}
	
	public AbstractPostRanking(final PostRanking other) {		
		this.rankMaxSize = RANK_SIZE;
		this.updateWithRank(other);		
	}	
	
	@Override
	public long getTimestamp() {
		return this.ts;
	}
	
	protected void setTimestamp(final long ts) {
		this.ts = ts;
	}
	
	@Override
	public int getRankSize() {
		int size = this.ranking.size();
		return (size > this.rankMaxSize) ? this.rankMaxSize : size;
	}
	
	@Override
	public int getRankMaxSize() {
		return this.rankMaxSize;
	}
	
	@Override
	public int getRankOf(final long postId) {
		for (int pos = 0; pos < this.ranking.size(); pos++) {
			if (this.ranking.get(pos).getPostId() == postId) {
				return pos;
			}
		}
		return -1;
	}
	
	@Override
	public PostScore getFirst() {
		return (this.ranking.size() >= 1) ? this.ranking.get(0) : null;
	}
	
	protected void setFirst(final PostScore score) {
		this.ranking.add(0, score);
	}
	
	@Override
	public PostScore getSecond() {
		return (this.ranking.size() >= 2) ? this.ranking.get(1) : null;
	}
	
	protected void setSecond(final PostScore score) {
		this.ranking.add(1, score);
	}
	
	@Override
	public PostScore getThird() {
		return (this.ranking.size() >= 3) ? this.ranking.get(2) : null;
	}
	
	protected void setThird(final PostScore score) {
		this.ranking.add(2, score);
	}
	
	@Override
	public abstract List<PostScore> getRanking();
	
	@Override
	public void updateWithRank(final PostRanking rank) {
		this.addRank(rank);
		this.consolidate();
	}
	
	@Override
	public void updateWithScore(final PostScore score) {
		this.addScore(score);
		this.consolidate();
	}
	
	@Override
	public void addRank(final PostRanking rank) {
		for (PostScore score : rank.getRanking()) {
			this.addScore(score);
		}
	}
	
	@Override
	public abstract void addScore(final PostScore score);
	
	@Override
	public abstract void consolidate();
	
	@Override
	public boolean isSynchronized() {	
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
	
	@Override
	public PostRank toPostRank() {
		PostScore first = this.getFirst();
		PostScore second = this.getSecond();
		PostScore third = this.getThird();
		
		return new PostRank(this.getTimestamp(), 
				(first != null) ? first : PostScore.UNDEFINED_SCORE, 
				(second != null) ? second : PostScore.UNDEFINED_SCORE, 
				(third != null) ? third : PostScore.UNDEFINED_SCORE); 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AbstractPostRanking)) {
			return false;
		}
		
		final AbstractPostRanking other = (AbstractPostRanking) obj;
		
		return this.getFirst().equals(other.getFirst()) &&
				this.getSecond().equals(other.getSecond()) &&
				this.getThird().equals(other.getThird());
	}	
	
	@Override
	public int hashCode() {
		int result = this.getFirst() != null ? this.getFirst().hashCode() : 0;
		result = 31 * result + (this.getSecond() != null ? this.getSecond().hashCode() : 0);
		result = 31 * result + (this.getThird() != null ? this.getThird().hashCode() : 0);
		return result;
	}
	
	@Override
	public String toString() {
		ToStringBuilder string = new ToStringBuilder(this);
		List<PostScore> ranking = this.getRanking();
		
		string.append("size", ranking.size());
		string.append("max_size", this.getRankMaxSize());
		for (int pos = 0; pos <= ranking.size(); pos++) {
			string.append(String.valueOf(pos), ranking.get(pos));
		}
		
		return string.toString();
	}
	
	@Override
	public String asString() {
		return this.toPostRank().asString();	
	}	
	
	protected void touchWithTimestamp(final long ts) {
		if (ts > this.getTimestamp()) {
			this.setTimestamp(ts);
		}
	}

}
