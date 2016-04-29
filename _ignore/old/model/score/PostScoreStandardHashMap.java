package com.threecore.project.model.score;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import com.threecore.project.model.PostScore;

public class PostScoreStandardHashMap implements PostScoreMap {

	private static final long serialVersionUID = 1L;
	
	protected Map<Long, PostScore> map;
	
	public PostScoreStandardHashMap() {
		this.map = new HashMap<Long, PostScore>();
	}
	
	@Override
	public List<Long> getAllPostId() {
		return new ArrayList<Long>(this.map.keySet());
	}

	@Override
	public void update(PostScore score) {
		this.map.put(score.getPostId(), score.copy());	
	}

	@Override
	public PostScore get(Long postId) {
		return this.map.get(postId);
	}
	
	public void clean() {
		Iterator<Map.Entry<Long, PostScore>> iter = this.map.entrySet().iterator();
		while (iter.hasNext()) {
		    Map.Entry<Long, PostScore> entry = iter.next();
		    if(entry.getValue().getScore() == 0){
		        iter.remove();
		    }
		}
	}
	
	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner("|");
		Iterator<Map.Entry<Long, PostScore>> iter = this.map.entrySet().iterator();
		while (iter.hasNext()) {
		    Map.Entry<Long, PostScore> entry = iter.next();
		    joiner.add("(" + entry.getKey() + ";" + entry.getValue().getScore() + ")");
		}		
		return joiner.toString();
		
	}

}
