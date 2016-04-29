package com.threecore.project.model;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import com.threecore.project.SimpleTest;
import com.threecore.project.model.rank.post.PostRanking;
import com.threecore.project.model.rank.post.PostRankingStandard;

public class TestPostRank extends SimpleTest {
	
	private static final LocalDateTime TIME = LocalDateTime.of(2016, 1, 1, 12, 0, 0);
	
	private static final String RANK_LINE_0 = "2016-01-01T12:00:00.000+0000,-,-,-,-,-,-,-,-,-,-,-,-";
	private static final String RANK_LINE_1 = "2016-01-01T12:00:00.000+0000,3,user-1,3,1,-,-,-,-,-,-,-,-";
	private static final String RANK_LINE_2 = "2016-01-01T12:00:00.000+0000,3,user-1,3,1,2,user-1,2,1,-,-,-,-";
	private static final String RANK_LINE_3 = "2016-01-01T12:00:00.000+0000,3,user-1,3,1,2,user-1,2,1,1,user-1,1,1";
	
	private static final PostRank RANK_0 = new PostRank(TIME);
	private static final PostRank RANK_1 = new PostRank(TIME, new PostScore(TIME.plusMinutes(10), TIME, 3L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(5)));
	private static final PostRank RANK_2 = new PostRank(TIME, new PostScore(TIME.plusMinutes(10), TIME, 3L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(5)), 
															  new PostScore(TIME.plusMinutes(10), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(5)));
	private static final PostRank RANK_3 = new PostRank(TIME, new PostScore(TIME.plusMinutes(10), TIME, 3L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(5)), 
															  new PostScore(TIME.plusMinutes(10), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(5)), 
															  new PostScore(TIME.plusMinutes(10), TIME, 1L, 1L, "user-1", 1L, 1L, TIME.plusMinutes(5)));
	
	private PostRank RANKS_EQUIV[] = getEquivalentPostRanks(); 
	private PostRank RANKS_NOEQUIV[] = getNotEquivalentPostRanks(); 
	
	@Test
	public void asString() {		
		assertEquals(RANK_LINE_0, RANK_0.asString());
		assertEquals(RANK_LINE_1, RANK_1.asString());
		assertEquals(RANK_LINE_2, RANK_2.asString());
		assertEquals(RANK_LINE_3, RANK_3.asString());
	}
	
	@Test
	public void copy() {
		assertEquals(RANK_0, RANK_0.copy());
		assertEquals(RANK_1, RANK_1.copy());
		assertEquals(RANK_2, RANK_2.copy());
		assertEquals(RANK_3, RANK_3.copy());
		
		PostRank copyRank3 = RANK_3.copy();
		assertEquals(RANK_0, copyRank3.copy(RANK_0));	
	}

	@Test
	public void undefined() {
		PostRank init = new PostRank();
		
		assertNotEquals(init, RANK_0);
		assertNotEquals(init, RANK_1);
		assertNotEquals(init, RANK_2);
		assertNotEquals(init, RANK_3);
		
		for (PostScore score : init.getAll())
			assertFalse(score.isDefined());
	}
	
	@Test
	public void isEquivalent() {
		for (int n = 0; n < RANKS_EQUIV.length; n++) {
			for (int m = 0; m < RANKS_EQUIV.length; m++) {
				assertTrue(RANKS_EQUIV[n].isEquivalent(RANKS_EQUIV[m]));
				assertTrue(RANKS_EQUIV[n].isHashEquivalent(RANKS_EQUIV[m]));
			}
		}
		
		for (int n = 0; n < RANKS_NOEQUIV.length; n++) {
			for (int m = 0; m < RANKS_NOEQUIV.length; m++) {
				if (n == m) {
					assertTrue(RANKS_NOEQUIV[n].isEquivalent(RANKS_NOEQUIV[m]));
					assertTrue(RANKS_NOEQUIV[n].isHashEquivalent(RANKS_NOEQUIV[m]));
				} else {
					assertFalse(RANKS_NOEQUIV[n].isEquivalent(RANKS_NOEQUIV[m]));
					assertFalse(RANKS_NOEQUIV[n].isHashEquivalent(RANKS_NOEQUIV[m]));
				}
			}
		}
	}
	
	@Test
	public void isUpperLower() {
		PostRank rnk0 = new PostRank(TIME);
		PostRank rnk1 = new PostRank(TIME, new PostScore(TIME.plusMinutes(10), TIME, 3L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(5)));
		PostRank rnk2 = new PostRank(TIME, new PostScore(TIME.plusMinutes(10), TIME, 3L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(5)), 
											 new PostScore(TIME.plusMinutes(10), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(5)));
		PostRank rnk3 = new PostRank(TIME, new PostScore(TIME.plusMinutes(10), TIME, 3L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(5)), 
											 new PostScore(TIME.plusMinutes(10), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(5)), 
											 new PostScore(TIME.plusMinutes(10), TIME, 1L, 1L, "user-1", 1L, 1L, TIME.plusMinutes(5)));
		PostRank rnk4 = new PostRank(TIME, new PostScore(TIME.plusMinutes(10), TIME, 6L, 1L, "user-1", 6L, 1L, TIME.plusMinutes(5)), 
				 							 new PostScore(TIME.plusMinutes(10), TIME, 5L, 1L, "user-1", 5L, 1L, TIME.plusMinutes(5)), 
				 							 new PostScore(TIME.plusMinutes(10), TIME, 4L, 1L, "user-1", 4L, 1L, TIME.plusMinutes(5)));
		
		assertTrue(rnk4.isUpper(rnk3));
		assertTrue(rnk4.isUpper(rnk2));
		assertTrue(rnk4.isUpper(rnk1));
		assertTrue(rnk4.isUpper(rnk0));
	}
	
	@Test
	public void mergeWithUpdated() {
		PostRank rnk0 = new PostRank(TIME);
		PostRank rnk1 = new PostRank(TIME, new PostScore(TIME.plusMinutes(10), TIME, 1L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(5)));
		PostRank rnk2 = new PostRank(TIME, new PostScore(TIME.plusMinutes(10), TIME, 1L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(5)), 
											 new PostScore(TIME.plusMinutes(10), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(5)));
		PostRank rnk3 = new PostRank(TIME, new PostScore(TIME.plusMinutes(10), TIME, 1L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(5)), 
											 new PostScore(TIME.plusMinutes(10), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(5)), 
											 new PostScore(TIME.plusMinutes(10), TIME, 3L, 1L, "user-1", 1L, 1L, TIME.plusMinutes(5)));
		
		PostRank rnk0_updated = new PostRank(TIME.plusMinutes(5));
		PostRank rnk1_updated = new PostRank(TIME.plusMinutes(5), new PostScore(TIME.plusMinutes(10), TIME, 1L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(5)));
		PostRank rnk2_updated = new PostRank(TIME.plusMinutes(5), new PostScore(TIME.plusMinutes(10), TIME, 1L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(5)), 
											 new PostScore(TIME.plusMinutes(10), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(5)));
		PostRank rnk3_updated = new PostRank(TIME.plusMinutes(5), new PostScore(TIME.plusMinutes(10), TIME, 1L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(5)), 
											 new PostScore(TIME.plusMinutes(10), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(5)), 
											 new PostScore(TIME.plusMinutes(10), TIME, 3L, 1L, "user-1", 1L, 1L, TIME.plusMinutes(5)));
		
		assertTrue(rnk0.isEquivalent(rnk0_updated));
		assertTrue(rnk1.isEquivalent(rnk1_updated));
		assertTrue(rnk2.isEquivalent(rnk2_updated));
		assertTrue(rnk3.isEquivalent(rnk3_updated));
				
		rnk0.updateWith(rnk0_updated);
		rnk1.updateWith(rnk1_updated);
		rnk2.updateWith(rnk2_updated);
		rnk3.updateWith(rnk3_updated);
		
		assertTrue(rnk0.sameAs(rnk0_updated));
		assertTrue(rnk1.sameAs(rnk1_updated));
		assertTrue(rnk2.sameAs(rnk2_updated));
		assertTrue(rnk3.sameAs(rnk3_updated));
	}
	
	@Test
	public void mergeWithOutdated() {
		PostRank rnk0 = new PostRank(TIME);
		PostRank rnk1 = new PostRank(TIME, new PostScore(TIME.plusMinutes(10), TIME, 1L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(5)));
		PostRank rnk2 = new PostRank(TIME, new PostScore(TIME.plusMinutes(10), TIME, 1L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(5)), 
											 new PostScore(TIME.plusMinutes(10), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(5)));
		PostRank rnk3 = new PostRank(TIME, new PostScore(TIME.plusMinutes(10), TIME, 1L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(5)), 
											 new PostScore(TIME.plusMinutes(10), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(5)), 
											 new PostScore(TIME.plusMinutes(10), TIME, 3L, 1L, "user-1", 1L, 1L, TIME.plusMinutes(5)));
		
		PostRank rnk0_outdated = new PostRank(TIME.minusMinutes(5));
		PostRank rnk1_outdated = new PostRank(TIME.minusMinutes(5), new PostScore(TIME.plusMinutes(10), TIME, 1L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(5)));
		PostRank rnk2_outdated = new PostRank(TIME.minusMinutes(5), new PostScore(TIME.plusMinutes(10), TIME, 1L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(5)), 
											 new PostScore(TIME.plusMinutes(10), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(5)));
		PostRank rnk3_outdated = new PostRank(TIME.minusMinutes(5), new PostScore(TIME.plusMinutes(10), TIME, 1L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(5)), 
											 new PostScore(TIME.plusMinutes(10), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(5)), 
											 new PostScore(TIME.plusMinutes(10), TIME, 3L, 1L, "user-1", 1L, 1L, TIME.plusMinutes(5)));
		
		assertTrue(rnk0.isEquivalent(rnk0_outdated));
		assertTrue(rnk1.isEquivalent(rnk1_outdated));
		assertTrue(rnk2.isEquivalent(rnk2_outdated));
		assertTrue(rnk3.isEquivalent(rnk3_outdated));
				
		rnk0.updateWith(rnk0_outdated);
		rnk1.updateWith(rnk1_outdated);
		rnk2.updateWith(rnk2_outdated);
		rnk3.updateWith(rnk3_outdated);
		
		assertFalse(rnk0.sameAs(rnk0_outdated));
		assertFalse(rnk1.sameAs(rnk1_outdated));
		assertFalse(rnk2.sameAs(rnk2_outdated));
		assertFalse(rnk3.sameAs(rnk3_outdated));
	}
	
	@Test
	public void mergeWithCompleteLower() {
		PostRank rnk3 = new PostRank(TIME, new PostScore(TIME.plusMinutes(10), TIME, 1L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(5)), 
				 							 new PostScore(TIME.plusMinutes(10), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(5)), 
				 							 new PostScore(TIME.plusMinutes(10), TIME, 3L, 1L, "user-1", 1L, 1L, TIME.plusMinutes(5)));
		PostRank rnk4 = new PostRank(TIME, new PostScore(TIME.plusMinutes(10), TIME, 4L, 1L, "user-1", 6L, 1L, TIME.plusMinutes(5)), 
				 							 new PostScore(TIME.plusMinutes(10), TIME, 5L, 1L, "user-1", 5L, 1L, TIME.plusMinutes(5)), 
				 							 new PostScore(TIME.plusMinutes(10), TIME, 6L, 1L, "user-1", 4L, 1L, TIME.plusMinutes(5)));
		
		PostRank RNK4 = new PostRank(TIME, new PostScore(TIME.plusMinutes(10), TIME, 4L, 1L, "user-1", 6L, 1L, TIME.plusMinutes(5)), 
				 							 new PostScore(TIME.plusMinutes(10), TIME, 5L, 1L, "user-1", 5L, 1L, TIME.plusMinutes(5)), 
				 							 new PostScore(TIME.plusMinutes(10), TIME, 6L, 1L, "user-1", 4L, 1L, TIME.plusMinutes(5)));
				
		rnk4.updateWith(rnk3);		
		assertTrue(rnk4.sameAs(RNK4));
	}
	
	@Test
	public void mergeWithCompleteUpper() {
		PostRank rnk3 = new PostRank(TIME, new PostScore(TIME.plusMinutes(10), TIME, 1L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(5)), 
				 							 new PostScore(TIME.plusMinutes(10), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(5)), 
				 							 new PostScore(TIME.plusMinutes(10), TIME, 3L, 1L, "user-1", 1L, 1L, TIME.plusMinutes(5)));
		PostRank rnk4 = new PostRank(TIME, new PostScore(TIME.plusMinutes(10), TIME, 4L, 1L, "user-1", 6L, 1L, TIME.plusMinutes(5)), 
				 							 new PostScore(TIME.plusMinutes(10), TIME, 5L, 1L, "user-1", 5L, 1L, TIME.plusMinutes(5)), 
				 							 new PostScore(TIME.plusMinutes(10), TIME, 6L, 1L, "user-1", 4L, 1L, TIME.plusMinutes(5)));

		PostRank RNK4 = new PostRank(TIME, new PostScore(TIME.plusMinutes(10), TIME, 4L, 1L, "user-1", 6L, 1L, TIME.plusMinutes(5)), 
				 							 new PostScore(TIME.plusMinutes(10), TIME, 5L, 1L, "user-1", 5L, 1L, TIME.plusMinutes(5)), 
				 							 new PostScore(TIME.plusMinutes(10), TIME, 6L, 1L, "user-1", 4L, 1L, TIME.plusMinutes(5)));
				
		rnk3.updateWith(rnk4);	
		assertTrue(rnk3.sameAs(RNK4));
	}
	
	@Test
	public void mergeWithMoreIncomplete() {
		PostRank rnk0 = new PostRank(TIME);
		PostRank rnk1 = new PostRank(TIME, new PostScore(TIME.plusMinutes(10), TIME, 1L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(5)));
		PostRank rnk2 = new PostRank(TIME, new PostScore(TIME.plusMinutes(10), TIME, 1L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(5)), 
											 new PostScore(TIME.plusMinutes(10), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(5)));
		PostRank rnk3 = new PostRank(TIME, new PostScore(TIME.plusMinutes(10), TIME, 1L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(5)), 
											 new PostScore(TIME.plusMinutes(10), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(5)), 
											 new PostScore(TIME.plusMinutes(10), TIME, 3L, 1L, "user-1", 1L, 1L, TIME.plusMinutes(5)));
		
		PostRank RNK1 = new PostRank(TIME, new PostScore(TIME.plusMinutes(10), TIME, 1L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(5)));
		PostRank RNK2 = new PostRank(TIME, new PostScore(TIME.plusMinutes(10), TIME, 1L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(5)), 
											 new PostScore(TIME.plusMinutes(10), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(5)));
		PostRank RNK3 = new PostRank(TIME, new PostScore(TIME.plusMinutes(10), TIME, 1L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(5)), 
											 new PostScore(TIME.plusMinutes(10), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(5)), 
											 new PostScore(TIME.plusMinutes(10), TIME, 3L, 1L, "user-1", 1L, 1L, TIME.plusMinutes(5)));
				
		rnk3.updateWith(rnk2);	
		assertTrue(rnk3.sameAs(RNK3));
		
		rnk3.updateWith(rnk1);		
		assertTrue(rnk3.sameAs(RNK3));
		
		rnk3.updateWith(rnk0);		
		assertTrue(rnk3.sameAs(RNK3));
		
		rnk2.updateWith(rnk1);		
		assertTrue(rnk2.sameAs(RNK2));
		
		rnk2.updateWith(rnk0);		
		assertTrue(rnk2.sameAs(RNK2));
		
		rnk1.updateWith(rnk0);		
		assertTrue(rnk1.sameAs(RNK1));
	}
	
	@Test
	public void mergeWithMoreComplete() {
		PostRank rnk0 = new PostRank(TIME);
		PostRank rnk1 = new PostRank(TIME, new PostScore(TIME.plusMinutes(10), TIME, 1L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(5)));
		PostRank rnk2 = new PostRank(TIME, new PostScore(TIME.plusMinutes(10), TIME, 1L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(5)), 
											 new PostScore(TIME.plusMinutes(10), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(5)));
		PostRank rnk3 = new PostRank(TIME, new PostScore(TIME.plusMinutes(10), TIME, 1L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(5)), 
											 new PostScore(TIME.plusMinutes(10), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(5)), 
											 new PostScore(TIME.plusMinutes(10), TIME, 3L, 1L, "user-1", 1L, 1L, TIME.plusMinutes(5)));
		
		PostRank RNK1 = new PostRank(TIME, new PostScore(TIME.plusMinutes(10), TIME, 1L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(5)));
		PostRank RNK2 = new PostRank(TIME, new PostScore(TIME.plusMinutes(10), TIME, 1L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(5)), 
											 new PostScore(TIME.plusMinutes(10), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(5)));
		PostRank RNK3 = new PostRank(TIME, new PostScore(TIME.plusMinutes(10), TIME, 1L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(5)), 
											 new PostScore(TIME.plusMinutes(10), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(5)), 
											 new PostScore(TIME.plusMinutes(10), TIME, 3L, 1L, "user-1", 1L, 1L, TIME.plusMinutes(5)));
				
		rnk0.updateWith(rnk1);	
		assertTrue(rnk0.sameAs(RNK1));

		rnk0.updateWith(rnk2);		
		assertTrue(rnk0.sameAs(RNK2));
		
		rnk0.updateWith(rnk3);		
		assertTrue(rnk0.sameAs(RNK3));
		
		rnk1.updateWith(rnk2);		
		assertTrue(rnk1.sameAs(RNK2));
		
		rnk1.updateWith(rnk3);		
		assertTrue(rnk1.sameAs(RNK3));
		
		rnk2.updateWith(rnk3);		
		assertTrue(rnk2.sameAs(RNK3));
	}
	
	@Test
	public void serialization() {
		SerializationUtils.serialize(RANK_3);
	}

	private PostRank[] getEquivalentPostRanks() {
		PostRank ranks[] = new PostRank[32];
		
		long i = 1;
		
		ranks[0] = new PostRank(TIME.plusMinutes(1), 
				new PostScore(TIME.plusMinutes(1), TIME, 3L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1), TIME, 1L, 1L, "user-1", 1L, 1L, TIME.plusMinutes(1)));
		ranks[1] = new PostRank(TIME.plusMinutes(1+i), 
				new PostScore(TIME.plusMinutes(1), TIME, 3L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1), TIME, 1L, 1L, "user-1", 1L, 1L, TIME.plusMinutes(1)));
		ranks[2] = new PostRank(TIME.plusMinutes(1), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 3L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 1L, 1L, "user-1", 1L, 1L, TIME.plusMinutes(1)));
		ranks[3] = new PostRank(TIME.plusMinutes(1+i), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 3L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 1L, 1L, "user-1", 1L, 1L, TIME.plusMinutes(1)));
		ranks[4] = new PostRank(TIME.plusMinutes(1), 
				new PostScore(TIME.plusMinutes(1), TIME, 3L, 1L, "user-1", 3L+i, 1L, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1), TIME, 2L, 1L, "user-1", 2L+i, 1L, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1), TIME, 1L, 1L, "user-1", 1L+i, 1L, TIME.plusMinutes(1)));
		ranks[5] = new PostRank(TIME.plusMinutes(1+i), 
				new PostScore(TIME.plusMinutes(1), TIME, 3L, 1L, "user-1", 3L+i, 1L, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1), TIME, 2L, 1L, "user-1", 2L+i, 1L, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1), TIME, 1L, 1L, "user-1", 1L+i, 1L, TIME.plusMinutes(1)));
		ranks[6] = new PostRank(TIME.plusMinutes(1), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 3L, 1L, "user-1", 3L+i, 1L, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 2L, 1L, "user-1", 2L+i, 1L, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 1L, 1L, "user-1", 1L+i, 1L, TIME.plusMinutes(1)));
		ranks[7] = new PostRank(TIME.plusMinutes(1+i), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 3L, 1L, "user-1", 3L+i, 1L, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 2L, 1L, "user-1", 2L+i, 1L, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 1L, 1L, "user-1", 1L+i, 1L, TIME.plusMinutes(1)));
		ranks[8] = new PostRank(TIME.plusMinutes(1), 
				new PostScore(TIME.plusMinutes(1), TIME, 3L, 1L, "user-1", 3L, 1L+i, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1), TIME, 2L, 1L, "user-1", 2L, 1L+i, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1), TIME, 1L, 1L, "user-1", 1L, 1L+i, TIME.plusMinutes(1)));
		ranks[9] = new PostRank(TIME.plusMinutes(1+i), 
				new PostScore(TIME.plusMinutes(1), TIME, 3L, 1L, "user-1", 3L, 1L+i, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1), TIME, 2L, 1L, "user-1", 2L, 1L+i, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1), TIME, 1L, 1L, "user-1", 1L, 1L+i, TIME.plusMinutes(1)));
		ranks[10] = new PostRank(TIME.plusMinutes(1), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 3L, 1L, "user-1", 3L, 1L+i, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 2L, 1L, "user-1", 2L, 1L+i, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 1L, 1L, "user-1", 1L, 1L+i, TIME.plusMinutes(1)));
		ranks[11] = new PostRank(TIME.plusMinutes(1+i), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 3L, 1L, "user-1", 3L, 1L+i, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 2L, 1L, "user-1", 2L, 1L+i, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 1L, 1L, "user-1", 1L, 1L+i, TIME.plusMinutes(1)));
		ranks[12] = new PostRank(TIME.plusMinutes(1), 
				new PostScore(TIME.plusMinutes(1), TIME, 3L, 1L, "user-1", 3L+i, 1L+i, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1), TIME, 2L, 1L, "user-1", 2L+i, 1L+i, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1), TIME, 1L, 1L, "user-1", 1L+i, 1L+i, TIME.plusMinutes(1)));
		ranks[13] = new PostRank(TIME.plusMinutes(1+i), 
				new PostScore(TIME.plusMinutes(1), TIME, 3L, 1L, "user-1", 3L+i, 1L+i, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1), TIME, 2L, 1L, "user-1", 2L+i, 1L+i, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1), TIME, 1L, 1L, "user-1", 1L+i, 1L+i, TIME.plusMinutes(1)));
		ranks[14] = new PostRank(TIME.plusMinutes(1), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 3L, 1L, "user-1", 3L+i, 1L+i, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 2L, 1L, "user-1", 2L+i, 1L+i, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 1L, 1L, "user-1", 1L+i, 1L+i, TIME.plusMinutes(1)));
		ranks[15] = new PostRank(TIME.plusMinutes(1+i), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 3L, 1L, "user-1", 3L+i, 1L+i, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 2L, 1L, "user-1", 2L+i, 1L+i, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 1L, 1L, "user-1", 1L+i, 1L+i, TIME.plusMinutes(1)));
		ranks[16] = new PostRank(TIME.plusMinutes(1), 
				new PostScore(TIME.plusMinutes(1), TIME, 3L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(1+i)), 
				new PostScore(TIME.plusMinutes(1), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(1+i)), 
				new PostScore(TIME.plusMinutes(1), TIME, 1L, 1L, "user-1", 1L, 1L, TIME.plusMinutes(1+i)));
		ranks[17] = new PostRank(TIME.plusMinutes(1+i), 
				new PostScore(TIME.plusMinutes(1), TIME, 3L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(1+i)), 
				new PostScore(TIME.plusMinutes(1), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(1+i)), 
				new PostScore(TIME.plusMinutes(1), TIME, 1L, 1L, "user-1", 1L, 1L, TIME.plusMinutes(1+i)));
		ranks[18] = new PostRank(TIME.plusMinutes(1), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 3L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(1+i)), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(1+i)), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 1L, 1L, "user-1", 1L, 1L, TIME.plusMinutes(1+i)));
		ranks[19] = new PostRank(TIME.plusMinutes(1+i), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 3L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(1+i)), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(1+i)), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 1L, 1L, "user-1", 1L, 1L, TIME.plusMinutes(1+i)));
		ranks[20] = new PostRank(TIME.plusMinutes(1), 
				new PostScore(TIME.plusMinutes(1), TIME, 3L, 1L, "user-1", 3L+i, 1L, TIME.plusMinutes(1+i)), 
				new PostScore(TIME.plusMinutes(1), TIME, 2L, 1L, "user-1", 2L+i, 1L, TIME.plusMinutes(1+i)), 
				new PostScore(TIME.plusMinutes(1), TIME, 1L, 1L, "user-1", 1L+i, 1L, TIME.plusMinutes(1+i)));
		ranks[21] = new PostRank(TIME.plusMinutes(1+i), 
				new PostScore(TIME.plusMinutes(1), TIME, 3L, 1L, "user-1", 3L+i, 1L, TIME.plusMinutes(1+i)), 
				new PostScore(TIME.plusMinutes(1), TIME, 2L, 1L, "user-1", 2L+i, 1L, TIME.plusMinutes(1+i)), 
				new PostScore(TIME.plusMinutes(1), TIME, 1L, 1L, "user-1", 1L+i, 1L, TIME.plusMinutes(1+i)));
		ranks[22] = new PostRank(TIME.plusMinutes(1), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 3L, 1L, "user-1", 3L+i, 1L, TIME.plusMinutes(1+i)), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 2L, 1L, "user-1", 2L+i, 1L, TIME.plusMinutes(1+i)), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 1L, 1L, "user-1", 1L+i, 1L, TIME.plusMinutes(1+i)));
		ranks[23] = new PostRank(TIME.plusMinutes(1+i), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 3L, 1L, "user-1", 3L+i, 1L, TIME.plusMinutes(1+i)), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 2L, 1L, "user-1", 2L+i, 1L, TIME.plusMinutes(1+i)), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 1L, 1L, "user-1", 1L+i, 1L, TIME.plusMinutes(1+i)));
		ranks[24] = new PostRank(TIME.plusMinutes(1), 
				new PostScore(TIME.plusMinutes(1), TIME, 3L, 1L, "user-1", 3L, 1L+i, TIME.plusMinutes(1+i)), 
				new PostScore(TIME.plusMinutes(1), TIME, 2L, 1L, "user-1", 2L, 1L+i, TIME.plusMinutes(1+i)), 
				new PostScore(TIME.plusMinutes(1), TIME, 1L, 1L, "user-1", 1L, 1L+i, TIME.plusMinutes(1+i)));		
		ranks[25] = new PostRank(TIME.plusMinutes(1+i), 
				new PostScore(TIME.plusMinutes(1), TIME, 3L, 1L, "user-1", 3L, 1L+i, TIME.plusMinutes(1+i)), 
				new PostScore(TIME.plusMinutes(1), TIME, 2L, 1L, "user-1", 2L, 1L+i, TIME.plusMinutes(1+i)), 
				new PostScore(TIME.plusMinutes(1), TIME, 1L, 1L, "user-1", 1L, 1L+i, TIME.plusMinutes(1+i)));	
		ranks[26] = new PostRank(TIME.plusMinutes(1), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 3L, 1L, "user-1", 3L, 1L+i, TIME.plusMinutes(1+i)), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 2L, 1L, "user-1", 2L, 1L+i, TIME.plusMinutes(1+i)), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 1L, 1L, "user-1", 1L, 1L+i, TIME.plusMinutes(1+i)));	
		ranks[27] = new PostRank(TIME.plusMinutes(1+i), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 3L, 1L, "user-1", 3L, 1L+i, TIME.plusMinutes(1+i)), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 2L, 1L, "user-1", 2L, 1L+i, TIME.plusMinutes(1+i)), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 1L, 1L, "user-1", 1L, 1L+i, TIME.plusMinutes(1+i)));
		ranks[28] = new PostRank(TIME.plusMinutes(1), 
				new PostScore(TIME.plusMinutes(1), TIME, 3L, 1L, "user-1", 3L+i, 1L+i, TIME.plusMinutes(1+i)), 
				new PostScore(TIME.plusMinutes(1), TIME, 2L, 1L, "user-1", 2L+i, 1L+i, TIME.plusMinutes(1+i)), 
				new PostScore(TIME.plusMinutes(1), TIME, 1L, 1L, "user-1", 1L+i, 1L+i, TIME.plusMinutes(1+i)));
		ranks[29] = new PostRank(TIME.plusMinutes(1+i), 
				new PostScore(TIME.plusMinutes(1), TIME, 3L, 1L, "user-1", 3L+i, 1L+i, TIME.plusMinutes(1+i)), 
				new PostScore(TIME.plusMinutes(1), TIME, 2L, 1L, "user-1", 2L+i, 1L+i, TIME.plusMinutes(1+i)), 
				new PostScore(TIME.plusMinutes(1), TIME, 1L, 1L, "user-1", 1L+i, 1L+i, TIME.plusMinutes(1+i)));		
		ranks[30] = new PostRank(TIME.plusMinutes(1), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 3L, 1L, "user-1", 3L+i, 1L+i, TIME.plusMinutes(1+i)), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 2L, 1L, "user-1", 2L+i, 1L+i, TIME.plusMinutes(1+i)), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 1L, 1L, "user-1", 1L+i, 1L+i, TIME.plusMinutes(1+i)));
		ranks[31] = new PostRank(TIME.plusMinutes(1+i), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 3L, 1L, "user-1", 3L+i, 1L+i, TIME.plusMinutes(1+i)), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 2L, 1L, "user-1", 2L+i, 1L+i, TIME.plusMinutes(1+i)), 
				new PostScore(TIME.plusMinutes(1+i), TIME, 1L, 1L, "user-1", 1L+i, 1L+i, TIME.plusMinutes(1+i)));		
		
		return ranks;
	}
	
	public PostRank[] getNotEquivalentPostRanks() {
		PostRank ranks[] = new PostRank[8];
		
		long i = 1;
		
		ranks[0] = new PostRank(TIME.plusMinutes(1), 
				new PostScore(TIME.plusMinutes(1), TIME, 3L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1), TIME, 1L, 1L, "user-1", 1L, 1L, TIME.plusMinutes(1)));
		ranks[1] = new PostRank(TIME.plusMinutes(1), 
				new PostScore(TIME.plusMinutes(1), TIME, 3L+i, 1L+i, "user-1", 3L, 1L, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1), TIME, 1L, 1L, "user-1", 1L, 1L, TIME.plusMinutes(1)));	
		ranks[2] = new PostRank(TIME.plusMinutes(1), 
				new PostScore(TIME.plusMinutes(1), TIME, 3L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1), TIME, 2L+i, 1L+i, "user-1", 2L, 1L, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1), TIME, 1L, 1L, "user-1", 1L, 1L, TIME.plusMinutes(1)));	
		ranks[3] = new PostRank(TIME.plusMinutes(1), 
				new PostScore(TIME.plusMinutes(1), TIME, 3L+i, 1L+i, "user-1", 3L, 1L, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1), TIME, 2L+i, 1L+i, "user-1", 2L, 1L, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1), TIME, 1L, 1L, "user-1", 1L, 1L, TIME.plusMinutes(1)));	
		ranks[4] = new PostRank(TIME.plusMinutes(1), 
				new PostScore(TIME.plusMinutes(1), TIME, 3L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1), TIME, 1L+i, 1L+i, "user-1", 1L, 1L, TIME.plusMinutes(1)));	
		ranks[5] = new PostRank(TIME.plusMinutes(1), 
				new PostScore(TIME.plusMinutes(1), TIME, 3L+i, 1L+i, "user-1", 3L, 1L, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1), TIME, 2L, 1L, "user-1", 2L, 1L, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1), TIME, 1L+i, 1L+i, "user-1", 1L, 1L, TIME.plusMinutes(1)));	
		ranks[6] = new PostRank(TIME.plusMinutes(1), 
				new PostScore(TIME.plusMinutes(1), TIME, 3L, 1L, "user-1", 3L, 1L, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1), TIME, 2L+i, 1L+i, "user-1", 2L, 1L, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1), TIME, 1L+i, 1L+i, "user-1", 1L, 1L, TIME.plusMinutes(1)));	
		ranks[7] = new PostRank(TIME.plusMinutes(1), 
				new PostScore(TIME.plusMinutes(1), TIME, 3L+i, 1L+i, "user-1", 3L, 1L, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1), TIME, 2L+i, 1L+i, "user-1", 2L, 1L, TIME.plusMinutes(1)), 
				new PostScore(TIME.plusMinutes(1), TIME, 1L+i, 1L+i, "user-1", 1L, 1L, TIME.plusMinutes(1)));	
		
		return ranks;
	}

}
