package com.threecore.project.operator.key;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import com.threecore.project.SimpleTest;
import com.threecore.project.operator.key.EventCommentFriendshipLikeKeyer;

public class TestEventCommentFriendshipLikeKeyer extends SimpleTest {

	@Test
	public void serialization() {
		SerializationUtils.serialize(new EventCommentFriendshipLikeKeyer());
	}

}
