package com.threecore.project.model.rank.post;

import java.io.Serializable;

import com.threecore.project.model.PostRank;
import com.threecore.project.model.PostScore;
import com.threecore.project.model.rank.PostRanking;
import com.threecore.project.model.rank.base.AbstractRankingMinHeap;
import com.threecore.project.model.rank.base.Ranking;
import com.threecore.project.model.score.post.PostScoreComparatorAsc;

public class PostRankingMinHeap extends AbstractRankingMinHeap<PostScore> implements PostRanking, Serializable {

	private static final long serialVersionUID = 1L;
	
	public PostRankingMinHeap(final int rankMaxSize) {
		super(rankMaxSize, PostScoreComparatorAsc.getInstance());
	}

	public PostRankingMinHeap() {
		super(Ranking.DEFAULT_RANK_SIZE, PostScoreComparatorAsc.getInstance());
	}
	
	@Override
	public void addPostRank(final PostRank rank) {
		for (PostScore score : rank.getScores()) {
			super.addElement(score);
		}
	}
	
	@Override
	public PostRank toPostRank() {			
		int rsize = super.sortedElements.size();
		
		PostScore first = (rsize >= 1) ? super.sortedElements.get(0) : PostScore.UNDEFINED_SCORE;
		PostScore second = (rsize >= 2) ? super.sortedElements.get(1) : PostScore.UNDEFINED_SCORE;
		PostScore third = (rsize >= 3) ? super.sortedElements.get(2) : PostScore.UNDEFINED_SCORE;
		
		return new PostRank(super.timestamp, first, second, third);
	}
	
	@Override
	public String asString() {
		return this.toPostRank().asString();	
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PostRankingMinHeap)) {
			return false;
		}
		
		final PostRankingMinHeap other = (PostRankingMinHeap) obj;
		
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
