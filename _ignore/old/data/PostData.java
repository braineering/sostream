package com.threecore.project.tool.data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.threecore.project.model.Post;

public final class PostData {
	
	private static final LocalDateTime TIME = LocalDateTime.of(2016, 1, 1, 12, 0, 0);
	private static final long INTERVAL = 5;
	private static final long NUMPOSTS = 10;
	
	public static final List<Post> getDefault() {
		return get(TIME, INTERVAL, NUMPOSTS);
	}
	
	public static final List<Post> get(final LocalDateTime start, final long minutes, final long numposts) {
		List<Post> posts = new ArrayList<Post>();
		
		for (long post_id = 0; post_id < numposts; post_id++) {
			Post post = new Post(start.plusMinutes(post_id * minutes), Long.valueOf(post_id), Long.valueOf(post_id * 10), "sample-post", "username");
			posts.add(post);
		}
		
		return posts;
	}

}
