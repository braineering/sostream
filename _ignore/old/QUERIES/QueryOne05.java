package com.threecore.project.query;

import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStream;
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
import com.threecore.project.operator.event.EventPostCommentMapper2;
import com.threecore.project.operator.filter.PostRankUpdateFilter;
import com.threecore.project.operator.key.EventPostCommentKeyer2;
import com.threecore.project.operator.key.PostScoreKeyer;
import com.threecore.project.operator.rank.post.PostRankMergerStreamSelection;
import com.threecore.project.operator.rank.post.PostRankerStreamSelection;
import com.threecore.project.operator.score.post.PostScoreUpdaterTotal2;

public class QueryOne05 {

	private static final String JOB_NAME = "q1-5";
	
	public static void main(String[] args) throws Exception {	

		final AppConfiguration config = AppConfigurator.loadConfiguration(args);
		
		final StreamExecutionEnvironment env = EnvConfigurator.setupExecutionEnvironment(config);

		DataStream<EventQueryOne> events = EventPostCommentStreamgen.getStreamOfEvents4(env, config);
		
		DataStream<EventQueryOne> mapped = events.flatMap(new EventPostCommentMapper2()).setParallelism(1);
		
		DataStream<Long> timeUpdate = mapped.map(new EventPostCommentTimestamper2()).setParallelism(1).broadcast();
		
		DataStream<EventQueryOne> main = mapped.keyBy(new EventPostCommentKeyer2());	
		
		ConnectedStreams<EventQueryOne, Long> syncMain = main.connect(timeUpdate);
		
		DataStream<PostScore> scores = syncMain.flatMap(new PostScoreUpdaterTotal2()).setParallelism(config.getParallelism());
		
		DataStream<PostRank> bests = scores.keyBy(new PostScoreKeyer()).flatMap(new PostRankerStreamSelection()).setParallelism(config.getParallelism());
		
		DataStream<PostRank> tops = null;
		
		if (config.getParallelism() == 1) {
			tops = bests;
		} else {
			tops = bests.flatMap(new PostRankMergerStreamSelection()).setParallelism(1);
		}

		DataStream<PostRank> newtops = tops.filter(new PostRankUpdateFilter()).setParallelism(1);

		newtops.addSink(new AsStringSink<PostRank>(config.getSinkPath(JOB_NAME, "out"), config.getSinkBufferSize()));		

		JobExecutionResult res = env.execute(JOB_NAME);
		
		PerformanceWriter.write(res, config.getPerformancePath(JOB_NAME));
	}

}
