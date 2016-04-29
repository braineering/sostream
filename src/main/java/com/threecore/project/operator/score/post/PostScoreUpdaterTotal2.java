package com.threecore.project.operator.score.post;

import com.threecore.project.model.score.post.PostScoreRepoStandardTotal2;
import com.threecore.project.operator.score.post.base.AbstractPostScoreUpdaterWithTimeUpdate2;

public class PostScoreUpdaterTotal2 extends AbstractPostScoreUpdaterWithTimeUpdate2 {
	
	private static final long serialVersionUID = 1L;
	
	public PostScoreUpdaterTotal2() {
		super();
		super.scores = new PostScoreRepoStandardTotal2();
	}	
	
}
