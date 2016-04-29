package com.threecore.project.operator.rank.post;

import org.apache.flink.util.Collector;

import com.threecore.project.model.PostRank;
import com.threecore.project.model.rank.post.PostRankingMinHeap;
import com.threecore.project.operator.rank.post.base.AbstractPostRanker;

public final class PostRankerMinHeap extends AbstractPostRanker {

	private static final long serialVersionUID = 1L;
	
	public PostRankerMinHeap(final int rankMaxSize) {
		super.ranking = new PostRankingMinHeap(rankMaxSize);
	}
	
	public PostRankerMinHeap() {
		super.ranking = new PostRankingMinHeap();
	}

	@Override
	protected void refreshAndCollect(Collector<PostRank> out) {
		// TODO Auto-generated method stub
		
	}
	
}