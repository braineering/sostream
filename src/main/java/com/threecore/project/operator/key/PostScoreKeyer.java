package com.threecore.project.operator.key;

import org.apache.flink.api.java.functions.KeySelector;
import com.threecore.project.model.PostScore;

public class PostScoreKeyer implements KeySelector<PostScore, Long> {

	private static final long serialVersionUID = 1L;

	@Override
	public Long getKey(PostScore score) throws Exception {
		return score.getPostId();
	}

}
