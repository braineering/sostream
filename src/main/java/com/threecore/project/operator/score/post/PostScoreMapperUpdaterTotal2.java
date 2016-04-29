package com.threecore.project.operator.score.post;

import com.threecore.project.model.score.post.PostScoreRepoMapStandardTotal2;
import com.threecore.project.operator.score.post.base.AbstractPostScoreMapperUpdater2;

public class PostScoreMapperUpdaterTotal2 extends AbstractPostScoreMapperUpdater2 {
	
	private static final long serialVersionUID = 1L;
	
	public PostScoreMapperUpdaterTotal2() {
		super();
		super.scores = new PostScoreRepoMapStandardTotal2();
	}	

}
