package com.threecore.project.operator.source;

import com.threecore.project.model.Friendship;

public class FriendshipSource extends BaseFileSource<Friendship> {

	private static final long serialVersionUID = -7410760334808197676L;

	public FriendshipSource(final String dataPath) {
		super(dataPath);
	}

	@Override
	public Friendship parseElement(String line) {
		Friendship friendship = Friendship.fromString(line);
		return friendship;
	}
	
	@Override
	public void doSomethingWithElementBeforeEmitting(Friendship friendship) {
		// TODO Auto-generated method stub		
	}

	@Override
	public long getElementTimestampMillis(Friendship friendship) {
		return friendship.getTimestamp();
	}

}
