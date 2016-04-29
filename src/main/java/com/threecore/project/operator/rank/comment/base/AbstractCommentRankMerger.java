package com.threecore.project.operator.rank.comment.base;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.threecore.project.model.CommentRank;
import com.threecore.project.model.rank.CommentRanking;

public abstract class AbstractCommentRankMerger implements FlatMapFunction<CommentRank, CommentRank> {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCommentRankMerger.class.getSimpleName());
	
	protected CommentRanking ranking;	

	@Override
	public void flatMap(CommentRank rank, Collector<CommentRank> out) throws Exception {
		LOGGER.debug("RANK IN: " + rank.asString());
		if (this.needRankRefresh(rank)) {
			LOGGER.debug("RANKING REFRESH");
			this.ranking.consolidate();
			CommentRank newRank = this.ranking.toCommentRank();
			out.collect(newRank);
		}
		this.ranking.addCommentRank(rank);
	}
	
	protected final boolean needRankRefresh(final CommentRank rank) {
		if (rank.getTimestamp() > this.ranking.getTimestamp()) {
			return true;
		}
		return false;
	}

}
