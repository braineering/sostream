package com.threecore.project.control.stream;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import com.threecore.project.model.PostRank;
import com.threecore.project.model.PostScore;
import com.threecore.project.model.conf.AppConfiguration;
import com.threecore.project.operator.source.test.PostRankSource;
import com.threecore.project.operator.time.AscendingTimestamper;

@Deprecated
public class PostRankStreamgen {
	
	public static DataStream<PostRank> getPostRankStream(StreamExecutionEnvironment env, AppConfiguration config) {
		List<PostRank> list = PostRankStreamgen.getPostRankList();		
		
		DataStream<PostRank> ranks = env.addSource(new PostRankSource(list));	
		
		ranks.assignTimestampsAndWatermarks(new AscendingTimestamper<PostRank>());
		
		return ranks;
	}
	
	public static final List<PostRank> getPostRankList() {
		List<PostRank> ranks = new ArrayList<PostRank>();
		
		LocalDateTime time = LocalDateTime.of(2016, 1, 1, 12, 0);
		
		PostRank rnk1 = new PostRank(time.plusMinutes(10), 
				new PostScore(time.plusMinutes(10), time, 1L, 1L, "user-1", 3L, 1L, time.plusMinutes(5)));
		
		PostRank rnk2 = new PostRank(time.plusMinutes(20), 
				new PostScore(time.plusMinutes(10), time, 1L, 1L, "user-1", 3L, 1L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 2L, 1L, "user-1", 2L, 1L, time.plusMinutes(5)));
		
		PostRank rnk3 = new PostRank(time.plusMinutes(30), 
				new PostScore(time.plusMinutes(10), time, 1L, 1L, "user-1", 3L, 1L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 2L, 1L, "user-1", 2L, 1L, time.plusMinutes(5)));
		
		PostRank rnk4 = new PostRank(time.plusMinutes(40), 
				new PostScore(time.plusMinutes(10), time, 1L, 1L, "user-1", 3L, 1L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 2L, 1L, "user-1", 2L, 1L, time.plusMinutes(5)));
		
		PostRank rnk5 = new PostRank(time.plusMinutes(50), 
				new PostScore(time.plusMinutes(10), time, 1L, 1L, "user-1", 3L, 1L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 2L, 1L, "user-1", 2L, 1L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 3L, 1L, "user-1", 1L, 1L, time.plusMinutes(5)));
		
		PostRank rnk6 = new PostRank(time.plusMinutes(60), 
				new PostScore(time.plusMinutes(10), time, 1L, 1L, "user-1", 3L, 1L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 2L, 1L, "user-1", 2L, 1L, time.plusMinutes(5)), 
				new PostScore(time.plusMinutes(10), time, 3L, 1L, "user-1", 1L, 1L, time.plusMinutes(5)));
	
		ranks.add(rnk1);
		ranks.add(rnk2);
		ranks.add(rnk3);
		ranks.add(rnk4);
		ranks.add(rnk5);
		ranks.add(rnk6);
		
		return ranks;
	}
	
}
