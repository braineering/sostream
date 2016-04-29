package com.threecore.project.model.rank.comment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.threecore.project.model.CommentRank;
import com.threecore.project.model.CommentScore;
import com.threecore.project.model.rank.Ranking;
import com.threecore.project.model.rank.base.AbstractCommentRanking;
import com.threecore.project.model.score.comment.comparator.CommentScoreComparatorDesc;

public class CommentRankingSort extends AbstractCommentRanking {

	private static final long serialVersionUID = 1L;

	public CommentRankingSort(final int rankMaxSize) {
		super(rankMaxSize, CommentScoreComparatorDesc.getInstance());
	}
	
	public CommentRankingSort() {
		super(Ranking.DEFAULT_RANK_SIZE, CommentScoreComparatorDesc.getInstance());
	}
	
	@Override
	public void consolidate() {
		//super.clean();
		Collections.sort(super.elements, super.comparator);	
	}
	
	@Override
	public List<CommentScore> getRanking() { // ranking must be consolidated
		int numElements = super.elements.size();
		if (numElements == 0) return new ArrayList<CommentScore>();
		int size = (super.rankMaxSize < numElements) ? super.rankMaxSize : numElements;
		return super.elements.subList(0, size - 1);
	}	
	
	@Override
	public CommentRank toCommentRank() { // ranking must be consolidated
		return new CommentRank(super.rankMaxSize, super.timestamp, this.getRanking());
	}
}
