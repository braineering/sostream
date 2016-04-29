package com.threecore.project.operator.rank.post.base;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.threecore.project.model.PostRank;
import com.threecore.project.model.rank.PostRanking;

public abstract class AbstractPostRankMerger implements FlatMapFunction<PostRank, PostRank> {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPostRankMerger.class.getSimpleName());
	
	protected PostRanking ranking;
	protected PostRank lastRank = PostRank.UNDEFINED_RANK;

	@Override
	public void flatMap(PostRank rank, Collector<PostRank> out) throws Exception {
		LOGGER.debug("RANK IN: " + rank.asString());
		if (this.needRankRefresh(rank)) {
			LOGGER.debug("RANKING REFRESH");
			this.ranking.consolidate();
			this.lastRank = this.ranking.toPostRank();
			out.collect(this.lastRank);
		}
		this.ranking.addPostRank(rank);
	}
	
	protected final boolean needRankRefresh(final PostRank rank) {
		if (rank.getTimestamp() > this.ranking.getTimestamp() &&
				rank.getFirst().getScore() >= this.lastRank.getThird().getScore()) {
			return true;
		}
		return false;
	}

}
