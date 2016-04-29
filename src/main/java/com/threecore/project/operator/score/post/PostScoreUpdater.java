package com.threecore.project.operator.score.post;

import com.threecore.project.model.score.post.PostScoreRepoStandard;
import com.threecore.project.operator.score.post.base.AbstractPostScoreUpdaterWithTimeUpdate;

public class PostScoreUpdater extends AbstractPostScoreUpdaterWithTimeUpdate {
	
	private static final long serialVersionUID = 1L;
	
	public PostScoreUpdater() {
		super();
		super.scores = new PostScoreRepoStandard();
	}	

}
