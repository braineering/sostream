package com.threecore.project.tool;

import java.util.Set;
import java.util.TreeSet;

public class SetTools {
	
	public static <T> Set<T> SetOnSetUnion(Set<T> setA, Set<T> setB) {
	    Set<T> tmp = new TreeSet<T>(setA);
	    tmp.addAll(setB);
	    return tmp;
	  }
	
	public static <T> Set<T> SetOnObjUnion(Set<T> set, T elm){
		Set<T> tmp = new TreeSet<T>(set);
		tmp.add(elm);
		return tmp;
	}

	public static <T> Set<T> intersection(Set<T> setA, Set<T> setB) {
	    Set<T> tmp = new TreeSet<T>();
	    for (T x : setA)
	      if (setB.contains(x))
	        tmp.add(x);
	    return tmp;
	  }

	  public static <T> Set<T> SetOnSetDiff(Set<T> setA, Set<T> setB) {
	    Set<T> tmp = new TreeSet<T>(setA);
	    tmp.removeAll(setB);
	    return tmp;
	  }
	  
	  public static <T> Set<T> SetOnObjDiff(Set<T> set, T elm){
		  Set<T> tmp = new TreeSet<T>(set);
		  tmp.remove(elm);
		  return tmp;
	  }

}
