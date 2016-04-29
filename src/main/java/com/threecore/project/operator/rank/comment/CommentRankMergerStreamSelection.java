package com.threecore.project.operator.rank.comment;

import com.threecore.project.model.rank.comment.CommentRankingStreamSelection;
import com.threecore.project.operator.rank.comment.base.AbstractCommentRankMerger;

public final class CommentRankMergerStreamSelection extends AbstractCommentRankMerger {
	
	private static final long serialVersionUID = 1L;
	
	public CommentRankMergerStreamSelection(final int rankMaxSize) {
		super.ranking = new CommentRankingStreamSelection(rankMaxSize);
	}

}
