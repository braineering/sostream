package com.threecore.project.query;

import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import com.threecore.project.control.AppConfigurator;
import com.threecore.project.control.EnvConfigurator;
import com.threecore.project.control.EventPostCommentStreamgen;
import com.threecore.project.control.PerformanceWriter;
import com.threecore.project.model.EventQueryOne;
import com.threecore.project.model.conf.AppConfiguration;
import com.threecore.project.operator.sink.AsStringSink;

public class QueryOneSourceSink {

	private static final String JOB_NAME = "q1-source-sink-1";
	
	public static void main(String[] args) throws Exception {		
		
		final AppConfiguration config = AppConfigurator.loadConfiguration(args);
		
		final StreamExecutionEnvironment env = EnvConfigurator.setupExecutionEnvironment(config);

		DataStream<EventQueryOne> events = EventPostCommentStreamgen.getStreamOfEvents4(env, config);

		events.addSink(new AsStringSink<EventQueryOne>(config.getSinkPath(JOB_NAME, "out")));

		JobExecutionResult res = env.execute(JOB_NAME);
		
		PerformanceWriter.write(res, config.getPerformancePath(JOB_NAME));
	}

}
