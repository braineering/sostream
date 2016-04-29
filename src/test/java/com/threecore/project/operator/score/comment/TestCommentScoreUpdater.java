package com.threecore.project.operator.score.comment;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import com.threecore.project.SimpleTest;
import com.threecore.project.operator.score.comment.CommentScoreUpdater;

public class TestCommentScoreUpdater extends SimpleTest {

	@Test
	public void serialization() {
		SerializationUtils.serialize(new CommentScoreUpdater());
	}

}
