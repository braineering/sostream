package com.threecore.project.operator.rank.post;

import org.apache.flink.util.Collector;

import com.threecore.project.model.PostRank;
import com.threecore.project.model.rank.post.PostRankingSelection;
import com.threecore.project.operator.rank.post.base.AbstractPostRanker;

public final class PostRankerSelection extends AbstractPostRanker {

	private static final long serialVersionUID = 1L;
	
	public PostRankerSelection(final int rankMaxSize) {
		super.ranking = new PostRankingSelection(rankMaxSize);
	}
	
	public PostRankerSelection() {
		super.ranking = new PostRankingSelection();
	}

	@Override
	protected void refreshAndCollect(Collector<PostRank> out) {
		// TODO Auto-generated method stub
		
	}
	
}