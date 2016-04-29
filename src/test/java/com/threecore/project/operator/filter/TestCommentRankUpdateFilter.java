package com.threecore.project.operator.filter;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

public class TestCommentRankUpdateFilter {

	@Test
	public void serialization() {
		SerializationUtils.serialize(new CommentRankUpdateFilter());
	}

}
