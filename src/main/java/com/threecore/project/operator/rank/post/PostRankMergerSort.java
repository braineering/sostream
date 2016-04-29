package com.threecore.project.operator.rank.post;

import com.threecore.project.model.rank.post.PostRankingSort;
import com.threecore.project.operator.rank.post.base.AbstractPostRankMerger;

public final class PostRankMergerSort extends AbstractPostRankMerger {
	
	private static final long serialVersionUID = 1L;

	public PostRankMergerSort() {
		super.ranking = new PostRankingSort();
	}

}
