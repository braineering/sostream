package com.threecore.project.operator.rank.post;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import com.threecore.project.SimpleTest;
import com.threecore.project.operator.rank.post.PostRankMergerSort;

public class TestPostRankMerger extends SimpleTest {

	@Test
	public void serialization() {
		SerializationUtils.serialize(new PostRankMergerSort());
		SerializationUtils.serialize(new PostRankMergerStreamSelection());
	}

}
