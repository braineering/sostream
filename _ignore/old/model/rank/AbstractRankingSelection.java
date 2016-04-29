package com.threecore.project.model.rank.base;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.threecore.project.model.type.Keyable;
import com.threecore.project.model.type.Rankable;

public abstract class AbstractRankingSelection<T extends Rankable<T> & Keyable> extends AbstractRanking<T> implements Ranking<T>, Serializable {

	private static final long serialVersionUID = 1L;
	
	protected List<T> sortedElements;
	
	public AbstractRankingSelection(final int rankMaxSize, final Comparator<T> comparator) {
		super(rankMaxSize, comparator);
		super.elements = new ArrayList<T>();
		this.sortedElements = new ArrayList<T>(super.rankMaxSize);
	}		

	@Override
	public void consolidate() {
		super.clean();		
		
		List<T> tmp = new ArrayList<T>(super.elements);	
		
		int rsize = (super.rankMaxSize < tmp.size()) ? super.rankMaxSize : tmp.size();
		
		for (int pos = 0; pos < rsize; pos++) {
			T maxElement = tmp.get(0);			
			int maxIndex = 0;
			for (int i = 0; i < tmp.size(); i++) {
				T elem = tmp.get(i);
				if (super.comparator.compare(maxElement, elem) == -1) {
					maxElement = elem;
					maxIndex = i;
				}
			}
			try {
				this.sortedElements.set(pos, maxElement);
			} catch (IndexOutOfBoundsException exc) {
				this.sortedElements.add(maxElement);
			}
			tmp.remove(maxIndex);
		}
	}
	
	@Override
	public List<T> getRanking() {
		return this.sortedElements;
	}
	
	@Override
	public long getLowerBoundScore() {
		int rsize = this.sortedElements.size();
		
		if (rsize == 0) return Long.MIN_VALUE;
		
		int lbpos = (this.rankMaxSize < rsize) ? this.rankMaxSize : rsize;
		
		return this.sortedElements.get(lbpos - 1).getScore();
	}

}
