package com.threecore.project.draft.michele.operator.rank;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.streaming.api.checkpoint.Checkpointed;

import com.threecore.project.draft.michele.model.CommentRank;
import com.threecore.project.tool.TimeFormat;

@Deprecated
public final class NewCommentRankFilter implements FilterFunction<CommentRank>, Checkpointed<CommentRank> {
	
	private static final long serialVersionUID = 1945389535037511080L;
	private CommentRank lastRank;
	
	public NewCommentRankFilter() {
		this.lastRank = new CommentRank();
	}

	@Override
	public boolean filter(CommentRank cRank) throws Exception {		
		if (TimeFormat.isBefore(cRank.getTimestamp(), this.lastRank.getTimestamp()))
			return false;		
		if (this.lastRank.isEquivalent(cRank)) {
			return false;
		} else {
			this.lastRank.copy(cRank);
			return true;
		}
	}

	@Override
	public CommentRank snapshotState(long checkpointId, long checkpointTimestamp) throws Exception {
		return this.lastRank;
	}

	@Override
	public void restoreState(CommentRank state) throws Exception {
		this.lastRank = state;
	}

}
