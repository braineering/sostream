package com.threecore.project.operator.rank.post;

import org.apache.flink.api.common.functions.ReduceFunction;

import com.threecore.project.model.PostRank;

public class PostRankReducer implements ReduceFunction<PostRank> {

	private static final long serialVersionUID = 1L;

	@Override
	public PostRank reduce(PostRank rank1, PostRank rank2) throws Exception {
		if (rank1.isUpper(rank2)) {
			return rank1;
		} else if (rank2.isUpper(rank1)) {
			return rank2;
		} else {
			return PostRank.merge(rank1, rank2);
		}
	}

}
