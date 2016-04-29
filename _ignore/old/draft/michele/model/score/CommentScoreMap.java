package com.threecore.project.draft.michele.model.score;

import java.io.Serializable;
import java.util.List;

import com.threecore.project.draft.michele.model.CommentScore;

@Deprecated
public interface CommentScoreMap extends Serializable {
	
	public List<Long> getAllCommentId();
	
	public void updateWith(CommentScore score);
	
	public CommentScore get(Long commentId);
	
	public void clean();


}
