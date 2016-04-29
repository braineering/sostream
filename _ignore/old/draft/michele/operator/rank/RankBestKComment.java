package com.threecore.project.draft.michele.operator.rank;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.checkpoint.Checkpointed;
import org.apache.flink.util.Collector;

import com.threecore.project.draft.michele.model.CommentRank;
import com.threecore.project.draft.michele.model.CommentScore;
import com.threecore.project.draft.michele.model.rank.CommentRanking;
import com.threecore.project.draft.michele.model.rank.compute.CommentRankingStandardHashMap;

@Deprecated
public final class RankBestKComment implements FlatMapFunction <CommentScore, CommentRank>, Checkpointed<CommentRanking> 
{
	private static final long serialVersionUID = 7532521527243691908L;
	
	private CommentRanking ranking;	
	private int k = 0;
	
	public RankBestKComment(int maxRankSize) {
		this.k = maxRankSize;
		this.ranking = new CommentRankingStandardHashMap(k);
	}

	public void flatMap(CommentScore value, Collector<CommentRank> out) throws Exception {
		this.ranking.updateWith(value);	
		/**
		 * da sistemare il toCommentRank();
		 */
		out.collect(this.ranking.toCommentRank());
	}

	@Override
	public CommentRanking snapshotState(long checkpointId, long checkpointTimestamp) throws Exception {
		return this.ranking;
	}

	@Override
	public void restoreState(CommentRanking state) throws Exception {
		this.ranking = state;		
	}
}