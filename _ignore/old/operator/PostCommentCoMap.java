package com.threecore.project.operator;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;

import com.threecore.project.model.Comment;
import com.threecore.project.model.Post;

public class PostCommentCoMap implements CoMapFunction<Post, Comment, Tuple3<Post, Comment, Boolean>> {

	private static final long serialVersionUID = -5493959960528887085L;

	@Override
	public Tuple3<Post, Comment, Boolean> map1(Post post) throws Exception {
		return new Tuple3<>(post, null, true);
	}

	@Override
	public Tuple3<Post, Comment, Boolean> map2(Comment comment) throws Exception {
		return new Tuple3<>(null, comment, false);
	}

}
