package com.threecore.project.tool.rank.noll;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ranking implements Serializable {
	
	private static final long serialVersionUID = 3436518062083693085L;

	private static final int DEFAULT_TOP_N = 3;
	
	private final int maxSize;
	
	private final List<Rankable> ranking = new ArrayList<Rankable>();
	
	public Ranking() {
		this(DEFAULT_TOP_N);
	}
	
	public Ranking(final int topN) {
		assert (topN >= 1) : "topN must be >= 1.";
		this.maxSize = topN;
	}
	
	public Ranking(final Ranking other) {
		this(other.maxSize);
		this.updateWith(other);
	}
	
	public int getMaxSize() {
		return this.maxSize;
	}
	
	public int getSize() {
		return this.ranking.size();
	}
	
	public List<Rankable> getRanking() {
		List<Rankable> rnk = new ArrayList<Rankable>();
		for (Rankable r : this.ranking)
			rnk.add(r.copy());
		return this.ranking;
	}
	
	public void updateWith(final Ranking other) {
		for (Rankable r : other.getRanking())
			this.updateWith(r);
	}
	
	public void updateWith(final Rankable r) {
		synchronized (this.ranking) {
			this.add(r);
			this.rerank();
			this.shrink();
		}
	}
	
	public void wipeCounts() {
		int pos = 0;
		while (pos < this.ranking.size())
			if (this.ranking.get(pos).getCount() == 0)
				this.ranking.remove(pos);
			else
				pos++;
	}
	
	public Ranking copy() {
		return new Ranking(this);
	}
	
	private int getRankOf(final Rankable r) {
		return this.ranking.indexOf(r);
	}
	
	private void add(final Rankable r) {
		int pos = this.getRankOf(r);
		if (pos == -1)
			this.ranking.add(r);
		else
			this.ranking.set(pos, r);
	}
	
	private void rerank() {
		Collections.sort(this.ranking);
		Collections.reverse(this.ranking);
	}
	
	private void shrink() {
		while (this.ranking.size() > this.maxSize)
			this.ranking.remove(this.maxSize);			
	}
	
	@Override
	public String toString() {
		return this.ranking.toString();
	}

}
