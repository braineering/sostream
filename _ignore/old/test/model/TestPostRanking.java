package com.threecore.project.model;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import com.threecore.project.SimpleTest;
import com.threecore.project.control.stream.PostScoreStreamgen;
import com.threecore.project.model.rank.PRanking;
import com.threecore.project.model.rank.PRankingStandard;
import com.threecore.project.model.rank.post.PostRanking;
import com.threecore.project.model.rank.post.PostRankingStandard;

public class TestPostRanking extends SimpleTest {
	
	private static final LocalDateTime TIME = LocalDateTime.of(2016, 1, 1, 12, 0, 0);
	
	private static final List<PostScore> SCORES_CREATION = PostScoreStreamgen.getCreation(10);
	private static final List<PostScore> SCORES_COMMENT = PostScoreStreamgen.getCreationAndComment(10);
	private static final List<PostScore> SCORES_DELETION_OUTSIDE = PostScoreStreamgen.getCreationAndDeletionOutsideRank(10);
	private static final List<PostScore> SCORES_DELETION_INSIDE = PostScoreStreamgen.getCreationAndDeletionInsideRank(10);
	
	private static final boolean DEBUG = false;

	@Test
	public void creation() {
		PostRanking ranking = new PostRankingStandard();
		
		for (PostScore score: SCORES_CREATION) {
			if (DEBUG) System.out.println("WITH: " + score.asString());
			ranking.updateWithScore(score);
			if (DEBUG) System.out.println("\tRANK: " + ranking.toPostRank().asString() + "\n");
		}			
		
		assertEquals(3, ranking.getRankSize());
		assertEquals(9, ranking.getFirst().getPostId());
		assertEquals(8, ranking.getSecond().getPostId());
		assertEquals(7, ranking.getThird().getPostId());
	}
	
	@Test
	public void comment() {
		PostRanking ranking = new PostRankingStandard();		
		
		for (PostScore score: SCORES_COMMENT) {
			if (DEBUG) System.out.println("WITH: " + score.asString());
			ranking.updateWithScore(score);
			if (DEBUG) System.out.println("\tRANK: " + ranking.toPostRank().asString() + "\n");
		}
		
		assertEquals(3, ranking.getRankSize());
		assertEquals(0, ranking.getFirst().getPostId());
		assertEquals(1, ranking.getSecond().getPostId());
		assertEquals(2, ranking.getThird().getPostId());
	}
	
	@Test
	public void deletionOutside() {
		PostRanking ranking = new PostRankingStandard();
		
		for (PostScore score: SCORES_DELETION_OUTSIDE) {
			if (DEBUG) System.out.println("WITH: " + score.asString());
			ranking.updateWithScore(score);
			if (DEBUG) System.out.println("\tRANK: " + ranking.toPostRank().asString() + "\n");
		}		
		
		assertEquals(0, ranking.getRankSize());
	}
	
	@Test
	public void deletionInside() {
		PostRanking ranking = new PostRankingStandard();
		
		for (PostScore score: SCORES_DELETION_INSIDE) {
			if (DEBUG) System.out.println("WITH: " + score.asString());
			ranking.updateWithScore(score);
			if (DEBUG) System.out.println("\tRANK: " + ranking.toPostRank().asString() + "\n");
		}		
		
		assertEquals(0, ranking.getRankSize());
	}
	
	@Test
	public void isSynchronized() {
		PostRanking ranking = new PostRankingStandard();
		
		PostScore score_1 = new PostScore(TIME, TIME, 1L, 1L, "user-1", 10L, 0L, ModelCommons.UNDEFINED_LDT);
		PostScore score_2 = new PostScore(TIME.plusDays(1), TIME.plusDays(1), 2L, 1L, "user-1", 10L, 0L, ModelCommons.UNDEFINED_LDT);
		PostScore score_3 = new PostScore(TIME.plusDays(1), TIME, 1L, 1L, "user-1", 9L, 0L, ModelCommons.UNDEFINED_LDT);
		
		ranking.updateWithScore(score_1);		
		if (DEBUG) System.out.println(ranking.asString());
		assertTrue(ranking.isSynchronized());
		
		ranking.updateWithScore(score_2);
		if (DEBUG) System.out.println(ranking.asString());
		assertFalse(ranking.isSynchronized());
		
		ranking.updateWithScore(score_3);
		if (DEBUG) System.out.println(ranking.asString());
		assertTrue(ranking.isSynchronized());		
		
	}
	
	@Test
	public void undefined() {
		PostRanking ranking = new PostRankingStandard();
		PostRank rank = ranking.toPostRank();
		
		assertTrue(rank.isEquivalent(PostRank.UNDEFINED_RANK));
		assertTrue(PostRank.UNDEFINED_RANK.isEquivalent(rank));
	}
	
	@Test
	public void serialization() {
		PostRanking ranking = new PostRankingStandard();
		SerializationUtils.serialize(ranking);
	}

}
