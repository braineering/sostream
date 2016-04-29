package com.threecore.project.model.score.post.base;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;
import com.threecore.project.model.PostScore;
import com.threecore.project.model.score.post.PostScoreMapRepo2;
import com.threecore.project.tool.JodaTimeTool;

public abstract class AbstractPostScoreMapRepo2 implements PostScoreMapRepo2 {

	private static final long serialVersionUID = 1L;
	
	protected long ts; // up-to-date to this timestamp
	
	//protected Map<Long, Post> posts; // post_id -> Post
	protected Map<Long, Queue<Tuple2<Long, Long>>> updatesQueue; //post_id -> [(ts0,score_component), (ts1,score_component),...,(tsn,score_component)]
	protected Map<Long, Set<Long>> commenters; // post_id -> {commenters_id})
	protected Map<Long, PostScore> scores; // post_id -> PostScore
	
	public AbstractPostScoreMapRepo2() {
		this.ts = Long.MIN_VALUE;
		this.updatesQueue = new HashMap<Long, Queue<Tuple2<Long, Long>>>();
		this.commenters = new HashMap<Long, Set<Long>>();
		this.scores = new HashMap<Long, PostScore>();
	}
	
	@Override
	public abstract PostScore addPost(final long postTimestamp, final long postId, final long postUserId, final String postUser);
	
	@Override
	public abstract PostScore addCommentToPost(final long commentTimestamp, final long commentId, final long commentUserId, final long postCommentedId);
	
	@Override
	public abstract PostScore addCommentToComment(final long commentTimestamp, final long commentId, final long commentUserId, final long commentRepliedId);
	
	@Override
	public abstract void update(long timestamp, Collector<PostScore> out);
	
	@Override
	public long getTimestamp() {
		return this.ts;
	}
	
	@Override
	public boolean isActivePost(final long postId) {
		return this.scores.containsKey(postId);
	}
	
	protected long computeExpirationTime(final long postId) {
		Queue<Tuple2<Long, Long>> scoresQueue = this.updatesQueue.get(postId);
		long expirationTime = 0;
		
		for (Tuple2<Long, Long> entry : scoresQueue) {
			long ts = entry.f0 + (entry.f1 * JodaTimeTool.DAY_MILLIS);
			if (ts > expirationTime) {
				expirationTime = ts;
			}
		}
		
		return expirationTime;
	}
	
	@Override
	public void executeEOF(Collector<PostScore> out) {
		SortedSet<Long> expirations = new ConcurrentSkipListSet<Long>();
		
		for (long pid : this.updatesQueue.keySet()) {
			long expts = this.computeExpirationTime(pid);
			expirations.add(expts);
		}
		
		for (long expts : expirations) {
			this.update(expts, out);
		}
		
		PostScore eof = PostScore.EOF;
				
		out.collect(eof);
	}

}
