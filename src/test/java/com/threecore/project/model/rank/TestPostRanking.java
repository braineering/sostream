package com.threecore.project.model.rank;

import static org.junit.Assert.*;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import com.threecore.project.SimpleTest;
import com.threecore.project.model.CommonsPost;
import com.threecore.project.model.PostRank;
import com.threecore.project.model.PostScore;
import com.threecore.project.model.rank.PostRanking;
import com.threecore.project.model.rank.post.PostRankingSort;
import com.threecore.project.model.rank.post.PostRankingStreamSelection;

public class TestPostRanking extends SimpleTest {
	
	private static final List<PostScore> SCORES_CREATION = CommonsPost.getPostCreation(10);
	private static final List<PostScore> SCORES_COMMENT = CommonsPost.getPostCreationAndComment(10);
	private static final List<PostScore> SCORES_DELETION_OUTSIDE = CommonsPost.getPostCreationAndDeletionOutsideRank(10);
	private static final List<PostScore> SCORES_DELETION_INSIDE = CommonsPost.getPostCreationAndDeletionInsideRank(10);
	
	@Test
	public void creation() {
		PostRanking ranking_1 = new PostRankingSort();
		
		for (PostScore score: SCORES_CREATION) {
			if (DEBUG) System.out.println("WITH: " + score.asString());
			ranking_1.updateWithElement(score);
			if (DEBUG) System.out.println("\tRANK (sort): " + ranking_1.toPostRank().asString() + "\n");
		}			
		
		assertEquals(3, ranking_1.getRankSize());
		assertEquals(9, ranking_1.getRanking().get(0).getPostId());
		assertEquals(8, ranking_1.getRanking().get(1).getPostId());
		assertEquals(7, ranking_1.getRanking().get(2).getPostId());
	}
	
	@Test
	public void comment() {
		PostRanking ranking_1 = new PostRankingSort();
		
		for (PostScore score: SCORES_COMMENT) {
			if (DEBUG) System.out.println("WITH: " + score.asString());
			ranking_1.updateWithElement(score);
			if (DEBUG) System.out.println("\tRANK (sort): " + ranking_1.toPostRank().asString() + "\n");
		}
		
		assertEquals(3, ranking_1.getRankSize());
		assertEquals(0, ranking_1.getRanking().get(0).getPostId());
		assertEquals(1, ranking_1.getRanking().get(1).getPostId());
		assertEquals(2, ranking_1.getRanking().get(2).getPostId());
	}
	
	@Test
	public void deletionOutside() {
		PostRanking ranking_1 = new PostRankingSort();
		
		for (PostScore score: SCORES_DELETION_OUTSIDE) {
			if (DEBUG) System.out.println("WITH: " + score.asString());
			ranking_1.updateWithElement(score);
			if (DEBUG) System.out.println("\tRANK (sort): " + ranking_1.toPostRank().asString() + "\n");
		}		
		
		assertEquals(0, ranking_1.getRankSize());
	}
	
	@Test
	public void deletionInside() {
		PostRanking ranking_1 = new PostRankingSort();
		
		for (PostScore score: SCORES_DELETION_INSIDE) {
			if (DEBUG) System.out.println("WITH: " + score.asString());
			ranking_1.updateWithElement(score);
			if (DEBUG) System.out.println("\tRANK (sort): " + ranking_1.toPostRank().asString() + "\n");
		}		
		
		assertEquals(0, ranking_1.getRankSize());
	}
	
	@Test
	public void undefined() {
		PostRanking ranking_1 = new PostRankingSort();
		
		assertTrue(ranking_1.toPostRank().isEquivalent(PostRank.UNDEFINED_RANK));
		assertTrue(PostRank.UNDEFINED_RANK.isEquivalent(ranking_1.toPostRank()));
	}
	
	@Test
	public void serialization() {
		SerializationUtils.serialize(new PostRankingSort());
		SerializationUtils.serialize(new PostRankingStreamSelection());
	}

}
