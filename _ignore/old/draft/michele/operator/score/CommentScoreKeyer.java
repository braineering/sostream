package com.threecore.project.draft.michele.operator.score;

import org.apache.flink.api.java.functions.KeySelector;

import com.threecore.project.draft.michele.model.CommentScore;

@Deprecated
public class CommentScoreKeyer implements KeySelector<CommentScore, Long> {

	private static final long serialVersionUID = 1L;

	@Override
	public Long getKey(CommentScore score) throws Exception {
		return score.getCommentId();
	}

}
