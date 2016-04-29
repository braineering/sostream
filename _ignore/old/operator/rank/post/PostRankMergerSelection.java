package com.threecore.project.operator.rank.post;

import org.apache.flink.util.Collector;

import com.threecore.project.model.PostRank;
import com.threecore.project.model.rank.post.PostRankingSelection;
import com.threecore.project.operator.rank.post.base.AbstractPostRankMerger;

public final class PostRankMergerSelection extends AbstractPostRankMerger {
	
	private static final long serialVersionUID = 1L;
	
	public PostRankMergerSelection(final int rankMaxSize) {
		super.ranking = new PostRankingSelection(rankMaxSize);
	}

	public PostRankMergerSelection() {
		super.ranking = new PostRankingSelection();
	}

	@Override
	protected void refreshAndCollect(Collector<PostRank> out) {
		// TODO Auto-generated method stub
		
	}

}
