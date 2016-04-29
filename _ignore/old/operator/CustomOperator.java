package com.threecore.project.operator;

import java.util.HashSet;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.functions.co.CoFlatMapFunction;
import org.apache.flink.util.Collector;

import com.threecore.project.model.Comment;
import com.threecore.project.model.Post;

public class CustomOperator implements CoFlatMapFunction<Tuple3<Post, Comment, Boolean>, Long, Tuple3<Post, Comment, Boolean>> {
	
	private static final long serialVersionUID = 1L;
	private static volatile HashSet<Long> activePosts = new HashSet<Long>();

	public CustomOperator(){
		// TODO Auto-generated method stub		
	}

	@Override
	public void flatMap1(Tuple3<Post, Comment, Boolean> event, Collector<Tuple3<Post, Comment, Boolean>> out) throws Exception {
		if(event.f2){
			activePosts.add(event.f0.getPostId());
			out.collect(event);
		}
		else{
			if(activePosts.contains(event.f1.getPostCommentedId())){
				out.collect(event);
			}
		}
		
	}

	@Override
	public void flatMap2(Long expiredPost, Collector<Tuple3<Post, Comment, Boolean>> out) throws Exception {
		if(activePosts.contains(expiredPost)){
			activePosts.remove(expiredPost);
		}
		
	}	

}
