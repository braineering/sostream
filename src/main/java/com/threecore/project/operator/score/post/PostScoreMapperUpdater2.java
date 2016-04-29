package com.threecore.project.operator.score.post;

import com.threecore.project.model.score.post.PostScoreRepoMapStandard2;
import com.threecore.project.operator.score.post.base.AbstractPostScoreMapperUpdater2;

public class PostScoreMapperUpdater2 extends AbstractPostScoreMapperUpdater2 {
	
	private static final long serialVersionUID = 1L;
	
	public PostScoreMapperUpdater2() {
		super();
		super.scores = new PostScoreRepoMapStandard2();		
	}	

}
