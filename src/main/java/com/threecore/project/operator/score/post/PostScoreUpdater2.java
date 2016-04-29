package com.threecore.project.operator.score.post;

import com.threecore.project.model.score.post.PostScoreRepoStandard2;
import com.threecore.project.operator.score.post.base.AbstractPostScoreUpdaterWithTimeUpdate2;

public class PostScoreUpdater2 extends AbstractPostScoreUpdaterWithTimeUpdate2 {
	
	private static final long serialVersionUID = 1L;
	
	public PostScoreUpdater2() {
		super();
		super.scores = new PostScoreRepoStandard2();
	}	

}
