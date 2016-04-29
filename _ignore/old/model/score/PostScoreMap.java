package com.threecore.project.model.score;

import java.io.Serializable;
import java.util.List;

import com.threecore.project.model.PostScore;

public interface PostScoreMap extends Serializable {
	
	public List<Long> getAllPostId();
	
	public void update(PostScore score);
	
	public PostScore get(Long postId);
	
	public void clean();

}
