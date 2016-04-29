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
import com.threecore.project.operator.key.CommentScoreKeyer;
import com.threecore.project.operator.key.EventCommentLikeKeyer;
import com.threecore.project.operator.rank.comment.CommentRankMergerMinHeap;
import com.threecore.project.operator.rank.comment.CommentRankUpdateFilter;
import com.threecore.project.operator.rank.comment.CommentRankerMinHeap;
import com.threecore.project.operator.score.comment.CommentScoreUpdater;
import com.threecore.project.operator.sink.AsStringSinkDefault;
import com.threecore.project.operator.sink.ToStringSinkDefault;

public class QueryTwoMinHeap {	

	private static final String JOB_NAME = "q2-min-heap";	
	
	public static void main(String[] args) throws Exception {		
		
		final AppConfiguration config = AppConfigurator.loadConfiguration(args);
		
		boolean debug = config.getDebug();
		
		final StreamExecutionEnvironment env = EnvConfigurator.setupExecutionEnvironment(config);
		
		EnvConfigurator.initializeRedis(config.getRedisHostname(), config.getRedisPort());
		
		DataStream<EventCommentFriendshipLike> events = EventCommentFriendshipLikeStreamgen.getEvents(env, config);
		
		if (debug) events.addSink(new ToStringSinkDefault<EventCommentFriendshipLike>(config.getSinkPath(JOB_NAME, "events-raw")));
		
		if (debug) events.addSink(new AsStringSinkDefault<EventCommentFriendshipLike>(config.getSinkPath(JOB_NAME, "events")));
		
		SplitStream<EventCommentFriendshipLike> splitted = events.split(new FriendshipSplitter());
		
		DataStream<Friendship> friendships = splitted.select(FriendshipSplitter.FRIENDSHIP_ONLY).map(new FriendshipOperator()).setParallelism(1).broadcast();
		
		DataStream<EventCommentLike> eventsStream = splitted.select(FriendshipSplitter.EVENT_ALL).map(new EventCommentLikeExtractor()).keyBy(new EventCommentLikeKeyer());
				
		ConnectedStreams<EventCommentLike, Friendship> process = eventsStream.connect(friendships);
		
		DataStream<CommentScore> scores = process.flatMap(new CommentScoreUpdater(config.getD())).setParallelism(config.getParallelism());
		
		if (debug) scores.addSink(new ToStringSinkDefault<CommentScore>(config.getSinkPath(JOB_NAME, "scores-raw")));
		
		if (debug) scores.addSink(new AsStringSinkDefault<CommentScore>(config.getSinkPath(JOB_NAME, "scores")));
		
		DataStream<CommentRank> bests = scores.keyBy(new CommentScoreKeyer()).flatMap(new CommentRankerMinHeap(config.getK())).setParallelism(config.getParallelism());
		
		if (debug) bests.addSink(new ToStringSinkDefault<CommentRank>(config.getSinkPath(JOB_NAME, "bests-raw")));
		
		if (debug) bests.addSink(new AsStringSinkDefault<CommentRank>(config.getSinkPath(JOB_NAME, "bests")));	
		
		DataStream<CommentRank> tops = null;
		
		if (config.getParallelism() == 1) {
			tops = bests;
		} else {
			tops = bests.flatMap(new CommentRankMergerMinHeap(config.getK())).setParallelism(1);
		}
		
		if (debug) tops.addSink(new ToStringSinkDefault<CommentRank>(config.getSinkPath(JOB_NAME, "tops-events")));
		
		if (debug) tops.addSink(new AsStringSinkDefault<CommentRank>(config.getSinkPath(JOB_NAME, "tops")));	

		DataStream<CommentRank> newtops = tops.filter(new CommentRankUpdateFilter(config.getK())).setParallelism(1);
		
		if (debug) newtops.addSink(new ToStringSinkDefault<CommentRank>(config.getSinkPath(JOB_NAME, "out-raw")));

		newtops.addSink(new AsStringSinkDefault<CommentRank>(config.getSinkPath(JOB_NAME, "out")));
		
		JobExecutionResult res = env.execute(JOB_NAME);
		
		PerformanceWriter.write(res, config.getPerformancePath(JOB_NAME));
	}	

}
