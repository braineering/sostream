package com.threecore.project.query;

import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import com.threecore.project.control.AppConfigurator;
import com.threecore.project.control.EnvConfigurator;
import com.threecore.project.control.EventCommentFriendshipLikeStreamgen;
import com.threecore.project.control.PerformanceWriter;
import com.threecore.project.model.EventCommentFriendshipLike;
import com.threecore.project.model.conf.AppConfiguration;
import com.threecore.project.operator.sink.AsStringSink;

public class QueryTwoSourceSink {	

	private static final String JOB_NAME = "q2-source-sink";	
	
	public static void main(String[] args) throws Exception {		
		
		final AppConfiguration config = AppConfigurator.loadConfiguration(args);
		
		boolean debug = config.getDebug();
		
		final StreamExecutionEnvironment env = EnvConfigurator.setupExecutionEnvironment(config);
		
		EnvConfigurator.initializeRedis(config.getRedisHostname(), config.getRedisPort());
		
		DataStream<EventCommentFriendshipLike> events = EventCommentFriendshipLikeStreamgen.getEvents(env, config);
		
		if (debug) events.addSink(new AsStringSink<EventCommentFriendshipLike>(config.getSinkPath(JOB_NAME, "events")));

		events.addSink(new AsStringSink<EventCommentFriendshipLike>(config.getSinkPath(JOB_NAME, "out")));
		
		if (debug) System.out.println(env.getExecutionPlan());
		
		JobExecutionResult res = env.execute(JOB_NAME);
		
		PerformanceWriter.write(res, config.getPerformancePath(JOB_NAME));
	}	

}
