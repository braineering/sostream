package com.threecore.project.operator.rank.post.base;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.threecore.project.model.PostRank;
import com.threecore.project.model.PostScore;
import com.threecore.project.model.rank.PostRanking;
import com.threecore.project.operator.rank.post.PostRankerSort;
import com.threecore.project.tool.JodaTimeTool;

public abstract class AbstractPostRanker implements FlatMapFunction<PostScore, PostRank> {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PostRankerSort.class.getSimpleName());
	
	protected PostRanking ranking;	
	protected PostRank lastRank = PostRank.UNDEFINED_RANK;

	@Override
	public void flatMap(PostScore score, Collector<PostRank> out) throws Exception {
		LOGGER.debug("SCORE IN: " + score.asString());
		if (this.needRankRefresh(score)) {
			LOGGER.debug("RANKING REFRESH");
			this.ranking.consolidate();
			this.lastRank = this.ranking.toPostRank();
			out.collect(this.lastRank);
		}
		this.ranking.addElement(score);
	}
	
	protected final boolean needRankRefresh(final PostScore score) {
		if ((score.getPostId() == this.lastRank.getFirst().getPostId() && 
			 score.getScore() > this.lastRank.getSecond().getScore())) {
			return false;
		}
		if (score.getTimestamp() > this.ranking.getTimestamp() &&
				score.getScore() >= this.lastRank.getThird().getScore()) {
			return true;
		}
		LOGGER.debug("SKIPPED REFRESH: [SCORE-TS=" + JodaTimeTool.getStringFromMillis(score.getTimestamp()) + ";" +
									   "LOCAL-TS=" + JodaTimeTool.getStringFromMillis(score.getTimestamp()) + "]" + 
									   "[SCORE=" + score.getScore() + ";" + 
									   "LB=" + this.ranking.getLowerBoundScore() + "]");
		return false;
	}
	
}