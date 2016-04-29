package com.threecore.project.operator.rank.comment;

import com.threecore.project.model.rank.comment.CommentRankingStreamSelection;
import com.threecore.project.operator.rank.comment.base.AbstractCommentRanker;

public final class CommentRankerStreamSelection extends AbstractCommentRanker {

	private static final long serialVersionUID = 1L;

	public CommentRankerStreamSelection(final int rankMaxSize) {
		super.ranking = new CommentRankingStreamSelection(rankMaxSize);
	}
	
}