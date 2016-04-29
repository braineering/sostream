package com.threecore.project.model.rank.post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.threecore.project.model.PostRank;
import com.threecore.project.model.PostScore;
import com.threecore.project.model.rank.Ranking;
import com.threecore.project.model.rank.base.AbstractPostRanking;
import com.threecore.project.model.score.post.comparator.PostScoreComparatorDesc;

public class PostRankingSort extends AbstractPostRanking {

	private static final long serialVersionUID = 1L;

	public PostRankingSort() {
		super(Ranking.DEFAULT_RANK_SIZE, PostScoreComparatorDesc.getInstance());
	}

	@Override
	public void consolidate() {
		//super.clean();
		Collections.sort(super.elements, super.comparator);
	}	

	@Override
	public List<PostScore> getRanking() { // ranking must be consolidated
		int numElements = super.elements.size();
		if (numElements == 0) return new ArrayList<PostScore>();
		int size = (super.rankMaxSize < numElements) ? super.rankMaxSize : numElements;
		return super.elements.subList(0, size - 1);
	}	
	
	@Override
	public PostRank toPostRank() { // ranking must be consolidated
		int numElements = super.elements.size();
		
		PostScore first = (numElements >= 1) ? super.elements.get(0) : PostScore.UNDEFINED_SCORE;
		PostScore second = (numElements >= 2) ? super.elements.get(1) : PostScore.UNDEFINED_SCORE;
		PostScore third = (numElements >= 3) ? super.elements.get(2) : PostScore.UNDEFINED_SCORE;
		
		return new PostRank(super.timestamp, first, second, third);
	}
	
}
