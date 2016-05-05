package com.threecore.project.query;

import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SplitStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import com.threecore.project.control.AppConfigurator;
import com.threecore.project.control.EnvConfigurator;
import com.threecore.project.control.EventCommentFriendshipLikeStreamgen;
import com.threecore.project.control.PerformanceWriter;
import com.threecore.project.operator.event.FriendshipOperator;
import com.threecore.project.model.CommentRank;
import com.threecore.project.model.CommentScore;
import com.threecore.project.model.EventCommentFriendshipLike;
import com.threecore.project.model.EventCommentLike;
import com.threecore.project.model.Friendship;
import com.threecore.project.model.conf.AppConfiguration;
import com.threecore.project.operator.event.EventCommentLikeExtractor;
import com.threecore.project.operator.event.FriendshipSplitter;
import com.threecore.project.operator.filter.CommentRankUpdateFilter;
import com.threecore.project.operator.key.CommentScoreKeyer;
import com.threecore.project.operator.key.EventCommentLikeKeyer;
import com.threecore.project.operator.rank.comment.CommentRankMergerSort;
import com.threecore.project.operator.rank.comment.CommentRankerSort;
import com.threecore.project.operator.score.comment.CommentScoreUpdater;
import com.threecore.project.operator.sink.AsStringSink;

public class QueryTwo {	

	private static final String JOB_NAME = "q2";	
	
	public static void main(String[] args) throws Exception {		
		
		final AppConfiguration config = AppConfigurator.loadConfiguration(args);
		
		final StreamExecutionEnvironment env = EnvConfigurator.setupExecutionEnvironment(config);
		
		EnvConfigurator.initializeRedis(config.getRedisHostname(), config.getRedisPort());
		
		DataStream<EventCommentFriendshipLike> events = EventCommentFriendshipLikeStreamgen.getStreamOfEvents(env, config);
		
		SplitStream<EventCommentFriendshipLike> splitted = events.split(new FriendshipSplitter());
		
		DataStream<Friendship> friendships = splitted.select(FriendshipSplitter.FRIENDSHIP_ONLY).map(new FriendshipOperator()).setParallelism(1).broadcast();
		
		DataStream<EventCommentLike> eventsStream = splitted.select(FriendshipSplitter.EVENT_ALL).map(new EventCommentLikeExtractor()).keyBy(new EventCommentLikeKeyer());
				
		ConnectedStreams<EventCommentLike, Friendship> process = eventsStream.connect(friendships);
		
		DataStream<CommentScore> scores = process.flatMap(new CommentScoreUpdater(config.getD())).setParallelism(config.getParallelism());
		
		DataStream<CommentRank> bests = scores.keyBy(new CommentScoreKeyer()).flatMap(new CommentRankerSort(config.getK())).setParallelism(config.getParallelism());
		
		DataStream<CommentRank> tops = null;
		
		if (config.getParallelism() == 1) {
			tops = bests;
		} else {
			tops = bests.flatMap(new CommentRankMergerSort(config.getK())).setParallelism(1);
		}

		DataStream<CommentRank> newtops = tops.filter(new CommentRankUpdateFilter(config.getK())).setParallelism(1);

		newtops.addSink(new AsStringSink<CommentRank>(config.getSinkPath(JOB_NAME)));
		
		JobExecutionResult res = env.execute(JOB_NAME);
		
		PerformanceWriter.write(res, config.getPerformancePath(JOB_NAME));
	}	

}
