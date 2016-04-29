package com.threecore.project.operator.score.post;

import com.threecore.project.model.score.post.PostScoreRepoMapStandardTotal;
import com.threecore.project.operator.score.post.base.AbstractPostScoreUpdater;

public class PostScoreMapperUpdaterTotal extends AbstractPostScoreUpdater {
	
	private static final long serialVersionUID = 1L;
	
	public PostScoreMapperUpdaterTotal() {
		super();
		super.scores = new PostScoreRepoMapStandardTotal();
	}	

}
