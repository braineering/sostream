package com.threecore.project.draft.michele.model.score;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import com.threecore.project.draft.michele.model.CommentScore;

@Deprecated
public class CommentScoreStandardHashMap implements CommentScoreMap 
{
	private static final long serialVersionUID = 3361324450463133708L;
	
	protected Map<Long, CommentScore> map;
	
	public CommentScoreStandardHashMap() {
		this.map = new HashMap<Long, CommentScore>();
	}
	
	@Override
	public List<Long> getAllCommentId() {
		return new ArrayList<Long>(this.map.keySet());
	}

	@Override
	public void updateWith(CommentScore score) {
		this.map.put(score.getCommentId(), score.copy());	
	}

	@Override
	public CommentScore get(Long commentId) {
		return this.map.get(commentId);
	}
	
	public void clean() {
		Iterator<Map.Entry<Long, CommentScore>> iter = this.map.entrySet().iterator();
		while (iter.hasNext()) {
		    Map.Entry<Long, CommentScore> entry = iter.next();
		    if(entry.getValue().getScore() == 0){
		        iter.remove();
		    }
		}
	}
	
	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner("|");
		Iterator<Map.Entry<Long, CommentScore>> iter = this.map.entrySet().iterator();
		while (iter.hasNext()) {
		    Map.Entry<Long, CommentScore> entry = iter.next();
		    joiner.add("(" + entry.getKey() + ";" + entry.getValue().getScore() + ")");
		}		
		return joiner.toString();
		
	}

}
