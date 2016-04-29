package com.threecore.project.operator.score.post;

import org.apache.flink.api.common.functions.ReduceFunction;

import com.threecore.project.model.PostScore;

public class PostScoreAggregator implements ReduceFunction<PostScore> {

	private static final long serialVersionUID = 1L;

	@Override
	public PostScore reduce(PostScore score1, PostScore score2) throws Exception {
		long totScore = score1.getScore() + score2.getScore();
		long totCommenters = score1.getCommenters() + score2.getCommenters();
		score2.setScore(totScore);
		score2.setCommenters(totCommenters);
		return score2;
	}

}
