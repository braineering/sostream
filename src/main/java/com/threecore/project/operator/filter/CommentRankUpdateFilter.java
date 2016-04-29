package com.threecore.project.operator.filter;

import org.apache.flink.api.common.functions.FilterFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.threecore.project.model.CommentRank;
import com.threecore.project.model.rank.Ranking;

public final class CommentRankUpdateFilter implements FilterFunction<CommentRank> {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CommentRankUpdateFilter.class.getSimpleName());
	
	private CommentRank lastRank;
	
	public CommentRankUpdateFilter(final int rankMaxSize) {
		this.lastRank = new CommentRank(rankMaxSize);
	}
	
	public CommentRankUpdateFilter() {
		this.lastRank = new CommentRank(Ranking.DEFAULT_RANK_SIZE);
	}

	@Override
	public boolean filter(CommentRank rank) throws Exception {
		LOGGER.debug("RANK IN: " + rank.asString());
		if (this.lastRank.isEquivalent(rank)) {
			LOGGER.debug("RANK EQUIVALENT");
			return false;
		} else {			
			this.lastRank.copy(rank);
			LOGGER.debug("RANK OUT: " + this.lastRank.asString());
			return true;
		}
	}
	
}
