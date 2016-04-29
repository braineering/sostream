package com.threecore.project.draft.michele.model.rank.compute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.threecore.project.draft.michele.model.CommentScore;
import com.threecore.project.draft.michele.model.rank.CommentRanking;
import com.threecore.project.draft.michele.model.score.CommentScoreMap;
import com.threecore.project.draft.michele.model.score.CommentScoreStandardHashMap;

@Deprecated
public class CommentRankingStandardHashMap extends AbstractCommentRanking implements CommentRanking {

	
	private static final long serialVersionUID = -6422712185365550062L;
	
	protected List<CommentScore> ranking;
	protected CommentScoreMap map;
	protected int k;
	
	public CommentRankingStandardHashMap(int maxRankSize) {
		this.ranking = new ArrayList<CommentScore>();
		this.map = new CommentScoreStandardHashMap();
		this.k = maxRankSize;
	}
	
	public CommentRankingStandardHashMap(final int maxRankSize, final CommentRanking other) {
		this.ranking = new ArrayList<CommentScore>(other.getRanking());
		this.k = maxRankSize;
		this.map = new CommentScoreStandardHashMap();
	}
	
	@Override
	public long getRankSize() {
		return this.ranking.size();
	}
	
	@Override
	public int getRankOf(final Long commentId) {
		for (int pos = 0; pos < this.getRankSize(); pos++) {
			if (this.ranking.get(pos).getCommentId().equals(commentId))
				return pos;
		}
		return -1;
	}

	@Override
	public List<CommentScore> getRanking() {
		List<CommentScore> copy = new ArrayList<CommentScore>();
		for (CommentScore score : this.ranking)
			copy.add(score.copy());
		return copy;
	}
	
	@Override
	public void updateWith(CommentRanking rank) {
		for (CommentScore score : rank.getRanking())
			this.updateWith(score);		
	}

	@Override
	public void updateWith(CommentScore score) {
		this.map.updateWith(score);
		for (Long commentId : this.map.getAllCommentId()) {
			CommentScore updatedScore = this.map.get(commentId);
			this.add(updatedScore);
		}
		this.add(score);
		this.consolidate();
		this.map.clean();
	}			
	
	protected void add(final CommentScore score) {
		this.touchWithTimestamp(score.getTimestamp());
		int pos = this.getRankOf(score.getCommentId());
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
		Iterator<CommentScore> iter = this.ranking.iterator();
		while(iter.hasNext()) {
			CommentScore score = iter.next();
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
