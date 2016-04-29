package com.threecore.project.model;

import static org.junit.Assert.*;

import org.apache.commons.lang3.SerializationUtils;
import org.joda.time.DateTime;
import org.junit.Test;

import com.threecore.project.SimpleTest;
import com.threecore.project.model.score.post.comparator.PostScoreComparatorAsc;
import com.threecore.project.model.score.post.comparator.PostScoreComparatorDesc;

public class TestPostScore extends SimpleTest {
	
	private static final DateTime TIME = CommonsPost.TIME;	
	
	// Post creation: same score; different creation_ts; most recent wins.
	private static final PostScore SCORE_INIT_1 = new PostScore(TIME.plusMinutes(10), TIME, 1L, 1L, "user-1", 10L, 0L, ModelCommons.UNDEFINED_LDT);
	private static final PostScore SCORE_INIT_2 = new PostScore(TIME.plusMinutes(10), TIME.plusMinutes(5), 2L, 1L, "user-1", 10L, 0L, ModelCommons.UNDEFINED_LDT);
	
	// Post scoring: different score; everything else does not matter; the top scored wins.
	private static final PostScore SCORE_1 = new PostScore(TIME.plusMinutes(10), TIME, 1L, 1L, "user-1", 20L, 1L, TIME.plusMinutes(5));
	private static final PostScore SCORE_2 = new PostScore(TIME.plusMinutes(10), TIME, 2L, 1L, "user-1", 30L, 3L, TIME.plusMinutes(5));
	
	// Post comments: same creation_ts, same score, same commenters; different last_comment_ts; most recently commented wins.
	private static final PostScore SCORE_COMMENT_1 = new PostScore(TIME.plusMinutes(10), TIME, 1L, 1L, "user-1", 20L, 1L, TIME.plusMinutes(5));
	private static final PostScore SCORE_COMMENT_2 = new PostScore(TIME.plusMinutes(10), TIME, 2L, 1L, "user-1", 20L, 1L, TIME.plusMinutes(10));

	@Test
	public void comparison() {
		PostScoreComparatorAsc casc = PostScoreComparatorAsc.getInstance();
		PostScoreComparatorDesc cdesc = PostScoreComparatorDesc.getInstance();
		
		assertEquals(1, SCORE_INIT_2.compareTo(SCORE_INIT_1));
		assertEquals(1, SCORE_2.compareTo(SCORE_1));
		assertEquals(1, SCORE_COMMENT_2.compareTo(SCORE_COMMENT_1));
		
		assertEquals(1, casc.compare(SCORE_INIT_2, SCORE_INIT_1));
		assertEquals(1, casc.compare(SCORE_2, SCORE_1));
		assertEquals(1, casc.compare(SCORE_COMMENT_2, SCORE_COMMENT_1));
		
		assertEquals(-1, cdesc.compare(SCORE_INIT_2, SCORE_INIT_1));
		assertEquals(-1, cdesc.compare(SCORE_2, SCORE_1));
		assertEquals(-1, cdesc.compare(SCORE_COMMENT_2, SCORE_COMMENT_1));
	}
	
	@Test
	public void serialization() {
		SerializationUtils.serialize(SCORE_1);
	}

}
