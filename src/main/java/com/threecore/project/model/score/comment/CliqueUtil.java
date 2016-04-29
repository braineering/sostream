package com.threecore.project.model.score.comment;

import java.util.HashSet;
import java.util.Set;

import com.threecore.project.model.Like;

public class CliqueUtil {

	public Set<Like> likes;
	
	public long lastEventTs;
	public long lastUpdateTs;
	
	public CliqueUtil(final long ts) {		
		this.likes = new HashSet<Like>();
		this.lastEventTs = -1L;
		this.lastUpdateTs = ts;		
	}
	
	public void addLike(final Like like) {
		this.likes.add(like);
	}
	
	public HashSet<String> getUserLikes() {
		HashSet<String> userLikes = new HashSet<String>();
		
		for(Like l : likes){
			userLikes.add(String.valueOf(l.getUserId()));
		}
		
		return userLikes;
	}
	
	public boolean needsRecheck() {
		return this.lastEventTs > this.lastUpdateTs;
	}
	
	public void updateLastEventTs(final long ts) {
		this.lastEventTs = ts;
	}
	
	public void updateLastUpdateTs(final long ts) {
		this.lastUpdateTs = ts;
	}
	
}
