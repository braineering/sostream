package com.threecore.project.operator.key;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import com.threecore.project.SimpleTest;
import com.threecore.project.operator.key.EventPostCommentKeyer;

public class TestEventPostCommentKeyer extends SimpleTest {

	@Test
	public void serialization() {
		SerializationUtils.serialize(new EventPostCommentKeyer());
	}

}
