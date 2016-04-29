package com.threecore.project.operator.rank.comment;

import org.apache.flink.api.common.functions.ReduceFunction;

import com.threecore.project.model.CommentRank;

public class CommentRankReducer implements ReduceFunction<CommentRank> {

	private static final long serialVersionUID = 1L;

	@Override
	public CommentRank reduce(CommentRank rank1, CommentRank rank2) throws Exception {
		if (rank1.isUpper(rank2)) {
			return rank1;
		} else if (rank2.isUpper(rank1)) {
			return rank2;
		} else {
			return CommentRank.merge(rank1, rank2);
		}
	}

}
