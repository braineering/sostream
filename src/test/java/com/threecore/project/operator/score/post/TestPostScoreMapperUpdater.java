package com.threecore.project.operator.score.post;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import com.threecore.project.SimpleTest;

public class TestPostScoreMapperUpdater extends SimpleTest {

	@Test
	public void serialization() {
		SerializationUtils.serialize(new PostScoreMapperUpdater());
	}

}
