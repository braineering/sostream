package com.threecore.project.tool.data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.threecore.project.model.PostRank;
import com.threecore.project.model.PostScore;

public final class PostRankData {
	
	public static final int TYPE_0 = 0;
	public static final int TYPE_A = 1;
	public static final int TYPE_B = 2;
	public static final int TYPE_C = 3;
	public static final int TYPE_D = 4;
	/*
	public static final List<PostRank> get(final int type) {
		switch (type) {
		case TYPE_0:
			return getDefault();
		case TYPE_A:
			return getA();
		case TYPE_B:
			return getB();
		case TYPE_C:
			return getC();
		case TYPE_D:
			return getD();
		default:
			return new ArrayList<PostRank>();
		}
	}
	*/
	
	public static final List<PostRank> getDefault() {
		List<PostRank> ranks = new ArrayList<PostRank>();
		
		LocalDateTime time = LocalDateTime.of(2016, 1, 1, 12, 0);
		
		PostRank rnk1 = new PostRank(time.plusMinutes(10), 
				new PostScore(time.plusMinutes(10), time, 1L, 1L, 3L, 1L, time.plusMinutes(5)));
		
		PostRank rnk2 = new PostRank(time.plusMinutes(20), 
				new PostScore(time.plusMinutes(10), time, 1L, 1L, 3L, 1L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 2L, 1L, 2L, 1L, time.plusMinutes(5)));
		
		PostRank rnk3 = new PostRank(time.plusMinutes(30), 
				new PostScore(time.plusMinutes(10), time, 1L, 1L, 3L, 1L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 2L, 1L, 2L, 1L, time.plusMinutes(5)));
		
		PostRank rnk4 = new PostRank(time.plusMinutes(40), 
				new PostScore(time.plusMinutes(10), time, 1L, 1L, 3L, 1L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 2L, 1L, 2L, 1L, time.plusMinutes(5)));
		
		PostRank rnk5 = new PostRank(time.plusMinutes(50), 
				new PostScore(time.plusMinutes(10), time, 1L, 1L, 3L, 1L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 2L, 1L, 2L, 1L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 3L, 1L, 1L, 1L, time.plusMinutes(5)));
		
		PostRank rnk6 = new PostRank(time.plusMinutes(60), 
				new PostScore(time.plusMinutes(10), time, 1L, 1L, 3L, 1L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 2L, 1L, 2L, 1L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 3L, 1L, 1L, 1L, time.plusMinutes(5)));
	
		ranks.add(rnk1);
		ranks.add(rnk2);
		ranks.add(rnk3);
		ranks.add(rnk4);
		ranks.add(rnk5);
		ranks.add(rnk6);
		
		return ranks;
	}	
	
	/*
	
	public static final List<PostRank> getA() {
		List<PostRank> ranks = new ArrayList<PostRank>();
		
		LocalDateTime time = LocalDateTime.of(2016, 1, 1, 12, 0);
		
		PostRank rnk1 = new PostRank(time.plusMinutes(10), 
				new PostScore(time.plusMinutes(10), time, 1L, 1L, 100L, 100L, time.plusMinutes(5)));
		
		PostRank rnk2 = new PostRank(time.plusMinutes(14), 
				new PostScore(time.plusMinutes(10), time, 1L, 1L, 100L, 100L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 2L, 1L, 50L, 50L, time.plusMinutes(5)));
		
		PostRank rnk3 = new PostRank(time.plusMinutes(18), 
				new PostScore(time.plusMinutes(10), time, 1L, 1L, 100L, 100L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 2L, 1L, 50L, 50L, time.plusMinutes(5)));
		
		PostRank rnk4 = new PostRank(time.plusMinutes(22), 
				new PostScore(time.plusMinutes(10), time, 1L, 1L, 100L, 100L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 2L, 1L, 50L, 50L, time.plusMinutes(5)));
		
		PostRank rnk5 = new PostRank(time.plusMinutes(26), 
				new PostScore(time.plusMinutes(10), time, 1L, 1L, 100L, 100L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 2L, 1L, 50L, 50L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 3L, 1L, 25L, 25L, time.plusMinutes(5)));
		
		PostRank rnk6 = new PostRank(time.plusMinutes(30), 
				new PostScore(time.plusMinutes(10), time, 1L, 1L, 100L, 100L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 2L, 1L, 50L, 50L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 3L, 1L, 25L, 25L, time.plusMinutes(5)));
	
		ranks.add(rnk1);
		ranks.add(rnk2);
		ranks.add(rnk3);
		ranks.add(rnk4);
		ranks.add(rnk5);
		ranks.add(rnk6);
		
		return ranks;
	}
	
	public static final List<PostRank> getB() {
		List<PostRank> ranks = new ArrayList<PostRank>();
		
		LocalDateTime time = LocalDateTime.of(2016, 1, 1, 12, 0);
		
		PostRank rnk1 = new PostRank(time.plusMinutes(11), 
				new PostScore(time.plusMinutes(10), time, 4L, 1L, 90L, 90L, time.plusMinutes(5)));
		
		PostRank rnk2 = new PostRank(time.plusMinutes(15), 
				new PostScore(time.plusMinutes(10), time, 4L, 1L, 90L, 90L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 5L, 1L, 45L, 45L, time.plusMinutes(5)));
		
		PostRank rnk3 = new PostRank(time.plusMinutes(19), 
				new PostScore(time.plusMinutes(10), time, 4L, 1L, 90L, 90L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 5L, 1L, 45L, 45L, time.plusMinutes(5)));
		
		PostRank rnk4 = new PostRank(time.plusMinutes(23), 
				new PostScore(time.plusMinutes(10), time, 4L, 1L, 90L, 90L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 5L, 1L, 45L, 45L, time.plusMinutes(5)));
		
		PostRank rnk5 = new PostRank(time.plusMinutes(27), 
				new PostScore(time.plusMinutes(10), time, 4L, 1L, 90L, 90L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 5L, 1L, 45L, 45L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 6L, 1L, 27L, 27L, time.plusMinutes(5)));
		
		PostRank rnk6 = new PostRank(time.plusMinutes(31), 
				new PostScore(time.plusMinutes(10), time, 4L, 1L, 90L, 90L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 5L, 1L, 45L, 45L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 6L, 1L, 27L, 27L, time.plusMinutes(5)));
	
		ranks.add(rnk1);
		ranks.add(rnk2);
		ranks.add(rnk3);
		ranks.add(rnk4);
		ranks.add(rnk5);
		ranks.add(rnk6);
		
		return ranks;
	}
	
	public static final List<PostRank> getC() {
		List<PostRank> ranks = new ArrayList<PostRank>();
		
		LocalDateTime time = LocalDateTime.of(2016, 1, 1, 12, 0);
		
		PostRank rnk1 = new PostRank(time.plusMinutes(12), 
				new PostScore(time.plusMinutes(10), time, 7L, 1L, 80L, 80L, time.plusMinutes(5)));
		
		PostRank rnk2 = new PostRank(time.plusMinutes(16), 
				new PostScore(time.plusMinutes(10), time, 7L, 1L, 80L, 80L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 8L, 1L, 40L, 40L, time.plusMinutes(5)));
		
		PostRank rnk3 = new PostRank(time.plusMinutes(20), 
				new PostScore(time.plusMinutes(10), time, 7L, 1L, 80L, 80L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 8L, 1L, 40L, 40L, time.plusMinutes(5)));
		
		PostRank rnk4 = new PostRank(time.plusMinutes(24), 
				new PostScore(time.plusMinutes(10), time, 7L, 1L, 80L, 80L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 8L, 1L, 40L, 40L, time.plusMinutes(5)));
		
		PostRank rnk5 = new PostRank(time.plusMinutes(28), 
				new PostScore(time.plusMinutes(10), time, 7L, 1L, 80L, 80L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 8L, 1L, 40L, 40L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 9L, 1L, 20L, 20L, time.plusMinutes(5)));
		
		PostRank rnk6 = new PostRank(time.plusMinutes(32), 
				new PostScore(time.plusMinutes(10), time, 7L, 1L, 80L, 80L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 8L, 1L, 40L, 40L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 9L, 1L, 20L, 20L, time.plusMinutes(5)));
	
		ranks.add(rnk1);
		ranks.add(rnk2);
		ranks.add(rnk3);
		ranks.add(rnk4);
		ranks.add(rnk5);
		ranks.add(rnk6);
		
		return ranks;
	}
	
	public static final List<PostRank> getD() {
		List<PostRank> ranks = new ArrayList<PostRank>();
		
		LocalDateTime time = LocalDateTime.of(2016, 1, 1, 12, 0);
		
		PostRank rnk1 = new PostRank(time.plusMinutes(13), 
				new PostScore(time.plusMinutes(10), time, 10L, 1L, 70L, 70L, time.plusMinutes(5)));
		
		PostRank rnk2 = new PostRank(time.plusMinutes(17), 
				new PostScore(time.plusMinutes(10), time, 10L, 1L, 70L, 70L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 11L, 1L, 35L, 35L, time.plusMinutes(5)));
		
		PostRank rnk3 = new PostRank(time.plusMinutes(21), 
				new PostScore(time.plusMinutes(10), time, 10L, 1L, 70L, 70L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 11L, 1L, 35L, 35L, time.plusMinutes(5)));
		
		PostRank rnk4 = new PostRank(time.plusMinutes(25), 
				new PostScore(time.plusMinutes(10), time, 10L, 1L, 70L, 70L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 11L, 1L, 35L, 35L, time.plusMinutes(5)));
		
		PostRank rnk5 = new PostRank(time.plusMinutes(29), 
				new PostScore(time.plusMinutes(10), time, 10L, 1L, 70L, 70L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 11L, 1L, 35L, 35L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 12L, 1L, 17L, 17L, time.plusMinutes(5)));
		
		PostRank rnk6 = new PostRank(time.plusMinutes(33), 
				new PostScore(time.plusMinutes(10), time, 10L, 1L, 70L, 70L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 11L, 1L, 35L, 35L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 12L, 1L, 17L, 17L, time.plusMinutes(5)));
	
		ranks.add(rnk1);
		ranks.add(rnk2);
		ranks.add(rnk3);
		ranks.add(rnk4);
		ranks.add(rnk5);
		ranks.add(rnk6);
		
		return ranks;
	}*/

}
