package com.threecore.project.model.rank;

import static org.junit.Assert.*;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Ignore;
import org.junit.Test;

import com.threecore.project.SimpleTest;
import com.threecore.project.model.CommentRank;
import com.threecore.project.model.CommentScore;
import com.threecore.project.model.CommonsComment;
import com.threecore.project.model.rank.comment.CommentRankingSort;
import com.threecore.project.model.rank.comment.CommentRankingStreamSelection;

public class TestCommentRanking extends SimpleTest {
	
	private static final List<CommentScore> SCORES_CREATION = CommonsComment.getCommentCreation(10);
	private static final List<CommentScore> SCORES_COMMENT = CommonsComment.getCreationAndLike(10);
	private static final List<CommentScore> SCORES_DELETION_OUTSIDE = CommonsComment.getCommentCreationAndDeletionOutsideRank(10);
	private static final List<CommentScore> SCORES_DELETION_INSIDE = CommonsComment.getCommentCreationAndDeletionInsideRank(10);
	
	@Test
	public void creation() {
		CommentRanking ranking_1 = new CommentRankingSort(3);
		
		for (CommentScore score: SCORES_CREATION) {
			if (DEBUG) System.out.println("WITH: " + score.asString());
			ranking_1.updateWithElement(score);
			if (DEBUG) System.out.println("\tRANK (sort): " + ranking_1.toCommentRank().asString() + "\n");
		}			
		
		assertEquals(3, ranking_1.getRankSize());
		assertEquals(9, ranking_1.getRanking().get(0).getCommentId());
		assertEquals(8, ranking_1.getRanking().get(1).getCommentId());
		assertEquals(7, ranking_1.getRanking().get(2).getCommentId());
	}
	
	
	@Test
	@Ignore
	public void comment() {
		CommentRanking ranking = new CommentRankingSort();		
		
		for (CommentScore score: SCORES_COMMENT) {
			if (DEBUG) System.out.println("WITH: " + score.asString());
			ranking.updateWithElement(score);
			if (DEBUG) System.out.println("\tRANK: " + ranking.toCommentRank().asString() + "\n");
		}
		
		assertEquals(3, ranking.getRankSize());
		assertEquals(0, ranking.getRanking().get(0).getCommentId());
		assertEquals(1, ranking.getRanking().get(1).getCommentId());
		assertEquals(2, ranking.getRanking().get(2).getCommentId());
	}
	
	@Test
	@Ignore
	public void deletionOutside() {
		CommentRanking ranking = new CommentRankingSort();
		
		for (CommentScore score: SCORES_DELETION_OUTSIDE) {
			if (DEBUG) System.out.println("WITH: " + score.asString());
			ranking.updateWithElement(score);
			if (DEBUG) System.out.println("\tRANK: " + ranking.toCommentRank().asString() + "\n");
		}		
		
		assertEquals(0, ranking.getRankSize());
	}
	
	@Test
	@Ignore
	public void deletionInside() {
		CommentRanking ranking = new CommentRankingSort();
		
		for (CommentScore score: SCORES_DELETION_INSIDE) {
			if (DEBUG) System.out.println("WITH: " + score.asString());
			ranking.updateWithElement(score);
			if (DEBUG) System.out.println("\tRANK: " + ranking.toCommentRank().asString() + "\n");
		}		
		
		assertEquals(0, ranking.getRankSize());
	}

	
	@Test
	public void undefined() {
		CommentRanking ranking_1 = new CommentRankingSort(3);
		
		assertTrue(ranking_1.toCommentRank().isEquivalent(CommentRank.UNDEFINED_RANK));
		assertTrue(CommentRank.UNDEFINED_RANK.isEquivalent(ranking_1.toCommentRank()));
	}
	
	@Test
	public void serialization() {
		SerializationUtils.serialize(new CommentRankingSort(3));
		SerializationUtils.serialize(new CommentRankingStreamSelection(3));
	}

}
