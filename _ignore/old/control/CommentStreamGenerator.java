package com.threecore.project.control.stream;

import java.util.List;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import com.threecore.project.model.Comment;
import com.threecore.project.model.conf.AppConfiguration;
import com.threecore.project.operator.source.test.CommentSource;
import com.threecore.project.operator.time.AscendingTimestamper;
import com.threecore.project.tool.data.CommentData;

public class CommentStreamGenerator {
	
	public static DataStream<Comment> getComments(StreamExecutionEnvironment env, AppConfiguration config) {
		String source = config.getComments();
		
		DataStream<Comment> comments = null;
		
		if (source == null) {
			List<Comment> list = CommentData.getDefault();
			comments = env.fromCollection(list);		
		} else {
			comments = env.addSource(new CommentSource(source), "comments-source");
		}				
		
		comments.assignTimestampsAndWatermarks(new AscendingTimestamper<Comment>());
		
		return comments;
	}
}
