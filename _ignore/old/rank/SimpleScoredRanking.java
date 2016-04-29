package com.threecore.project.tool.rank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.threecore.project.model.interfaces.Copyable;
import com.threecore.project.model.interfaces.IScorable;

public class SimpleScoredRanking<T extends IScorable & Comparable<T> & Copyable<T>> 
								extends AbstractScoredRanking<T> implements IRank<T> {
	
	private static final long serialVersionUID = 3436518062083693085L;
	
	protected final List<T> ranking;
	
	public SimpleScoredRanking(final int rankMaxSize) {
		super(rankMaxSize);
		this.ranking = new ArrayList<T>();
	}
	
	public SimpleScoredRanking(final SimpleScoredRanking<T> other) {
		super(other);
		this.ranking = new ArrayList<T>(other.getRanking());
	}
	
	public SimpleScoredRanking() {
		this(RANK_SIZE);
	}
	
	@Override
	public List<T> getRanking() {
		List<T> rank = new ArrayList<T>();
		for (T element : this.ranking)
			rank.add(element.copy());
		return this.ranking;
	}	
	
	@Override
	public void updateWith(final IRank<T> other) {
		for (T element : other.getRanking())
			this.updateWith(element);
	}
	
	@Override
	public void updateWith(final T element) {
		synchronized (this.ranking) {
			this.add(element);
			this.rerank();
			this.shrink();
		}
	}
	
	protected void wipeCounts() {
		int pos = 0;
		while (pos < this.ranking.size())
			if (this.ranking.get(pos).getScore() <= 0)
				this.ranking.remove(pos);
			else
				pos++;
	}		
	
	protected void add(final T element) {
		int pos = this.getRankOf(element);
		if (pos == -1)
			this.ranking.add(element);
		else
			this.ranking.set(pos, element);
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
