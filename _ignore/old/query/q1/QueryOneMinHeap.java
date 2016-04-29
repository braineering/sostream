package com.threecore.project.query;

import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SplitStream;
import org.apache.flink.streaming.api.datastream.IterativeStream.ConnectedIterativeStreams;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import com.threecore.project.control.AppConfigurator;
import com.threecore.project.control.EnvConfigurator;
import com.threecore.project.control.EventPostCommentStreamgen;
import com.threecore.project.control.PerformanceWriter;
import com.threecore.project.model.EventPostComment;
import com.threecore.project.model.PostRank;
import com.threecore.project.model.PostScore;
import com.threecore.project.model.conf.AppConfiguration;
import com.threecore.project.operator.sink.AsStringSink;
import com.threecore.project.operator.time.EventPostCommentTimestamper;
import com.threecore.project.operator.event.ActiveEventPostCommentMapper;
import com.threecore.project.operator.key.EventPostCommentKeyer;
import com.threecore.project.operator.key.PostScoreIdExtractor;
import com.threecore.project.operator.key.PostScoreKeyer;
import com.threecore.project.operator.rank.post.PostRankMergerMinHeap;
import com.threecore.project.operator.rank.post.PostRankUpdateFilter;
import com.threecore.project.operator.rank.post.PostRankerMinHeap;
import com.threecore.project.operator.score.post.PostScoreAggregator;
import com.threecore.project.operator.score.post.PostScoreSplitter;
import com.threecore.project.operator.score.post.PostScoreUpdater;

public class QueryOneMinHeap {	

	private static final String JOB_NAME = "q1-min-heap";
	
	public static void main(String[] args) throws Exception {		
		
		final AppConfiguration config = AppConfigurator.loadConfiguration(args);
		
		boolean debug = config.getDebug();
		
		final StreamExecutionEnvironment env = EnvConfigurator.setupExecutionEnvironment(config);

		DataStream<EventPostComment> events = EventPostCommentStreamgen.getStreamOfEvents2(env, config);
		
		if (debug) events.addSink(new AsStringSink<EventPostComment>(config.getSinkPath(JOB_NAME, "events")));
		
		ConnectedIterativeStreams<EventPostComment, Long> eventsIter = events.iterate(config.getIterationTimeout()).withFeedbackType(Long.class);
		
		DataStream<EventPostComment> mapped = eventsIter.flatMap(new ActiveEventPostCommentMapper()).setParallelism(1);
		
		if (debug) mapped.addSink(new AsStringSink<EventPostComment>(config.getSinkPath(JOB_NAME, "mapped-events")));
		
		DataStream<Long> timeUpdate = mapped.map(new EventPostCommentTimestamper()).setParallelism(1).broadcast();
		
		DataStream<EventPostComment> main = mapped.keyBy(new EventPostCommentKeyer());	
		
		ConnectedStreams<EventPostComment, Long> syncMain = main.connect(timeUpdate);
		
		DataStream<PostScore> updates = syncMain.flatMap(new PostScoreUpdater()).setParallelism(config.getParallelism());
		
		if (debug) updates.addSink(new AsStringSink<PostScore>(config.getSinkPath(JOB_NAME, "updates")));
		
		DataStream<PostScore> scores = updates.keyBy(new PostScoreKeyer()).reduce(new PostScoreAggregator()).setParallelism(config.getParallelism());
		
		if (debug) scores.addSink(new AsStringSink<PostScore>(config.getSinkPath(JOB_NAME, "scores")));
		
		SplitStream<PostScore> splitScores = scores.split(new PostScoreSplitter());
		
		DataStream<Long> inactivePosts = splitScores.select(PostScoreSplitter.SCORE_INACTIVE).map(new PostScoreIdExtractor());
		
		eventsIter.closeWith(inactivePosts);
		
		DataStream<PostScore> scoresMain = splitScores.select(PostScoreSplitter.SCORE_ALL);	
		
		DataStream<PostRank> bests = scoresMain.keyBy(new PostScoreKeyer()).flatMap(new PostRankerMinHeap()).setParallelism(config.getParallelism());
		
		if (debug) bests.addSink(new AsStringSink<PostRank>(config.getSinkPath(JOB_NAME, "bests")));	
		
		DataStream<PostRank> tops = null;
		
		if (config.getParallelism() == 1) {
			tops = bests;
		} else {
			tops = bests.flatMap(new PostRankMergerMinHeap()).setParallelism(1);
		}
		
		if (debug) tops.addSink(new AsStringSink<PostRank>(config.getSinkPath(JOB_NAME, "tops")));	

		DataStream<PostRank> newtops = tops.filter(new PostRankUpdateFilter()).setParallelism(1);

		newtops.addSink(new AsStringSink<PostRank>(config.getSinkPath(JOB_NAME, "out")));		

		JobExecutionResult res = env.execute(JOB_NAME);
		
		PerformanceWriter.write(res, config.getPerformancePath(JOB_NAME));
	}

}
