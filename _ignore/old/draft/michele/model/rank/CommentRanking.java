package com.threecore.project.draft.michele.model.rank;

import java.io.Serializable;
import java.util.List;

import com.threecore.project.draft.michele.model.CommentRank;
import com.threecore.project.draft.michele.model.CommentScore;
import com.threecore.project.model.type.Stringable;

@Deprecated
public interface CommentRanking extends Stringable, Serializable {
	
	public long getTimestamp();
	
	public long getRankSize();
	
	public int getRankOf(Long commentId);
	
	public List<CommentScore> getRanking();
	
	public void updateWith(CommentScore score);
	
	public void updateWith(CommentRanking rank);
	
	public CommentRank toCommentRank();

	public CommentScore getCommentScoreOfPosition(int position);

}
