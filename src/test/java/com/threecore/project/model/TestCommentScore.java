package com.threecore.project.model;

import static org.junit.Assert.*;

import org.apache.commons.lang3.SerializationUtils;
import org.joda.time.DateTime;
import org.junit.Test;

import com.threecore.project.SimpleTest;
import com.threecore.project.model.score.comment.comparator.CommentScoreComparatorAsc;
import com.threecore.project.model.score.comment.comparator.CommentScoreComparatorDesc;

public class TestCommentScore extends SimpleTest {
	
	private static final DateTime TIME = CommonsPost.TIME;	
	
	// Comment creation: same score; different creation_ts; most recent wins.
	private static final CommentScore SCORE_INIT_1 = new CommentScore(TIME.plusMinutes(10), 201, "CMT-201", 0);
	private static final CommentScore SCORE_INIT_2 = new CommentScore(TIME.plusMinutes(20), 202, "CMT-202", 0);
	
	// Comment scoring: different score; everything else does not matter; the top scored wins.
	private static final CommentScore SCORE_1 = new CommentScore(TIME.plusMinutes(20), 202, "CMT-202", 0);
	private static final CommentScore SCORE_2 = new CommentScore(TIME.plusMinutes(10), 201, "CMT-201", 1);
	
	// Comment liked: same creation_ts, same score; different comments; lexicographical order applied.
	private static final CommentScore SCORE_COMMENT_1 = new CommentScore(TIME.plusMinutes(10), 201, "ACMT", 1);
	private static final CommentScore SCORE_COMMENT_2 = new CommentScore(TIME.plusMinutes(10), 202, "BCMT", 1);

	@Test
	public void comparison() {
		CommentScoreComparatorAsc casc = CommentScoreComparatorAsc.getInstance();
		CommentScoreComparatorDesc cdesc = CommentScoreComparatorDesc.getInstance();
		
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
