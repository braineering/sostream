package com.threecore.project.draft.marco;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.util.Collector;
import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class ProcessOperator implements 
	FlatMapFunction<
	Tuple4<Comment, Friendship, Like, Integer>,
	Tuple4<Long, Long, String, Long>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6198611048637988673L;

	public UndirectedGraph<Long, DefaultEdge> friendsGraph;
	
	public LinkedList<Comment> window;
	public HashMap<Long, Comment> commentMap;
	public Integer d;
	
	public ProcessOperator(int d) {
		friendsGraph = new SimpleGraph<Long, DefaultEdge>(DefaultEdge.class);
		window = new LinkedList<Comment>();
		commentMap = new HashMap<Long, Comment>();
		this.d = d;
	}
	
	@Override
	public void flatMap(Tuple4<Comment, Friendship, Like, Integer> in,
			Collector<Tuple4<Long, Long, String, Long>> out) throws Exception {
		
		switch(in.f3){
		case 0:{			
			window.addFirst(in.f0);
			commentMap.put(in.f0.getCommentId(), in.f0);
			checkComments(in.f0.getTimestamp(), out);
			break;
		}
		case 1:{
			
			Long user1 = in.f1.getUserId1();
			Long user2 = in.f1.getUserId2();
			
			if(!friendsGraph.containsVertex(user1)){
				friendsGraph.addVertex(user1);
			}
			if(!friendsGraph.containsVertex(user2)){
				friendsGraph.addVertex(user2);
			}
			
			friendsGraph.addEdge(user1, user2);
			checkComments(in.f1.getTimestamp(), out);
			
			break;
		}
		case 2:{
			commentMap.get(in.f2.getCommentId()).addLike(in.f2);
			
			checkComments(in.f2.getTimestamp(), out);
			break;
		}
		default:{
			break;
		}
		}
		
	}

	private void checkComments(Long ts, 
			Collector<Tuple4<Long, Long, String, Long>> out) {
		Iterator<Comment> iterWindow = window.iterator();
		
		Comment tmp;
		Integer index = 0;
		LinkedList<Comment> tmpWindow = null;
		LinkedList<Comment> removeWindow = null;
		
		while(iterWindow.hasNext()){
			tmp = iterWindow.next();
			if(tmp.getTimestamp() > ts + (d*1000)){
				tmpWindow = (LinkedList<Comment>) window.subList(0, index-1);
				removeWindow = (LinkedList<Comment>) window.subList(index, window.size()-1);
				break;
			}
			else{
				processCliques(tmp, ts, out);
			}
			
			index++;
		}
		
		if(tmpWindow != null){
			window = tmpWindow;
			if(!removeWindow.isEmpty()){
				remove(removeWindow);
			}
		}
		
	}

	private void processCliques(Comment tmp, Long ts, Collector<Tuple4<Long, Long, String, Long>> out) {
		HashSet<Set<Long>> maxCliques = new HashSet<Set<Long>>();
		
		HashSet<Long> R = new HashSet<Long>();
		HashSet<Long> P = new HashSet<Long>();
		HashSet<Long> X = new HashSet<Long>();
		
		HashSet<Long> likes = tmp.getUserLikes();
		
		Long maxRange = 0L;
		Long tmpRange;
		
		for(Long l : likes){
			P.addAll(SetOnObjUnion(new HashSet<Long>(Graphs.neighborListOf(friendsGraph, l)), l));
			
			BronKerbosch(R, P, X, maxCliques);
			
			maxCliques = filter(maxCliques, likes);
			
			tmpRange = findMax(maxCliques);
			
			if(tmpRange > maxRange){
				maxRange = tmpRange.longValue();
			}

		}
		out.collect(new Tuple4<>(ts, tmp.getCommentId(), tmp.getComment(), maxRange));
	}

	private HashSet<Set<Long>> filter(HashSet<Set<Long>> maxCliques, HashSet<Long> likes) {
		
		HashSet<Set<Long>> tmpCliques = new HashSet<Set<Long>>();
		Set<Long> tmpSet;
//		System.out.println("Likes: " + likes);
		
		for(Set<Long> s : maxCliques){
			tmpSet = intersection(s, likes);
			if(!tmpSet.isEmpty()){
				tmpCliques.add(tmpSet);
	
			}
		}
		
		return tmpCliques;
		
	}

	private Long findMax(HashSet<Set<Long>> maxCliques) {
		
		Iterator<Set<Long>> iterCliques = maxCliques.iterator();
		
		Long max = 0L;
		Long tmp;
		
		while(iterCliques.hasNext()){
			tmp = (long) iterCliques.next().size();
			
			if(tmp > max){
				max = tmp;
			}
		}
		
		return max;
	}

	private void remove(LinkedList<Comment> removeWindow) {
		Iterator<Comment> iterRemove = removeWindow.iterator();
		Comment tmp;
		
		while(iterRemove.hasNext()){
			tmp = iterRemove.next();
			
			commentMap.remove(tmp.getCommentId());
			
		}
	}

	public void BronKerbosch(
			Set<Long> R,
			Set<Long> P,
			Set<Long> X, // This could be useless, it needs to be checked
			Set<Set<Long>> maxCliques
			){
		
		if(P.isEmpty() && X.isEmpty()){
			maxCliques.add(R);
		}
		else{
			for(Long v : P){
				BronKerbosch(SetOnObjUnion(R, v),
						intersection(P, new HashSet<Long>(Graphs.neighborListOf(friendsGraph, v))),
						intersection(X, new HashSet<Long>(Graphs.neighborListOf(friendsGraph, v))),
						maxCliques);
				
				P = SetOnObjDiff(P, v);
				X.add(v);
			
			}
		}
		
	}

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
