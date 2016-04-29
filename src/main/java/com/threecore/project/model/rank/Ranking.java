package com.threecore.project.model.rank;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import com.threecore.project.model.type.Chronological;
import com.threecore.project.model.type.Copyable;
import com.threecore.project.model.type.Scorable;

public interface Ranking<T extends Chronological & Scorable & Comparable<T> & Copyable<T> & Serializable> extends Chronological, Serializable {
	
	public int DEFAULT_RANK_SIZE = 3;	
	
	public int getRankMaxSize();
	
	public Comparator<T> getComparator();
	
	public long getTimestamp();
	
	public int getRankSize();	
	
	public List<T> getAllElements();
	
	public void updateWithRanking(final Ranking<T> rank);
	
	public void updateWithElement(final T elem);
	
	public void addRanking(final Ranking<T> rank);
	
	public void addElement(final T elem);	
	
	public boolean isComplete();
	
	// abstract
	
	public void clean();
	
	public void consolidate();
	
	public List<T> getRanking();
	
	public long getLowerBoundScore();
	
}
