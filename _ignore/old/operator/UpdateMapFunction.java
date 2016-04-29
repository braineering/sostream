package com.threecore.project.operator;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;

import com.threecore.project.model.Comment;
import com.threecore.project.model.Post;

public class UpdateMapFunction implements MapFunction<Tuple3<Post, Comment, Boolean>, Tuple4<Long, Long, Long, Long>> {

	private static final long serialVersionUID = 1L;

	@Override
	public Tuple4<Long, Long, Long, Long> map(Tuple3<Post, Comment, Boolean> tuple) throws Exception {
		if(tuple.f2){
			return new Tuple4<>(
					tuple.f0.getTimestamp(),
					tuple.f0.getPostId(),
					tuple.f0.getUserId(),
					10L);
		}
		else{
			return new Tuple4<>(
					tuple.f1.getTimestamp(),
					tuple.f1.getPostCommentedId(),
					tuple.f1.getUserId(),
					10L);
		}
	}

}
