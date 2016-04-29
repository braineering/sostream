package com.threecore.project.model.rank;

import java.io.Serializable;

import com.threecore.project.model.CommentRank;
import com.threecore.project.model.CommentScore;
import com.threecore.project.model.type.Stringable;

public interface CommentRanking extends Ranking<CommentScore>, Stringable, Serializable {
	
	public void addCommentRank(CommentRank rank);
	
	public CommentRank toCommentRank();

}
