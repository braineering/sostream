package com.threecore.project.tool.rank.noll;

import java.io.Serializable;
import java.util.Objects;

public class RankableObject implements Rankable, Serializable {
	
	private static final long serialVersionUID = 5510476320259793564L;
	
	private Object object;
	private long count;

	public RankableObject(Object obj, long count) {
		if (obj == null)
			throw new IllegalArgumentException("object must not be null");
		if (count < 0)
			throw new IllegalArgumentException("count must be >= 0 (yours is " + count + ")");
		this.object = obj;
		this.count = count;
	}

	@Override
	public Object getObject() {
		return this.object;
	}

	@Override
	public long getCount() {
		return this.count;
	}
	
	@Override
	public Rankable copy() {
		return new RankableObject(this.getObject(), this.getCount());
	}
	
	@Override
	public int compareTo(Rankable other) {
		long diff = this.getCount() - other.getCount();
		if (diff > 0)
			return 1;
		else if (diff < 0)
			return -1;
		else
			return 0;
	}
	
	@Override 
	public boolean equals(Object obj) {
		if (!(obj instanceof RankableObject))
			return false;
		RankableObject other = (RankableObject) obj;
		return this.getObject().equals(other.getObject()) &&
				this.getCount() == other.getCount();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getObject(), this.getCount());
	}
	
	
	@Override 
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append("[");
		buff.append(this.object);
		buff.append("|");
		buff.append(this.count);
		buff.append("]");
		return buff.toString();
	}
	

}
