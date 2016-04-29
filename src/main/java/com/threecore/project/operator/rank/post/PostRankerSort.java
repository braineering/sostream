package com.threecore.project.operator.rank.post;

import com.threecore.project.model.rank.post.PostRankingSort;
import com.threecore.project.operator.rank.post.base.AbstractPostRanker;

public final class PostRankerSort extends AbstractPostRanker {

	private static final long serialVersionUID = 1L;
	
	public PostRankerSort() {
		super.ranking = new PostRankingSort();
	}
	
}