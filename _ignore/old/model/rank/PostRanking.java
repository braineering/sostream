package com.threecore.project.model.rank;

import java.io.Serializable;
import java.util.List;

import com.threecore.project.model.PostRank;
import com.threecore.project.model.PostScore;
import com.threecore.project.model.type.Stringable;

public interface PostRanking extends Stringable, Serializable {
	
	public long getTimestamp();
	
	public int getRankSize();
	
	public int getRankOf(Long postId);
	
	public PostScore getFirst();
	
	public PostScore getSecond();
	
	public PostScore getThird();
	
	public List<PostScore> getRanking();
	
	public void updateWithRank(PostRanking rank);
	
	public void updateWithScore(PostScore score);	
	
	public PostRank toPostRank();
	
	public boolean isSynchronized();

}
