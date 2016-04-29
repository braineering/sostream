package com.threecore.project.tool.rank.noll;

public interface Rankable extends Comparable<Rankable> {	
	
	public Object getObject();
	
	public long getCount();
	
	public Rankable copy();
	
	@Override
	default public int compareTo(Rankable other) {
		return Long.compare(this.getCount(), other.getCount());
	}

}
