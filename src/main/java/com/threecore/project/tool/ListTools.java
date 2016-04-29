package com.threecore.project.tool;

import java.util.LinkedList;

public class ListTools {
	
	public static <T> LinkedList<T> subList(LinkedList<T> list, int start, int end){
		LinkedList<T> tmp = new LinkedList<T>();
		
		for(int i=start; i<end; i++){
			tmp.add(list.poll());
		}
		
		return tmp;
	}

}
