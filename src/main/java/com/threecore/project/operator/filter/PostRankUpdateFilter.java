package com.threecore.project.operator.filter;

import org.apache.flink.api.common.functions.FilterFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.threecore.project.model.PostRank;

public final class PostRankUpdateFilter implements FilterFunction<PostRank> {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PostRankUpdateFilter.class.getSimpleName());
	
	private PostRank lastRank;
	
	public PostRankUpdateFilter() {
		this.lastRank = PostRank.UNDEFINED_RANK;
	}

	@Override
	public boolean filter(PostRank rank) throws Exception {
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
