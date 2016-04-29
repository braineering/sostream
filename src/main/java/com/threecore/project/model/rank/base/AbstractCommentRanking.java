package com.threecore.project.model.rank.base;

import java.util.Comparator;

import com.threecore.project.model.CommentRank;
import com.threecore.project.model.CommentScore;
import com.threecore.project.model.rank.CommentRanking;

public abstract class AbstractCommentRanking extends AbstractRanking<CommentScore> implements CommentRanking {

	private static final long serialVersionUID = 1L;

	public AbstractCommentRanking(int rankMaxSize, Comparator<CommentScore> comparator) {
		super(rankMaxSize, comparator);
	}

	@Override
	public void addCommentRank(CommentRank rank) {
		for (CommentScore score : rank.getScores()) {
			super.addElement(score);
		}
	}
	
	@Override
	public String asString() {
		return this.toCommentRank().asString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AbstractCommentRanking)) {
			return false;
		}
		
		final AbstractCommentRanking other = (AbstractCommentRanking) obj;
		
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
