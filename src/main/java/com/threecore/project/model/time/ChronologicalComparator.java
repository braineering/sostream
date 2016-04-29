package com.threecore.project.model.time;

import java.util.Comparator;

import com.threecore.project.model.type.Chronological;

public class ChronologicalComparator<T extends Chronological> implements Comparator<T> {
	
	@Override
	public int compare(T obj1, T obj2) {
		if (obj1.getTimestamp() > obj2.getTimestamp())
			return 1;
		else if (obj1.getTimestamp() < obj2.getTimestamp())
			return -1;
		else
			return 0;
	}

}
