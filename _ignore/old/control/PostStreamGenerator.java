package com.threecore.project.control.stream;

import java.util.List;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import com.lambdaworks.redis.api.async.RedisAsyncCommands;
import com.threecore.project.model.Post;
import com.threecore.project.model.conf.AppConfiguration;
import com.threecore.project.operator.source.test.PostSource;
import com.threecore.project.operator.time.AscendingTimestamper;
import com.threecore.project.tool.data.PostData;

public class PostStreamGenerator {

	public static DataStream<Post> getPosts(StreamExecutionEnvironment env, AppConfiguration config) {
		String postSource = config.getPosts();
		
		DataStream<Post> posts = null;
		
		if (postSource == null) {
			List<Post> list = PostData.getDefault();
			posts = env.fromCollection(list);		
		} else {
			posts = env.addSource(new PostSource(postSource), "posts-source");
		}	
		
		posts.assignTimestampsAndWatermarks(new AscendingTimestamper<Post>());
		
		return posts;
	}
	
	public static DataStream<Post> getPostsRedis(StreamExecutionEnvironment env, AppConfiguration config, RedisAsyncCommands<String, String> redisCmd) {
		String postSource = config.getPosts();
		
		DataStream<Post> posts = null;
		
		if (postSource == null) {
			List<Post> list = PostData.getDefault();
			posts = env.fromCollection(list);			
		} else {
			posts = env.addSource(new PostSource(postSource), "posts-source");
		}	
		
		posts.assignTimestampsAndWatermarks(new AscendingTimestamper<Post>());
		
		return posts;
	}
}
