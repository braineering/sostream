package com.threecore.project.operator.rank.post;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import com.threecore.project.SimpleTest;
import com.threecore.project.operator.rank.post.PostRankerSort;

public class TestPostRanker extends SimpleTest {

	@Test
	public void serialization() {
		SerializationUtils.serialize(new PostRankerSort());
		SerializationUtils.serialize(new PostRankerStreamSelection());
	}

}
