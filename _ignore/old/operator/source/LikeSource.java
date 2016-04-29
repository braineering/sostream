package com.threecore.project.operator.source;

import com.threecore.project.model.Like;

public class LikeSource extends BaseFileSource<Like> {

	private static final long serialVersionUID = -7410760334808197676L;

	public LikeSource(final String dataPath) {
		super(dataPath);
	}

	@Override
	public Like parseElement(String line) {
		Like like = Like.fromString(line);
		return like;
	}
	
	@Override
	public void doSomethingWithElementBeforeEmitting(Like like) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getElementTimestampMillis(Like like) {
		return like.getTimestamp();
	}	

}
