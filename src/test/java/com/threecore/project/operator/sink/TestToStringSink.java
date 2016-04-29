package com.threecore.project.operator.sink;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import com.threecore.project.model.Post;

public class TestToStringSink {

	@Test
	public void serialization() {
		SerializationUtils.serialize(new ToStringSink<Post>());
	}

}
