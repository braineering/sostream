package com.threecore.project.operator.time;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import com.threecore.project.model.Post;

public class TestTimestamper {

	@Test
	public void serialization() {
		SerializationUtils.serialize(new Timestamper<Post>());
	}

}
