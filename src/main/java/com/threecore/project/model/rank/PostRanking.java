package com.threecore.project.model.rank;

import java.io.Serializable;

import com.threecore.project.model.PostRank;
import com.threecore.project.model.PostScore;
import com.threecore.project.model.type.Stringable;

public interface PostRanking extends Ranking<PostScore>, Stringable, Serializable {
	
	public void addPostRank(PostRank rank);
	
	public PostRank toPostRank();

}
