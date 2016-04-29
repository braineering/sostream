package com.threecore.project.query.deprecated;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple6;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import com.threecore.project.control.AppControl;
import com.threecore.project.dataset.LongRankData;
import com.threecore.project.operator.deprecated.RankBestThreeLong;
import com.threecore.project.operator.word.WordTokenizer;

@Deprecated
public class QueryLongRank 
{	
	
	private static final String JOB_NAME = "longrank";	
	private static ParameterTool config;
	
	private static final Long DEFAULT_INPUT[] = LongRankData.SIMPLE;
	
	public static void main(String[] args) throws Exception 
	{
		setupProgram(args);
		
		final StreamExecutionEnvironment env = setupExecutionEnvironment(config);
		
		DataStream<Long> numbers = env.fromElements(DEFAULT_INPUT);
		
		DataStream<Tuple2<Long, Long>> counts = numbers.flatMap(new WordTokenizer()).setParallelism(1).keyBy(0).sum(1);
		
		DataStream <Tuple6<String,Long,String,Long,String,Long>> classifica = counts.flatMap(new RankBestThreeLong());
		
		if (config.get("output") != null)
			classifica.writeAsText(config.get("output") + "-" + JOB_NAME, FileSystem.WriteMode.OVERWRITE);
		else
			classifica.print();
		
		env.execute(JOB_NAME);
		
	}
	
	public static final void setupProgram(final String[] args) {
		config = AppControl.parseArguments(args);
		
		assert (config != null) : "final config must be != null.";
		assert (AppControl.checkParameters(config)) : "parsed config must be correct.";
		
		System.out.println("CONFIGURATION LOADED: " + config.toMap().toString());
	}
	
	public static final StreamExecutionEnvironment setupExecutionEnvironment(ParameterTool config) {
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();	
		env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
		env.getConfig().enableTimestamps();
		env.getConfig().setAutoWatermarkInterval(1000);
		env.setParallelism(1);
		env.getConfig().setGlobalJobParameters(config);
		
		return env;
	}

}