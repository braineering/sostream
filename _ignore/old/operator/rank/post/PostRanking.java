package com.threecore.project.model.rank.post;

import java.io.Serializable;
import java.util.List;

import com.threecore.project.model.PostRank;
import com.threecore.project.model.PostScore;
import com.threecore.project.model.type.Stringable;

@Deprecated
public interface PostRanking extends Stringable, Serializable {
	
	public long getTimestamp();
	
	public int getRankSize();
	
	public int getRankMaxSize();
	
	public int getRankOf(long postId);
	
	public PostScore getFirst();
	
	public PostScore getSecond();
	
	public PostScore getThird();
	
	public List<PostScore> getRanking();
	
	public void updateWithRank(PostRanking rank);
	
	public void updateWithScore(PostScore score);
	
	public void addRank(PostRanking rank);
	
	public void addScore(PostScore score);	
	
	public void consolidate();
	
	public boolean isSynchronized();
	
	public PostRank toPostRank();

}
