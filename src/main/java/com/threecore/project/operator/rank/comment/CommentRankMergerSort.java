package com.threecore.project.operator.rank.comment;

import com.threecore.project.model.rank.comment.CommentRankingSort;
import com.threecore.project.operator.rank.comment.base.AbstractCommentRankMerger;

public final class CommentRankMergerSort extends AbstractCommentRankMerger {
	
	private static final long serialVersionUID = 1L;
	
	public CommentRankMergerSort(final int rankMaxSize) {
		super.ranking = new CommentRankingSort(rankMaxSize);
	}

}
