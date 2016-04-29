package com.threecore.project.model.rank.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import com.threecore.project.model.type.Keyable;
import com.threecore.project.model.type.Rankable;

public abstract class AbstractRankingMinHeap<T extends Rankable<T> & Keyable> extends AbstractRanking<T> implements Ranking<T>, Serializable {

	private static final long serialVersionUID = 1L;
	
	protected List<T> sortedElements; 
	protected PriorityQueue<T> heap;
	
	public AbstractRankingMinHeap(final int rankMaxSize, final Comparator<T> comparator) {
		super(rankMaxSize, comparator);
		super.elements = new ArrayList<T>();
		this.sortedElements = new ArrayList<T>(super.rankMaxSize);
		this.heap = new PriorityQueue<T>(super.rankMaxSize, super.comparator);
	}

	@Override
	public void consolidate() {
		super.clean();
		
		int rsize = super.elements.size();		
		for (int i = 0; i < rsize; i++) {
			T elem = super.elements.get(i);
			if (i < super.rankMaxSize) {
				this.heap.add(elem);
			} else if (super.comparator.compare(this.heap.peek(), elem) == -1) {
				this.heap.poll();
				this.heap.add(elem);
			}
			
		}
		
		int size = (rsize < super.rankMaxSize) ? rsize : super.rankMaxSize;
		for (int pos = size - 1; pos >= 0; pos--) {
			T elem = this.heap.poll();
			try {
				this.sortedElements.set(pos, elem);
			} catch (IndexOutOfBoundsException exc) {
				this.sortedElements.add(elem);
			}
		}	
		
		this.heap.clear();
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
