package com.threecore.project.operator.time;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import com.threecore.project.SimpleTest;
import com.threecore.project.operator.time.EventPostCommentTimestamper;

public class TestEventPostCommentTimestamper extends SimpleTest {

	@Test
	public void serialization() {
		SerializationUtils.serialize(new EventPostCommentTimestamper());
	}

}
