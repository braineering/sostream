package com.threecore.project.operator.score.post;

import com.threecore.project.model.score.post.PostScoreRepoStandardTotal;
import com.threecore.project.operator.score.post.base.AbstractPostScoreUpdaterWithTimeUpdate;

public class PostScoreUpdaterTotal extends AbstractPostScoreUpdaterWithTimeUpdate {
	
	private static final long serialVersionUID = 1L;
	
	public PostScoreUpdaterTotal() {
		super();
		super.scores = new PostScoreRepoStandardTotal();
	}	
	
}
