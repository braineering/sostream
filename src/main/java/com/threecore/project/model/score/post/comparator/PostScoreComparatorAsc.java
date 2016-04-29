package com.threecore.project.model.score.post.comparator;

import java.io.Serializable;
import java.util.Comparator;

import com.threecore.project.model.PostScore;

public class PostScoreComparatorAsc implements Comparator<PostScore>, Serializable {

	private static final long serialVersionUID = 1L;
	
	private static PostScoreComparatorAsc instance;
	
	public static final PostScoreComparatorAsc getInstance() {
		if (instance == null) {
			instance = new PostScoreComparatorAsc();
		}
		return instance;
	}
	
	private PostScoreComparatorAsc() {}

	@Override
	public int compare(PostScore score1, PostScore score2) {
		if (score1.getScore() == score2.getScore()) {
			if (score1.getPostTimestamp() == score2.getPostTimestamp()) {		
				if (score1.getLastCommentTimestamp() == score2.getLastCommentTimestamp()) {
					return Long.compare(score1.getPostId(), score2.getPostId());
				} else {
					return Long.compare(score1.getLastCommentTimestamp(), score2.getLastCommentTimestamp());
				}				
			} else {
				return Long.compare(score1.getPostTimestamp(), score2.getPostTimestamp());
			}
		} else {
			return Long.compare(score1.getScore(), score2.getScore());
		}
	}
	
}
