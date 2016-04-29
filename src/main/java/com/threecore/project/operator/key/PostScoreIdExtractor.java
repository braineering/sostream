package com.threecore.project.operator.key;

import org.apache.flink.api.common.functions.MapFunction;

import com.threecore.project.model.PostScore;

public class PostScoreIdExtractor implements MapFunction<PostScore, Long> {

	private static final long serialVersionUID = 1L;

	@Override
	public Long map(PostScore score) throws Exception {
		return score.getPostId();
	}

}
