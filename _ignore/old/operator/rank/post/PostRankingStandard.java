package com.threecore.project.model.rank.post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.threecore.project.model.PostScore;
import com.threecore.project.model.score.post.PostScoreComparatorDesc;

@Deprecated
public class PostRankingStandard extends AbstractPostRanking implements PostRanking {

	private static final long serialVersionUID = 1L;
	
	public PostRankingStandard() {
		super();
		this.ranking = new ArrayList<PostScore>();
	}
	
	public PostRankingStandard(final PostRanking other) {
		super(other);
		this.ranking = new ArrayList<PostScore>(other.getRanking());
	}

	@Override
	public List<PostScore> getRanking() {
		List<PostScore> copy = new ArrayList<PostScore>();
		for (PostScore score : this.ranking) {
			copy.add(score.copy());
		}
		return copy;
	}		
	
	@Override
	public void addScore(final PostScore score) {
		int pos = this.getRankOf(score.getPostId());
		if (pos == -1) {
			this.ranking.add(score.copy());
		} else {
			this.ranking.set(pos, score.copy());
		}
		this.touchWithTimestamp(score.getTimestamp());
	}
	
	@Override
	public void consolidate() {
		this.clean();
		this.rerank();
	}
	
	protected void clean() {
		Iterator<PostScore> iter = this.ranking.iterator();
		while (iter.hasNext()) {
			PostScore score = iter.next();
			if (score.getScore() == 0) {
				iter.remove();
			}
		}			
	}	
	
	protected void rerank() {
		Collections.sort(this.ranking, new PostScoreComparatorDesc());
	}
	
}
