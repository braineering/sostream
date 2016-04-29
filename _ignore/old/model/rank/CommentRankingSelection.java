package com.threecore.project.model.rank.comment;

import java.io.Serializable;

import com.threecore.project.model.CommentRank;
import com.threecore.project.model.CommentScore;
import com.threecore.project.model.rank.CommentRanking;
import com.threecore.project.model.rank.base.AbstractRankingSelection;
import com.threecore.project.model.rank.base.Ranking;
import com.threecore.project.model.score.comment.CommentScoreComparatorDesc;

public class CommentRankingSelection extends AbstractRankingSelection<CommentScore> implements CommentRanking, Serializable {

	private static final long serialVersionUID = 1L;

	public CommentRankingSelection(final int rankMaxSize) {
		super(rankMaxSize, new CommentScoreComparatorDesc());
	}
	
	public CommentRankingSelection() {
		super(Ranking.DEFAULT_RANK_SIZE, new CommentScoreComparatorDesc());
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
		if (!(obj instanceof CommentRankingSelection)) {
			return false;
		}
		
		final CommentRankingSelection other = (CommentRankingSelection) obj;
		
		return this.getTimestamp() == other.getTimestamp() &&
				this.getRankMaxSize() == other.getRankMaxSize() &&
				this.getRanking().equals(other.getRanking());
	}	
	
	@Override
	public int hashCode() {
		int result = this.getRankMaxSize();
		result = 31 * result + (Long.hashCode(this.getTimestamp()));
		result = 31 * result + (this.getRanking().hashCode());
		return result;
	}

	@Override
	public long getLowerBoundScore() {
		// TODO Auto-generated method stub
		return 0;
	}	
	
}
