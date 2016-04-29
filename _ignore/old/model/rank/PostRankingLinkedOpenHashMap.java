package com.threecore.project.model.rank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.threecore.project.model.score.PostScoreLinkedOpenHashMap;
import com.threecore.project.model.PostScore;
import com.threecore.project.model.score.PostScoreMap;

public class PostRankingLinkedOpenHashMap extends AbstractPostRanking implements PostRanking {

	private static final long serialVersionUID = 1L;
	
	protected List<PostScore> ranking;
	protected PostScoreMap map;
	
	public PostRankingLinkedOpenHashMap() {
		super();
		this.ranking = new ArrayList<PostScore>();
		this.map = new PostScoreLinkedOpenHashMap();
	}
	
	public PostRankingLinkedOpenHashMap(final PostRanking other) {
		super(other);
		this.ranking = new ArrayList<PostScore>(other.getRanking());
		this.map = new PostScoreLinkedOpenHashMap();
	}
	
	@Override
	public int getRankSize() {
		return this.ranking.size();
	}
	
	@Override
	public int getRankOf(final Long postId) {
		for (int pos = 0; pos < this.getRankSize(); pos++) {
			if (this.ranking.get(pos).getPostId().equals(postId))
				return pos;
		}
		return -1;
	}

	@Override
	public List<PostScore> getRanking() {
		List<PostScore> copy = new ArrayList<PostScore>();
		for (PostScore score : this.ranking)
			copy.add(score.copy());
		return copy;
	}
	
	@Override
	public void updateWithRank(PostRanking rank) {
		for (PostScore score : rank.getRanking())
			this.updateWithScore(score);		
	}

	@Override
	public void updateWithScore(PostScore score) {
		this.map.update(score);
		for (Long postId : this.map.getAllPostId()) {
			PostScore updatedScore = this.map.get(postId);
			this.add(updatedScore);
		}
		this.add(score);
		this.consolidate();
		this.map.clean();
	}			
	
	protected void add(final PostScore score) {
		this.touchWithTimestamp(score.getTimestamp());
		int pos = this.getRankOf(score.getPostId());
		if (pos == -1)
			this.ranking.add(score.copy());
		else
			this.ranking.set(pos, score.copy());
	}
	
	protected void consolidate() {
		this.wipeCounts();
		this.rerank();
		this.shrink();
	}	
	
	protected void wipeCounts() {
		Iterator<PostScore> iter = this.ranking.iterator();
		while(iter.hasNext()) {
			PostScore score = iter.next();
			if (score.getScore() == 0)
				iter.remove();
		}			
	}	
	
	protected void rerank() {
		Collections.sort(this.ranking);
		Collections.reverse(this.ranking);
	}	
	
	protected void shrink() {
		while (this.ranking.size() > this.rankMaxSize)
			this.ranking.remove(this.rankMaxSize);
	}	
	
}
