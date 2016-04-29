package com.threecore.project.draft.marco;

import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.core.fs.FileSystem.WriteMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AscendingTimestampExtractor;

public class Main {
	
	public static final Integer PARALLELISM = 1;
	public static final Integer SINGLE = 1;

	public static void main(String[] args) {
		
		ParameterTool parameters = ParameterTool.fromArgs(args);
		int k = parameters.getInt("k");
		int d = parameters.getInt("d");
		
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
		env.setParallelism(SINGLE);
		
		DataStream<Tuple4<Comment, Friendship, Like, Integer>> input = env
				.addSource(new EventSource())
				.assignTimestampsAndWatermarks(new AscendingTimestampExtractor<Tuple4<Comment,Friendship,Like,Integer>>() {

					private static final long serialVersionUID = 6567129775392631104L;

					@Override
					public long extractAscendingTimestamp(Tuple4<Comment, Friendship, Like, Integer> element) {
						switch(element.f3){
						
							case 0: {
								return element.f0.f0;
							}
							case 1: {
								return element.f1.f0; 
							}
							case 2: {
								return element.f2.f0;
							}
							default: {
								return -1L;
							}	
						
						}
					}
				});
				
		DataStream<Tuple4<Long, Long, String, Long>> controller = input
				.keyBy(new KeySelector<Tuple4<Comment, Friendship, Like, Integer>, Long>() {
					
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public Long getKey(Tuple4<Comment, Friendship, Like, Integer> tuple) throws Exception {
						switch(tuple.f3){
							case 0:{
								return tuple.f0.getCommentId();
							}
							case 1:{
								return 0L;
							}
							case 2:{
								return tuple.f2.getCommentId();
							}
							default:{
								return -1L;
							}
						}
					}
					
				})
				.flatMap(new ProcessOperator(d));
		
		controller.print();

		try {
			env.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
