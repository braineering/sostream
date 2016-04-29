package com.threecore.project.model.score;

import java.util.ArrayList;
import java.util.List;

import com.threecore.project.model.PostScore;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

public class PostScorePidHashMap implements PostScoreMap {

	private static final long serialVersionUID = 1L;
	
	public static final long LOW_ID_MASK = 0x7ffffff; 
	public static final long TOP_ID_MASK = ~LOW_ID_MASK;  
	private static final int TOP_ID_SHIFT = Long.numberOfTrailingZeros(TOP_ID_MASK); 

	private Long2ObjectOpenHashMap<Long2ObjectOpenHashMap<PostScore>> topMap; 	

	private ArrayList<Long> listOfKey;
	
	@SuppressWarnings("unused")
	private int size; 
	
	public PostScorePidHashMap() {
		topMap = new Long2ObjectOpenHashMap<Long2ObjectOpenHashMap<PostScore>>();
		listOfKey = new ArrayList<Long>();
	}

	@Override
	public List<Long> getAllPostId() {
		return new ArrayList<Long>(this.listOfKey);
	}

	@Override
	public void update(PostScore score) {
		long topId = score.getPostId() >> TOP_ID_SHIFT; 
		Long2ObjectOpenHashMap<PostScore> midMap = topMap.get(topId); 
		if (midMap == null) { 
			midMap = new Long2ObjectOpenHashMap<PostScore>(); 
			topMap.put(topId, midMap); 
		} 
		int midId = (int)(score.getPostId() & LOW_ID_MASK); 
		PostScore old = midMap.put(midId, score); 
		if (old == null) {	
			listOfKey.add((long)midId);
			size++;
		}
	}

	@Override
	public PostScore get(Long postId) {
		long topId = postId >> TOP_ID_SHIFT; 
		Long2ObjectOpenHashMap<PostScore> midMap = topMap.get(topId); 
		if (midMap == null) 
			return null; 
		int midId = (int)(postId & LOW_ID_MASK); 
		return midMap.get(midId); 
	}

	@Override
	public void clean() {
		// TODO Auto-generated method stub

	}
	
	public PostScore put(PostScore score) { 
		long topId = score.getPostId() >> TOP_ID_SHIFT; 
		Long2ObjectOpenHashMap<PostScore> midMap = topMap.get(topId); 
		if (midMap == null) { 
			midMap = new Long2ObjectOpenHashMap<PostScore>(); 
			topMap.put(topId, midMap); 
		} 
		int midId = (int)(score.getPostId() & LOW_ID_MASK); 
		PostScore old = midMap.put(midId, score); 
		if (old == null) {	
			listOfKey.add((long)midId);
			size++;
		}
		return old; 
	} 
	
	protected PostScore remove(long key) { 
		long topId = key >> TOP_ID_SHIFT; 
		Long2ObjectOpenHashMap<PostScore> midMap = topMap.get(topId); 
		if (midMap == null) 
			return null; 
		int midId = (int)(key & LOW_ID_MASK); 
		PostScore old = midMap.remove(midId); 
		if (old == null) 
			return null; 
		if (midMap.isEmpty()) 
			topMap.remove(topId); 
		size--;
		int x = listOfKey.indexOf(key);
		listOfKey.remove(x);
		return old; 
	}
	/*
	protected List<Long> getKeySet(){
		return listOfKey;
	}
	
	protected void clear(){ 
		topMap.clear(); 
		size = 0; 
	} 

	protected int size(){ 
		return size; 
	} 

	protected boolean containsKey(long key) { 
		return get(key) != null;  
	} 
	*/

}
