package com.threecore.project.operator.rank.post;

import com.threecore.project.model.rank.post.PostRankingStreamSelection;
import com.threecore.project.operator.rank.post.base.AbstractPostRankMerger;

public final class PostRankMergerStreamSelection extends AbstractPostRankMerger {
	
	private static final long serialVersionUID = 1L;

	public PostRankMergerStreamSelection() {
		super.ranking = new PostRankingStreamSelection();
	}

}
