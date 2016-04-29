package com.threecore.project.model.score.comment.comparator;

import java.io.Serializable;
import java.util.Comparator;

import com.threecore.project.model.CommentScore;

public class CommentScoreComparatorAsc implements Comparator<CommentScore>, Serializable {

	private static final long serialVersionUID = 1L;
	
	private static CommentScoreComparatorAsc instance;
	
	public static final CommentScoreComparatorAsc getInstance() {
		if (instance == null) {
			instance = new CommentScoreComparatorAsc();
		}
		return instance;
	}
	
	private CommentScoreComparatorAsc() {}

	@Override
	public int compare(CommentScore score1, CommentScore score2) {
		if (score1.getScore() == score2.getScore()) {
			if (score1.getTimestamp() == score2.getTimestamp()) {
				return score1.getComment().compareTo(score2.getComment());
			} else {
				return Long.compare(score1.getTimestamp(),score2.getTimestamp());
			}			
		} else {
			return Long.compare(score1.getScore(), score2.getScore());
		}
	}
	
}
