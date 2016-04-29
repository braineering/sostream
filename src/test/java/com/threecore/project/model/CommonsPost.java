package com.threecore.project.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class CommonsPost {	
	
	public static final DateTime TIME = new DateTime(2016, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC);
	public static final int RNKSIZE = 3;

	public static List<PostScore> getPostCreation(final long numposts) {
		List<PostScore> scores = new ArrayList<PostScore>();
		
		DateTime time;
		DateTime post_creation_ts;
		
		int eventno = 0;
		
		for (long post_id = 0; post_id < numposts; post_id++) {
			time = TIME.plusMinutes(5 * eventno);
			post_creation_ts = time;
			PostScore score = new PostScore(time, post_creation_ts, post_id, 1L, "user-1", 10L, 0L, ModelCommons.UNDEFINED_LDT);
			scores.add(score);
			eventno++;
		}		
		
		return scores;
	}
	
	public static List<PostScore> getPostCreationAndComment(final long numposts) {
		List<PostScore> scores = new ArrayList<PostScore>();
		Map<Long, DateTime> creationTime = new HashMap<Long, DateTime>();
		
		DateTime time;
		DateTime post_creation_ts;
		DateTime last_comment_ts;
		
		int eventno = 0;
		
		for (long post_id = 0; post_id < numposts; post_id++) {
			time = TIME.plusMinutes(5 * eventno);
			post_creation_ts = time;
			creationTime.put(post_id, post_creation_ts);
			PostScore score = new PostScore(time, post_creation_ts, post_id, 1L, "user-1", 10L, 0L, ModelCommons.UNDEFINED_LDT);
			scores.add(score);
			eventno++;
		}		
		
		for (long post_id = 0; post_id < numposts; post_id++) {
			time = TIME.plusMinutes(5 * eventno);
			post_creation_ts = creationTime.get(post_id);
			last_comment_ts = time;
			long commenters = numposts - post_id;
			PostScore score = new PostScore(time, creationTime.get(post_id), post_id, 1L, "user-1", 10L + (10L * commenters), commenters, last_comment_ts);
			scores.add(score);
			eventno++;
		}
		
		return scores;
	}
	
	public static List<PostScore> getPostCreationAndDeletionOutsideRank(final long numposts) {
		List<PostScore> scores = new ArrayList<PostScore>();
		Map<Long, DateTime> creationTime = new HashMap<Long, DateTime>();
		
		DateTime time;
		DateTime post_creation_ts;
		DateTime last_comment_ts;
		
		int eventno = 0;
		
		for (long post_id = 0; post_id < numposts; post_id++) {
			time = TIME.plusMinutes(5 * eventno);
			post_creation_ts = time;
			creationTime.put(post_id, post_creation_ts);
			PostScore score = new PostScore(time, post_creation_ts, post_id, 1L, "user-1", 10L, 0L, ModelCommons.UNDEFINED_LDT);
			scores.add(score);
			eventno++;
		}		
		
		for (long post_id = 0; post_id < numposts; post_id++) {
			time = TIME.plusMinutes(5 * eventno);
			post_creation_ts = creationTime.get(post_id);
			last_comment_ts = time;
			long commenters = numposts - post_id;
			PostScore score = new PostScore(time, creationTime.get(post_id), post_id, 1L, "user-1", 0L, commenters, last_comment_ts);
			scores.add(score);
			eventno++;
		}
		
		return scores;
	}
	
	public static List<PostScore> getPostCreationAndDeletionInsideRank(final long numposts) {
		List<PostScore> scores = new ArrayList<PostScore>();
		Map<Long, DateTime> creationTime = new HashMap<Long, DateTime>();
		
		DateTime time = TIME;
		DateTime post_creation_ts;
		DateTime last_comment_ts;
		
		int eventno = 0;
		
		for (long post_id = 0; post_id < numposts; post_id++) {
			time = TIME.plusMinutes(5 * eventno);
			post_creation_ts = time;
			creationTime.put(post_id, post_creation_ts);
			PostScore score = new PostScore(time, post_creation_ts, post_id, 1L, "user-1", 10L, 0L, ModelCommons.UNDEFINED_LDT);
			scores.add(score);
			eventno++;
		}		
		
		for (long post_id = numposts - 1; post_id >= 0; post_id--) {
			time = TIME.plusMinutes(5 * eventno);
			post_creation_ts = creationTime.get(post_id);
			last_comment_ts = time;
			long commenters = numposts - post_id;
			PostScore score = new PostScore(time, creationTime.get(post_id), post_id, 1L, "user-1", 0L, commenters, last_comment_ts);
			scores.add(score);
			eventno++;
		}
		
		return scores;
	}

	public static PostRank[] getEquivalentPostRanks() {
		PostRank ranks[] = new PostRank[32];
		
		int i = 1;
		
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
	
	public static PostRank[] getNotEquivalentPostRanks() {
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
