package com.threecore.project.operator.score.post;

import java.util.ArrayList;
import java.util.List;

import org.apache.flink.streaming.api.collector.selector.OutputSelector;

import com.threecore.project.model.PostScore;

public class PostScoreSplitter implements OutputSelector<PostScore> {

	private static final long serialVersionUID = 1L;
	
	public static final String SCORE_ALL = "all";
	public static final String SCORE_INACTIVE = "inactive";

	@Override
	public Iterable<String> select(PostScore score) {
		List<String> output = new ArrayList<String>();
		
		if (score.getScore() > 0) {
			output.add(SCORE_ALL);
		} else {
			output.add(SCORE_ALL);
			output.add(SCORE_INACTIVE);
		}
        
        return output;
	}

}
