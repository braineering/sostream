package com.threecore.project.model.rank.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.threecore.project.model.rank.Ranking;
import com.threecore.project.model.type.Keyable;
import com.threecore.project.model.type.Rankable;

public abstract class AbstractRanking<T extends Rankable<T> & Keyable> implements Ranking<T>, Serializable {

	private static final long serialVersionUID = 1L;
	
	protected int rankMaxSize;	
	protected Comparator<T> comparator;
	
	protected long timestamp;
	
	protected List<T> elements;
	
	public AbstractRanking(final int rankMaxSize, final Comparator<T> comparator) {
		this.rankMaxSize = rankMaxSize;
		this.comparator = comparator;
		this.timestamp = Long.MIN_VALUE;
		this.elements = new ArrayList<T>();
	}
	
	@Override
	public int getRankMaxSize() {
		return this.rankMaxSize;
	}
	
	@Override
	public Comparator<T> getComparator() {
		return this.comparator;
	}
	
	@Override
	public long getTimestamp() {
		return this.timestamp;
	}
	
	@Override
	public int getRankSize() {
		int numelements = this.elements.size();
		return (this.rankMaxSize < numelements) ? this.rankMaxSize : numelements;
	}	
	
	@Override
	public List<T> getAllElements() {
		return this.elements;
	}
	
	@Override
	public void updateWithRanking(final Ranking<T> otherRank) {
		this.addRanking(otherRank);
		this.consolidate();
	}
	
	@Override
	public void updateWithElement(final T elem) {
		this.addElement(elem);
		this.consolidate();
	}
	
	@Override
	public void addRanking(final Ranking<T> otherRank) {
		for (T score : otherRank.getAllElements()) {
			this.addElement(score);
		}
	}
	
	@Override
	public void addElement(final T elem) {
		int pos = this.elements.indexOf(elem);
		if (pos == -1) {
			if (elem.getScore() > 0) {
				this.elements.add(elem.copy());
			}
		} else {
			if (elem.getScore() <= 0) {
				this.elements.remove(pos);
			} else {
				this.elements.set(pos, elem.copy());
			}			
		}
		
		if (elem.getTimestamp() > this.timestamp) {
			this.timestamp = elem.getTimestamp();
		}
	}		
	
	@Override
	public boolean isComplete() {
		return this.elements.size() >= this.rankMaxSize;
	}
	
	@Override
	public void clean() {
		Iterator<T> iter = this.elements.iterator();
		while (iter.hasNext()) {
			T elem = iter.next();
			if (elem.getScore() <= 0) {
				iter.remove();
			}
		}
	}
	
	@Override
	public abstract void consolidate();
	
	@Override
	public abstract List<T> getRanking();	

	@Override
	public long getLowerBoundScore() {
		int numElements = this.elements.size();
		
		if (numElements < this.rankMaxSize) {
			return Long.MIN_VALUE;
		} else {
			return this.elements.get(this.rankMaxSize - 1).getScore();
		}
	}
	
	@Override
	public String toString() {
		ToStringBuilder string = new ToStringBuilder(this);
		List<T> elements = this.getAllElements();		
		
		string.append("max_rank_size", this.getRankMaxSize());
		string.append("num_elements", elements.size());
		string.append("rank", this.getRanking());
		string.append("elements", this.elements);
		
		return string.toString();
	}

}
