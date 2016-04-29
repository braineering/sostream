package com.threecore.project.model.score.post.comparator;

import java.io.Serializable;
import java.util.Comparator;

import com.threecore.project.model.PostScore;

public class PostScoreComparatorDesc implements Comparator<PostScore>, Serializable {

	private static final long serialVersionUID = 1L;
	
	private static PostScoreComparatorDesc instance;
	
	public static final PostScoreComparatorDesc getInstance() {
		if (instance == null) {
			instance = new PostScoreComparatorDesc();
		}
		return instance;
	}
	
	private PostScoreComparatorDesc() {}

	@Override
	public int compare(PostScore score1, PostScore score2) {
		if (score1.getScore() == score2.getScore()) {
			if (score1.getPostTimestamp() == score2.getPostTimestamp()) {		
				if (score1.getLastCommentTimestamp() == score2.getLastCommentTimestamp()) {
					return Long.compare(score2.getPostId(), score1.getPostId());
				} else {
					return Long.compare(score2.getLastCommentTimestamp(), score1.getLastCommentTimestamp());
				}
			} else {
				return Long.compare(score2.getPostTimestamp(), score1.getPostTimestamp());
			}
		} else {
			return Long.compare(score2.getScore(), score1.getScore());
		}
	}

}
