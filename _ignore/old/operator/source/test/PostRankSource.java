package com.threecore.project.operator.source.test;

import java.util.Collection;

import com.threecore.project.model.PostRank;

public class PostRankSource extends BaseCollectionSource<PostRank> {

	private static final long serialVersionUID = 1L;
	
	public PostRankSource(Collection<PostRank> collection, final long delay) {
		super(collection, delay);
	}

	public PostRankSource(Collection<PostRank> collection) {
		super(collection);
	}

	@Override
	public void doSomethingWithElementBeforeEmitting(PostRank element) {
		// TODO Auto-generated method stub		
	}

	@Override
	public long getElementTimestampMillis(PostRank rank) {
		return rank.getTimestamp();
	}

}
