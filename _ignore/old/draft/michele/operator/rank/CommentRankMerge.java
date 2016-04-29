package com.threecore.project.draft.michele.operator.rank;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.checkpoint.Checkpointed;
import org.apache.flink.util.Collector;

import com.threecore.project.draft.michele.model.CommentRank;

@Deprecated
public final class CommentRankMerge implements FlatMapFunction<CommentRank, CommentRank>, Checkpointed<CommentRank> {


	private static final long serialVersionUID = -4944835054652485126L;
	private CommentRank lastrank;
	
	public CommentRankMerge() {
		this.lastrank = new CommentRank();
	}

	public void flatMap(CommentRank rank, Collector<CommentRank> out) throws Exception {
		this.lastrank.updateWith(rank);
		out.collect(this.lastrank);
	}

	@Override
	public CommentRank snapshotState(long checkpointId, long checkpointTimestamp) throws Exception {
		return this.lastrank;
	}

	@Override
	public void restoreState(CommentRank state) throws Exception {
		this.lastrank = state;
	}

}
