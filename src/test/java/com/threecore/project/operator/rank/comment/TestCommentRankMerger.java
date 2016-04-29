package com.threecore.project.operator.rank.comment;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import com.threecore.project.SimpleTest;
import com.threecore.project.operator.rank.comment.CommentRankMergerSort;

public class TestCommentRankMerger extends SimpleTest {

	@Test
	public void serialization() {
		SerializationUtils.serialize(new CommentRankMergerSort(3));
		SerializationUtils.serialize(new CommentRankMergerStreamSelection(3));
	}

}
