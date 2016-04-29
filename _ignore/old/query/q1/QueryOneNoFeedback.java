package com.threecore.project.query;

import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import com.threecore.project.control.AppConfigurator;
import com.threecore.project.control.EnvConfigurator;
import com.threecore.project.control.EventPostCommentStreamgen;
import com.threecore.project.control.PerformanceWriter;
import com.threecore.project.model.EventPostComment;
import com.threecore.project.model.PostRank;
import com.threecore.project.model.PostScore;
import com.threecore.project.model.conf.AppConfiguration;
import com.threecore.project.operator.score.PostScoreUpdater;
import com.threecore.project.operator.sink.AsStringSink;
import com.threecore.project.operator.sink.ToStringSink;
import com.threecore.project.operator.time.EventPostCommentTimestamper;
import com.threecore.project.operator.score.PostScoreAggregator;
import com.threecore.project.operator.event.EventPostCommentMapper;
import com.threecore.project.operator.key.EventPostCommentKeyer;
import com.threecore.project.operator.key.PostScoreKeyer;
import com.threecore.project.operator.rank.post.PostRankMergerSort;
import com.threecore.project.operator.rank.post.PostRankUpdateFilter;
import com.threecore.project.operator.rank.post.PostRankerSort;

public class QueryOneNoFeedback {

	private static final String JOB_NAME = "q1-no-feedback";
	
	public static void main(String[] args) throws Exception {	

		final AppConfiguration config = AppConfigurator.loadConfiguration(args);
		
		boolean debug = config.getDebug();
		
		final StreamExecutionEnvironment env = EnvConfigurator.setupExecutionEnvironment(config);

		DataStream<EventPostComment> events = EventPostCommentStreamgen.getEvents(env, config);
		
		if (debug) events.addSink(new ToStringSink<EventPostComment>(config.getSinkPath(JOB_NAME, "events-raw")));
		
		if (debug) events.addSink(new AsStringSink<EventPostComment>(config.getSinkPath(JOB_NAME, "events")));
		
		DataStream<EventPostComment> mapped = events.flatMap(new EventPostCommentMapper()).setParallelism(1);
		
		if (debug) mapped.addSink(new ToStringSink<EventPostComment>(config.getSinkPath(JOB_NAME, "mapped-events-raw")));
		
		if (debug) mapped.addSink(new AsStringSink<EventPostComment>(config.getSinkPath(JOB_NAME, "mapped-events")));	
		
		DataStream<Long> timeUpdate = mapped.map(new EventPostCommentTimestamper()).setParallelism(1).broadcast();
		
		DataStream<EventPostComment> main = mapped.keyBy(new EventPostCommentKeyer());	
		
		ConnectedStreams<EventPostComment, Long> syncMain = main.connect(timeUpdate);
		
		DataStream<PostScore> updates = syncMain.flatMap(new PostScoreUpdater()).setParallelism(config.getParallelism());
		
		if (debug) updates.addSink(new ToStringSink<PostScore>(config.getSinkPath(JOB_NAME, "updates-raw")));
		
		if (debug) updates.addSink(new AsStringSink<PostScore>(config.getSinkPath(JOB_NAME, "updates")));
		
		DataStream<PostScore> scores = updates.keyBy(new PostScoreKeyer()).reduce(new PostScoreAggregator()).setParallelism(config.getParallelism());
		
		if (debug) scores.addSink(new ToStringSink<PostScore>(config.getSinkPath(JOB_NAME, "scores-raw")));
		
		if (debug) scores.addSink(new AsStringSink<PostScore>(config.getSinkPath(JOB_NAME, "scores")));
		
		DataStream<PostRank> bests = scores.keyBy(new PostScoreKeyer()).flatMap(new PostRankerSort()).setParallelism(config.getParallelism());
		
		if (debug) bests.addSink(new ToStringSink<PostRank>(config.getSinkPath(JOB_NAME, "bests-raw")));
		
		if (debug) bests.addSink(new AsStringSink<PostRank>(config.getSinkPath(JOB_NAME, "bests")));	
		
		DataStream<PostRank> tops = null;
		
		if (config.getParallelism() == 1) {
			tops = bests;
		} else {
			tops = bests.flatMap(new PostRankMergerSort()).setParallelism(1);
		}
		
		if (debug) tops.addSink(new ToStringSink<PostRank>(config.getSinkPath(JOB_NAME, "tops-events")));
		
		if (debug) tops.addSink(new AsStringSink<PostRank>(config.getSinkPath(JOB_NAME, "tops")));	

		DataStream<PostRank> newtops = tops.filter(new PostRankUpdateFilter()).setParallelism(1);
		
		if (debug) newtops.addSink(new ToStringSink<PostRank>(config.getSinkPath(JOB_NAME, "out-raw")));

		newtops.addSink(new AsStringSink<PostRank>(config.getSinkPath(JOB_NAME, "out")));		

		JobExecutionResult res = env.execute(JOB_NAME);
		
		PerformanceWriter.write(res, config.getPerformancePath(JOB_NAME));
	}

}
