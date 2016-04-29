package com.threecore.project.query;

import org.apache.flink.api.common.JobExecutionResult;
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
import com.threecore.project.operator.filter.PostRankUpdateFilter;
import com.threecore.project.operator.key.PostScoreKeyer;
import com.threecore.project.operator.rank.post.PostRankMergerSort;
import com.threecore.project.operator.rank.post.PostRankerSort;
import com.threecore.project.operator.score.post.PostScoreAggregator;
import com.threecore.project.operator.score.post.PostScoreMapperUpdater2;

public class QueryOne06 {

	private static final String JOB_NAME = "q1-6";
	
	public static void main(String[] args) throws Exception {	

		final AppConfiguration config = AppConfigurator.loadConfiguration(args);
		
		final StreamExecutionEnvironment env = EnvConfigurator.setupExecutionEnvironment(config);

		DataStream<EventQueryOne> events = EventPostCommentStreamgen.getStreamOfEvents4(env, config);
		
		DataStream<PostScore> updates = events.flatMap(new PostScoreMapperUpdater2()).setParallelism(1);
		
		DataStream<PostScore> scores = updates.keyBy(new PostScoreKeyer()).reduce(new PostScoreAggregator()).setParallelism(config.getParallelism());
		
		DataStream<PostRank> bests = scores.keyBy(new PostScoreKeyer()).flatMap(new PostRankerSort()).setParallelism(config.getParallelism());
		
		DataStream<PostRank> tops = null;
		
		if (config.getParallelism() == 1) {
			tops = bests;
		} else {
			tops = bests.flatMap(new PostRankMergerSort()).setParallelism(1);
		}

		DataStream<PostRank> newTops = tops.filter(new PostRankUpdateFilter()).setParallelism(1);

		newTops.addSink(new AsStringSink<PostRank>(config.getSinkPath(JOB_NAME, "out"), config.getSinkBufferSize()));	

		JobExecutionResult res = env.execute(JOB_NAME);
		
		PerformanceWriter.write(res, config.getPerformancePath(JOB_NAME));
	}

}
