package com.threecore.project.operator.event;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import com.threecore.project.SimpleTest;

public class TestEventCommentLikeExtractor extends SimpleTest {

	@Test
	public void serialization() {
		SerializationUtils.serialize(new EventCommentLikeExtractor());
	}

}
