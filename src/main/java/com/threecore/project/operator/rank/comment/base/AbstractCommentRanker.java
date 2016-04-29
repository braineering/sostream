package com.threecore.project.operator.rank.comment.base;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.threecore.project.model.CommentRank;
import com.threecore.project.model.CommentScore;
import com.threecore.project.model.rank.CommentRanking;
import com.threecore.project.tool.JodaTimeTool;

public abstract class AbstractCommentRanker implements FlatMapFunction <CommentScore, CommentRank> {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCommentRanker.class.getSimpleName());
	
	protected CommentRanking ranking;	

	@Override
	public void flatMap(CommentScore score, Collector<CommentRank> out) throws Exception {
		LOGGER.debug("SCORE IN: " + score.asString());
		if (this.needRankRefresh(score)) {
			LOGGER.debug("RANKING REFRESH");
			this.ranking.consolidate();
			CommentRank newRank = this.ranking.toCommentRank();
			out.collect(newRank);
		}
		this.ranking.addElement(score);
	}
	
	protected final boolean needRankRefresh(final CommentScore score) {
		if (score.getTimestamp() > this.ranking.getTimestamp()) {
			return true;
		}
		LOGGER.debug("SKIPPED REFRESH: [SCORE-TS=" + JodaTimeTool.getStringFromMillis(score.getTimestamp()) + ";" +
				   "LOCAL-TS=" + JodaTimeTool.getStringFromMillis(score.getTimestamp()) + "]" + 
				   "[SCORE=" + score.getScore() + ";" + 
				   "LB=" + this.ranking.getLowerBoundScore() + "]");
		return false;
	}
	
}