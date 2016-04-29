package com.threecore.project.model.rank.comment;

import java.util.List;
import java.util.stream.Collectors;

import com.threecore.project.model.CommentRank;
import com.threecore.project.model.CommentScore;
import com.threecore.project.model.rank.Ranking;
import com.threecore.project.model.rank.base.AbstractCommentRanking;
import com.threecore.project.model.score.comment.comparator.CommentScoreComparatorDesc;

public class CommentRankingStreamSelection extends AbstractCommentRanking {

	private static final long serialVersionUID = 1L;

	public CommentRankingStreamSelection(final int rankMaxSize) {
		super(rankMaxSize, CommentScoreComparatorDesc.getInstance());
	}
	
	public CommentRankingStreamSelection() {
		super(Ranking.DEFAULT_RANK_SIZE, CommentScoreComparatorDesc.getInstance());
	}
	
	@Override
	public void consolidate() {
		// TODO Auto-generated method stub		
	}
	
	@Override
	public List<CommentScore> getRanking() {
		int numElements = super.elements.size();
		int size = (super.rankMaxSize < numElements) ? super.rankMaxSize : numElements;
		return super.elements.stream().sorted(super.comparator).limit(size).collect(Collectors.toList());
	}	
	
	@Override
	public CommentRank toCommentRank() {
		return new CommentRank(super.rankMaxSize, super.timestamp, this.getRanking());
	}
}
