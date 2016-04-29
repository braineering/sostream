package com.threecore.project.model.score.comment.comparator;

import java.io.Serializable;
import java.util.Comparator;

import com.threecore.project.model.CommentScore;

public class CommentScoreComparatorDesc implements Comparator<CommentScore>, Serializable {

	private static final long serialVersionUID = 1L;
	
	private static CommentScoreComparatorDesc instance;
	
	public static final CommentScoreComparatorDesc getInstance() {
		if (instance == null) {
			instance = new CommentScoreComparatorDesc();
		}
		return instance;
	}
	
	private CommentScoreComparatorDesc() {}

	@Override
	public int compare(CommentScore score1, CommentScore score2) {
		if (score1.getScore() == score2.getScore()) {
			if (score1.getTimestamp() == score2.getTimestamp()) {
				return score2.getComment().compareTo(score1.getComment());
			} else {
				return Long.compare(score2.getTimestamp(),score1.getTimestamp());
			}			
		} else {
			return Long.compare(score2.getScore(), score1.getScore());
		}
	}

}
