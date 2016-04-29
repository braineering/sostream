package com.threecore.project.tool.rank;

import java.io.Serializable;
import java.util.List;

import com.threecore.project.model.interfaces.Copyable;
import com.threecore.project.model.interfaces.IScorable;

public interface IRank<T extends IScorable & Comparable<T> & Copyable<T>> extends Serializable {
	
	public int getRankMaxSize();
	
	public int getRankSize();
	
	public int getRankOf(T element);
	
	public T getElementOfRank(int position);
	
	public List<T> getRanking();	
	
	public void updateWith(T element);
	
	public void updateWith(IRank<T> rank);
	
}
