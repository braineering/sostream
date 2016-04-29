package com.threecore.project.model;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class CommonsComment {
	
	public static final DateTime TIME = new DateTime(2016, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC);
	public static final int RNKSIZE = 3;
	
	public static List<CommentScore> getCommentCreation(final long numcomments) {
		List<CommentScore> scores = new ArrayList<CommentScore>();
		
		DateTime time;
		
		int eventno = 0;
		
		for (long comment_id = 0; comment_id < numcomments; comment_id++) {
			time = TIME.plusMinutes(5 * eventno);
			CommentScore score = new CommentScore(time, comment_id, "CMT-"+comment_id, 0);
			scores.add(score);
			eventno++;
		}		
		
		return scores;
	}
	
	/*
	public static List<CommentScore> getCreationAndLike(final long numposts) {
		List<CommentScore> scores = new ArrayList<CommentScore>();
		Map<Long, LocalDateTime> creationTime = new HashMap<Long, LocalDateTime>();
		
		LocalDateTime time;
		LocalDateTime post_creation_ts;
		LocalDateTime last_comment_ts;
		
		int eventno = 0;
		
		for (long post_id = 0; post_id < numposts; post_id++) {
			time = TIME.plusMinutes(5 * eventno);
			post_creation_ts = time;
			creationTime.put(post_id, post_creation_ts);
			CommentScore score = new CommentScore(time, post_creation_ts, post_id, 1L, "user-1", 10L, 0L, ModelCommons.UNDEFINED_LDT);
			CommentScore score = new CommentScore(time, post_id, "CMT-"+post_id, 0);
			scores.add(score);
			eventno++;
		}		
		
		for (long post_id = 0; post_id < numposts; post_id++) {
			time = TIME.plusMinutes(5 * eventno);
			post_creation_ts = creationTime.get(post_id);
			last_comment_ts = time;
			long commenters = numposts - post_id;
			CommentScore score = new CommentScore(time, creationTime.get(post_id), post_id, 1L, "user-1", 10L + (10L * commenters), commenters, last_comment_ts);
			scores.add(score);
			eventno++;
		}
		
		return scores;
	}
	
	public static List<CommentScore> getCommentCreationAndDeletionOutsideRank(final long numposts) {
		List<CommentScore> scores = new ArrayList<CommentScore>();
		Map<Long, LocalDateTime> creationTime = new HashMap<Long, LocalDateTime>();
		
		LocalDateTime time;
		LocalDateTime post_creation_ts;
		LocalDateTime last_comment_ts;
		
		int eventno = 0;
		
		for (long post_id = 0; post_id < numposts; post_id++) {
			time = TIME.plusMinutes(5 * eventno);
			post_creation_ts = time;
			creationTime.put(post_id, post_creation_ts);
			CommentScore score = new CommentScore(time, post_creation_ts, post_id, 1L, "user-1", 10L, 0L, ModelCommons.UNDEFINED_LDT);
			scores.add(score);
			eventno++;
		}		
		
		for (long post_id = 0; post_id < numposts; post_id++) {
			time = TIME.plusMinutes(5 * eventno);
			post_creation_ts = creationTime.get(post_id);
			last_comment_ts = time;
			long commenters = numposts - post_id;
			CommentScore score = new CommentScore(time, creationTime.get(post_id), post_id, 1L, "user-1", 0L, commenters, last_comment_ts);
			scores.add(score);
			eventno++;
		}
		
		return scores;
	}
	
	public static List<CommentScore> getCommentCreationAndDeletionInsideRank(final long numposts) {
		List<CommentScore> scores = new ArrayList<CommentScore>();
		Map<Long, LocalDateTime> creationTime = new HashMap<Long, LocalDateTime>();
		
		LocalDateTime time = TIME;
		LocalDateTime post_creation_ts;
		LocalDateTime last_comment_ts;
		
		int eventno = 0;
		
		for (long post_id = 0; post_id < numposts; post_id++) {
			time = TIME.plusMinutes(5 * eventno);
			post_creation_ts = time;
			creationTime.put(post_id, post_creation_ts);
			CommentScore score = new CommentScore(time, post_creation_ts, post_id, 1L, "user-1", 10L, 0L, ModelCommons.UNDEFINED_LDT);
			scores.add(score);
			eventno++;
		}		
		
		for (long post_id = numposts - 1; post_id >= 0; post_id--) {
			time = TIME.plusMinutes(5 * eventno);
			post_creation_ts = creationTime.get(post_id);
			last_comment_ts = time;
			long commenters = numposts - post_id;
			CommentScore score = new CommentScore(time, creationTime.get(post_id), post_id, 1L, "user-1", 0L, commenters, last_comment_ts);
			scores.add(score);
			eventno++;
		}
		
		return scores;
	}
	*/
	
	public static CommentRank[] getEquivalentCommentRanks() {
		CommentRank ranks[] = new CommentRank[8];
		
		int i = 1;
		
		ranks[0] = new CommentRank(RNKSIZE, TIME.plusMinutes(1), 
				new CommentScore(TIME.plusMinutes(1), 1, "CMT", 1), 
				new CommentScore(TIME.plusMinutes(1), 2, "CMT", 1),
				new CommentScore(TIME.plusMinutes(1), 3, "CMT", 1));
		
		ranks[1] = new CommentRank(RNKSIZE, TIME.plusMinutes(1+i), 
				new CommentScore(TIME.plusMinutes(1), 1, "CMT", 1), 
				new CommentScore(TIME.plusMinutes(1), 2, "CMT", 1),
				new CommentScore(TIME.plusMinutes(1), 3, "CMT", 1));
		
		ranks[2] = new CommentRank(RNKSIZE, TIME.plusMinutes(1), 
				new CommentScore(TIME.plusMinutes(1+i), 1, "CMT", 1), 
				new CommentScore(TIME.plusMinutes(1+i), 2, "CMT", 1),
				new CommentScore(TIME.plusMinutes(1+i), 3, "CMT", 1));
		
		ranks[3] = new CommentRank(RNKSIZE, TIME.plusMinutes(1+i), 
				new CommentScore(TIME.plusMinutes(1+i), 1, "CMT", 1), 
				new CommentScore(TIME.plusMinutes(1+i), 2, "CMT", 1),
				new CommentScore(TIME.plusMinutes(1+i), 3, "CMT", 1));
		
		ranks[4] = new CommentRank(RNKSIZE, TIME.plusMinutes(1), 
				new CommentScore(TIME.plusMinutes(1), 1, "CMT", 1+i), 
				new CommentScore(TIME.plusMinutes(1), 2, "CMT", 1+i),
				new CommentScore(TIME.plusMinutes(1), 3, "CMT", 1+i));
		
		ranks[5] = new CommentRank(RNKSIZE, TIME.plusMinutes(1+i), 
				new CommentScore(TIME.plusMinutes(1), 1, "CMT", 1+i), 
				new CommentScore(TIME.plusMinutes(1), 2, "CMT", 1+i),
				new CommentScore(TIME.plusMinutes(1), 3, "CMT", 1+i));
		
		ranks[6] = new CommentRank(RNKSIZE, TIME.plusMinutes(1), 
				new CommentScore(TIME.plusMinutes(1+i), 1, "CMT", 1+i), 
				new CommentScore(TIME.plusMinutes(1+i), 2, "CMT", 1+i),
				new CommentScore(TIME.plusMinutes(1+i), 3, "CMT", 1+i));
		
		ranks[7] = new CommentRank(RNKSIZE, TIME.plusMinutes(1+i), 
				new CommentScore(TIME.plusMinutes(1+i), 1, "CMT", 1+i), 
				new CommentScore(TIME.plusMinutes(1+i), 2, "CMT", 1+i),
				new CommentScore(TIME.plusMinutes(1+i), 3, "CMT", 1+i));		
		
		return ranks;
	}

	public static CommentRank[] getNotEquivalentCommentRank() {
		CommentRank ranks[] = new CommentRank[2];
		
		long i = 1;
		
		ranks[0] = new CommentRank(RNKSIZE, TIME.plusMinutes(1), 
				new CommentScore(TIME.plusMinutes(1), 1, "CMT", 1), 
				new CommentScore(TIME.plusMinutes(1), 2, "CMT", 1),
				new CommentScore(TIME.plusMinutes(1), 3, "CMT", 1));
		
		ranks[1] = new CommentRank(RNKSIZE, TIME.plusMinutes(1), 
				new CommentScore(TIME.plusMinutes(1), 1+i, "CMT", 1), 
				new CommentScore(TIME.plusMinutes(1), 2+i, "CMT", 1),
				new CommentScore(TIME.plusMinutes(1), 3+i, "CMT", 1));
		
		return ranks;
	}

	public static List<CommentScore> getCreationAndLike(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	public static List<CommentScore> getCommentCreationAndDeletionOutsideRank(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	public static List<CommentScore> getCommentCreationAndDeletionInsideRank(int i) {
		// TODO Auto-generated method stub
		return null;
	}

}
