package com.threecore.project.model.rank.comment;

import java.io.Serializable;

import com.threecore.project.model.CommentRank;
import com.threecore.project.model.CommentScore;
import com.threecore.project.model.rank.CommentRanking;
import com.threecore.project.model.rank.base.AbstractRankingMinHeap;
import com.threecore.project.model.rank.base.Ranking;
import com.threecore.project.model.score.comment.CommentScoreComparatorAsc;

public class CommentRankingMinHeap extends AbstractRankingMinHeap<CommentScore> implements CommentRanking, Serializable {

	private static final long serialVersionUID = 1L;

	public CommentRankingMinHeap(final int rankMaxSize) {
		super(rankMaxSize, new CommentScoreComparatorAsc());
	}
	
	public CommentRankingMinHeap() {
		super(Ranking.DEFAULT_RANK_SIZE, new CommentScoreComparatorAsc());
	}
	
	@Override
	public void addRank(final CommentRank rank) {
		for (CommentScore score : rank.getScores()) {
			super.addElement(score);
		}		
	}

	@Override
	public CommentRank toCommentRank() {		
		return new CommentRank(super.rankMaxSize, super.timestamp, super.sortedElements);
	}	
	
	@Override
	public String asString() {
		return this.toCommentRank().asString();	
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CommentRankingMinHeap)) {
			return false;
		}
		
		final CommentRankingMinHeap other = (CommentRankingMinHeap) obj;
		
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

	@Override
	public long getLowerBoundScore() {
		// TODO Auto-generated method stub
		return 0;
	}	
	
}
