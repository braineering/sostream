package com.threecore.project.operator.rank.post;

import com.threecore.project.model.rank.post.PostRankingStreamSelection;
import com.threecore.project.operator.rank.post.base.AbstractPostRanker;

public final class PostRankerStreamSelection extends AbstractPostRanker {

	private static final long serialVersionUID = 1L;
	
	public PostRankerStreamSelection() {
		super.ranking = new PostRankingStreamSelection();
	}
	
}