package com.threecore.project.tool.rank;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.threecore.project.model.interfaces.Copyable;
import com.threecore.project.model.interfaces.IScorable;

public abstract class AbstractScoredRanking<T extends IScorable & Comparable<T> & Copyable<T>> implements IRank<T> {
	
	private static final long serialVersionUID = 3436518062083693085L;
	
	public static final int RANK_SIZE = 3;
	
	protected int rankMaxSize;
	
	public AbstractScoredRanking(final int rankMaxSize) {
		assert (rankMaxSize >= 1) : "rankMaxSize must be >= 1.";
		this.rankMaxSize = rankMaxSize;
	}
	
	public AbstractScoredRanking(final AbstractScoredRanking<T> other) {
		assert (other != null) : "other must be != null.";
		this.rankMaxSize = other.getRankMaxSize();
	}
	
	public AbstractScoredRanking() {
		this(RANK_SIZE);
	}
	
	@Override
	public int getRankMaxSize() {
		return this.rankMaxSize;
	}
	
	@Override
	public int getRankSize() {
		return this.getRanking().size();
	}
	
	@Override
	public T getElementOfRank(final int position) {
		return this.getRanking().get(position);
	}
	
	@Override
	public int getRankOf(final T element) {
		return this.getRanking().indexOf(element);
	}
	
	@Override
	public abstract List<T> getRanking();
	
	@Override
	public abstract void updateWith(final T element);
	
	@Override
	public abstract void updateWith(final IRank<T> rank);	
	
	@Override
	public String toString() {
		ToStringBuilder toStringBuilder = new ToStringBuilder(this);
		
		for (int pos = 0 ; pos < this.getRankSize(); pos++)
			toStringBuilder.append(String.valueOf(pos), this.getRanking().get(pos));
		
		return toStringBuilder.toString();
	}

}
