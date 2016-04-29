package com.threecore.project.operator.rank.comment;

import com.threecore.project.model.rank.comment.CommentRankingSort;
import com.threecore.project.operator.rank.comment.base.AbstractCommentRanker;

public final class CommentRankerSort extends AbstractCommentRanker {

	private static final long serialVersionUID = 1L;

	public CommentRankerSort(final int rankMaxSize) {
		super.ranking = new CommentRankingSort(rankMaxSize);
	}
	
}