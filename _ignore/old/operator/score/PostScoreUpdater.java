package com.threecore.project.operator.score;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

import com.threecore.project.model.Comment;
import com.threecore.project.model.EventPostComment;
import com.threecore.project.model.Post;
import com.threecore.project.model.PostScore;
import com.threecore.project.tool.TimeFormat;

public class PostScoreUpdater implements FlatMapFunction<EventPostComment, PostScore> {

	private static final long serialVersionUID = 1L;
	
	public volatile Map<Long, Queue<Tuple2<Long, Long>>> updateList;
	
	public volatile Map<Long, Post> posts; // post_id -> Post
	public volatile Map<Long, Set<Long>> commenters; // post_id->(last_comment_ts, {commenters_id})
	public volatile Map<Long, Long> lastCommentTimestamp; // post_id -> last_comment_ts
	
	public PostScoreUpdater() {
		this.updateList = new HashMap<Long, Queue<Tuple2<Long, Long>>>();
		this.posts = new HashMap<Long, Post>();
		this.lastCommentTimestamp = new HashMap<Long, Long>();
		this.commenters = new HashMap<Long, Set<Long>>();
	}

	@Override
	public void flatMap(EventPostComment event, Collector<PostScore> out) throws Exception {
		if (event.isPost()) {
			this.addPost(event.getPost(), out);
		} else if (event.isComment() && this.posts.containsKey(event.getComment().getPostCommentedId())) {
			this.addComment(event.getComment(), out);
		}
		this.updateScores(event.getTimestamp(), out);		
	}
	
	private void addPost(final Post post, Collector<PostScore> out) throws Exception {
		this.posts.put(post.getPostId(), post);
		this.commenters.put(post.getPostId(), new HashSet<Long>());
		this.lastCommentTimestamp.put(post.getPostId(), null);			
		this.updateList.put(post.getPostId(), new LinkedBlockingQueue<Tuple2<Long, Long>>());
		this.updateList.get(post.getPostId()).add(new Tuple2<Long, Long>(post.getTimestamp(), 10L));
		
		PostScore score = new PostScore(post.getTimestamp(), 
										post.getTimestamp(), 
										post.getPostId(), 
										post.getUserId(), 
										post.getUser(),
										10L, 
										0L, 
										null);
		out.collect(score);
	}
	
	private void addComment(final Comment comment, Collector<PostScore> out) throws Exception {
		Post post = this.posts.get(comment.getPostCommentedId());
		
		boolean isNewCommenter = this.commenters.get(post.getPostId()).add(comment.getUserId());
		this.lastCommentTimestamp.put(post.getPostId(), comment.getTimestamp());
		this.updateList.get(post.getPostId()).add(new Tuple2<Long, Long>(comment.getTimestamp(), 10L));
	
		PostScore score = new PostScore(comment.getTimestamp(), 
										post.getTimestamp(), 
										post.getPostId(), 
										post.getUserId(), 
										post.getUser(),
										10L, 
										((isNewCommenter) ? 1L : 0L), 
										comment.getTimestamp());
		out.collect(score);
	}
	
	private void updateScores(final long timestamp, Collector<PostScore> out) throws Exception {
		List<Long> expiredPosts = new ArrayList<Long>();
		
		Iterator<Long> postIter = this.updateList.keySet().iterator();
		
		while (postIter.hasNext()) {			
			Long postId = postIter.next();
			Queue<Tuple2<Long, Long>> scoresQueue = this.updateList.get(postId);
			
			Long expirations = 0L;
			
			while ((scoresQueue.size() > 0) && (TimeFormat.daysPassed(scoresQueue.peek().f0, timestamp) >= 1)) {				
				Tuple2<Long, Long> scoreEntry = scoresQueue.poll();
				expirations ++;
				
				if (scoreEntry.f1 > 1) {					
					long newTs = scoreEntry.f0 + TimeFormat.DAY_MILLIS;						
					Tuple2<Long, Long> newScoreEntry = new Tuple2<Long, Long>(newTs, (scoreEntry.f1 - 1));					
					scoresQueue.add(newScoreEntry);
				}
			}
			
			if (expirations > 0) {
				PostScore score = new PostScore(timestamp, 
						this.posts.get(postId).getTimestamp(), 
						postId, 
						this.posts.get(postId).getUserId(),
						this.posts.get(postId).getUser(),
						expirations * -1,
						0L,
						this.lastCommentTimestamp.get(postId));
				out.collect(score);
				
				if (scoresQueue.size() == 0)
					expiredPosts.add(postId);
			}
			
			expirations = 0L;
		}
		
		for (Long expiredPost : expiredPosts) {
			this.posts.remove(expiredPost);
			this.lastCommentTimestamp.remove(expiredPost);
			this.updateList.remove(expiredPost);
		}			
	}
}
