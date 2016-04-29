package com.threecore.project.operator.rank.comment;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import com.threecore.project.SimpleTest;
import com.threecore.project.operator.rank.comment.CommentRankerSort;

public class TestCommentRanker extends SimpleTest {

	@Test
	public void serialization() {
		SerializationUtils.serialize(new CommentRankerSort(3));
		SerializationUtils.serialize(new CommentRankerStreamSelection(3));
	}

}
