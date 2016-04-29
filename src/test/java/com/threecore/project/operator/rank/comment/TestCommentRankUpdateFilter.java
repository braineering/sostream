package com.threecore.project.operator.rank.comment;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import com.threecore.project.SimpleTest;
import com.threecore.project.operator.filter.CommentRankUpdateFilter;

public class TestCommentRankUpdateFilter extends SimpleTest {

	@Test
	public void serialization() {
		SerializationUtils.serialize(new CommentRankUpdateFilter());
	}

}
