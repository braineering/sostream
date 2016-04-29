package com.threecore.project.operator.rank.post;

import org.apache.flink.util.Collector;

import com.threecore.project.model.PostRank;
import com.threecore.project.model.rank.post.PostRankingMinHeap;
import com.threecore.project.operator.rank.post.base.AbstractPostRankMerger;

public final class PostRankMergerMinHeap extends AbstractPostRankMerger {
	
	private static final long serialVersionUID = 1L;
	
	public PostRankMergerMinHeap(final int rankMaxSize) {
		super.ranking = new PostRankingMinHeap(rankMaxSize);
	}

	public PostRankMergerMinHeap() {
		super.ranking = new PostRankingMinHeap();
	}

	@Override
	protected void refreshAndCollect(Collector<PostRank> out) {
		// TODO Auto-generated method stub
		
	}

}
