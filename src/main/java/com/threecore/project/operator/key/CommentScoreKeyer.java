package com.threecore.project.operator.key;

import org.apache.flink.api.java.functions.KeySelector;

import com.threecore.project.model.CommentScore;

public class CommentScoreKeyer implements KeySelector<CommentScore, Long> {

	private static final long serialVersionUID = 1L;

	@Override
	public Long getKey(CommentScore score) throws Exception {
		return score.getCommentId();
	}

}
