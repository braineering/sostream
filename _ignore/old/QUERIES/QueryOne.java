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
import com.threecore.project.model.EventQueryOne;
import com.threecore.project.model.PostRank;
import com.threecore.project.model.PostScore;
import com.threecore.project.model.conf.AppConfiguration;
import com.threecore.project.operator.sink.AsStringSink;
import com.threecore.project.operator.time.EventPostCommentTimestamper2;
import com.threecore.project.operator.event.ActiveEventPostCommentMapper2;
import com.threecore.project.operator.filter.PostRankUpdateFilter;
import com.threecore.project.operator.key.EventPostCommentKeyer2;
import com.threecore.project.operator.key.PostScoreIdExtractor;
import com.threecore.project.operator.key.PostScoreKeyer;
import com.threecore.project.operator.rank.post.PostRankMergerSort;
import com.threecore.project.operator.rank.post.PostRankerSort;
import com.threecore.project.operator.score.post.PostScoreAggregator;
import com.threecore.project.operator.score.post.PostScoreSplitter;
import com.threecore.project.operator.score.post.PostScoreUpdater2;

public class QueryOne {

	private static final String JOB_NAME = "q1";
	
	public static void main(String[] args) throws Exception {		
		
		final AppConfiguration config = AppConfigurator.loadConfiguration(args);
		
		final StreamExecutionEnvironment env = EnvConfigurator.setupExecutionEnvironment(config);

		DataStream<EventQueryOne> events = EventPostCommentStreamgen.getStreamOfEvents4(env, config);
		
		ConnectedIterativeStreams<EventQueryOne, Long> eventsIter = events.iterate(config.getIterationTimeout()).withFeedbackType(Long.class);
		
		DataStream<EventQueryOne> mapped = eventsIter.flatMap(new ActiveEventPostCommentMapper2()).setParallelism(1);
		
		DataStream<Long> timeUpdate = mapped.map(new EventPostCommentTimestamper2()).setParallelism(1).broadcast();
		
		DataStream<EventQueryOne> main = mapped.keyBy(new EventPostCommentKeyer2());	
		
		ConnectedStreams<EventQueryOne, Long> syncMain = main.connect(timeUpdate);
		
		DataStream<PostScore> updates = syncMain.flatMap(new PostScoreUpdater2()).setParallelism(config.getParallelism());
		
		DataStream<PostScore> scores = updates.keyBy(new PostScoreKeyer()).reduce(new PostScoreAggregator()).setParallelism(config.getParallelism());
		
		SplitStream<PostScore> splitScores = scores.split(new PostScoreSplitter());
		
		DataStream<Long> inactivePosts = splitScores.select(PostScoreSplitter.SCORE_INACTIVE).map(new PostScoreIdExtractor());
		
		eventsIter.closeWith(inactivePosts);
		
		DataStream<PostScore> scoresMain = splitScores.select(PostScoreSplitter.SCORE_ALL);	
		
		DataStream<PostRank> bests = scoresMain.keyBy(new PostScoreKeyer()).flatMap(new PostRankerSort()).setParallelism(config.getParallelism());	
		
		DataStream<PostRank> tops = null;
		
		if (config.getParallelism() == 1) {
			tops = bests;
		} else {
			tops = bests.flatMap(new PostRankMergerSort()).setParallelism(1);
		}			

		DataStream<PostRank> newtops = tops.filter(new PostRankUpdateFilter()).setParallelism(1);		

		newtops.addSink(new AsStringSink<PostRank>(config.getSinkPath(JOB_NAME, "out"), config.getSinkBufferSize()));		

		JobExecutionResult res = env.execute(JOB_NAME);
		
		PerformanceWriter.write(res, config.getPerformancePath(JOB_NAME));
	}

}
