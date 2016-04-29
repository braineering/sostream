package com.threecore.project.model.rank.post;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.threecore.project.model.PostRank;
import com.threecore.project.model.PostScore;
import com.threecore.project.model.rank.Ranking;
import com.threecore.project.model.rank.base.AbstractPostRanking;
import com.threecore.project.model.score.post.comparator.PostScoreComparatorDesc;

public class PostRankingStreamSelection extends AbstractPostRanking {

	private static final long serialVersionUID = 1L;

	public PostRankingStreamSelection() {
		super(Ranking.DEFAULT_RANK_SIZE, PostScoreComparatorDesc.getInstance());
	}

	@Override
	public void consolidate() {
		// TODO Auto-generated method stub		
	}	

	@Override
	public List<PostScore> getRanking() {
		int numElements = super.elements.size();
		if (numElements == 0) return new ArrayList<PostScore>();
		int size = (super.rankMaxSize < numElements) ? super.rankMaxSize : numElements;
		return super.elements.stream().sorted(super.comparator).limit(size).collect(Collectors.toList());
	}	
	
	@Override
	public PostRank toPostRank() {
		List<PostScore> rank = this.getRanking();
		int rsize = rank.size();
		
		PostScore first = (rsize >= 1) ? rank.get(0) : PostScore.UNDEFINED_SCORE;
		PostScore second = (rsize >= 2) ? rank.get(1) : PostScore.UNDEFINED_SCORE;
		PostScore third = (rsize >= 3) ? rank.get(2) : PostScore.UNDEFINED_SCORE;
		
		return new PostRank(super.timestamp, first, second, third);
	}
}
