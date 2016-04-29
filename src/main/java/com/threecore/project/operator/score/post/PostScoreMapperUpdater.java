package com.threecore.project.operator.score.post;

import com.threecore.project.model.score.post.PostScoreRepoMapStandard;
import com.threecore.project.operator.score.post.base.AbstractPostScoreUpdater;

public class PostScoreMapperUpdater extends AbstractPostScoreUpdater {
	
	private static final long serialVersionUID = 1L;
	
	public PostScoreMapperUpdater() {
		super();
		super.scores = new PostScoreRepoMapStandard();		
	}	

}
