package com.threecore.project.model.rank.base;

import java.util.Comparator;

import com.threecore.project.model.PostRank;
import com.threecore.project.model.PostScore;
import com.threecore.project.model.rank.PostRanking;

public abstract class AbstractPostRanking extends AbstractRanking<PostScore> implements PostRanking {

	private static final long serialVersionUID = 1L;

	public AbstractPostRanking(int rankMaxSize, Comparator<PostScore> comparator) {
		super(rankMaxSize, comparator);
	}

	@Override
	public void addPostRank(PostRank rank) {
		for (PostScore score : rank.getScores()) {
			super.addElement(score);
		}
	}
	
	@Override
	public String asString() {
		return this.toPostRank().asString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AbstractPostRanking)) {
			return false;
		}
		
		final AbstractPostRanking other = (AbstractPostRanking) obj;
		
		return super.timestamp == other.getTimestamp() &&
				super.rankMaxSize == other.getRankMaxSize() &&
				super.elements.equals(other.getAllElements());
	}	
	
	@Override
	public int hashCode() {
		int result = super.rankMaxSize;
		result = 31 * result + (Long.hashCode(super.timestamp));
		result = 31 * result + (super.elements.hashCode());
		return result;
	}

}
