package com.threecore.project.model.score.post;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

import com.threecore.project.model.ModelCommons;
import com.threecore.project.model.PostScore;
import com.threecore.project.model.event.CommentMap;
import com.threecore.project.model.event.StandardCommentMap;
import com.threecore.project.model.score.post.base.AbstractPostScoreMapRepo2;
import com.threecore.project.tool.JodaTimeTool;

public class PostScoreRepoMapStandard2 extends AbstractPostScoreMapRepo2 {

	private static final long serialVersionUID = 1L;
	
	protected CommentMap comment2Post;

	public PostScoreRepoMapStandard2() {
		super();		
		this.comment2Post = new StandardCommentMap();
	}	

	@Override
	public PostScore addPost(final long postTimestamp, final long postId, final long postUserId, final String postUser) {	
		this.comment2Post.addPost(postId);
		super.updatesQueue.put(postId, new LinkedBlockingQueue<Tuple2<Long, Long>>());
		super.updatesQueue.get(postId).add(new Tuple2<Long, Long>(postTimestamp, ModelCommons.INITIAL_SCORE));
		super.commenters.put(postId, new HashSet<Long>());		
		
		PostScore scoreUpdate = new PostScore(postTimestamp, 
				postTimestamp, 
				postId, 
				postUserId, 
				postUser,
				ModelCommons.INITIAL_SCORE, 
				0, 
				ModelCommons.UNDEFINED_LONG);
		
		super.scores.put(postId, scoreUpdate);
		
		return scoreUpdate;
	}	
	
	@Override
	public PostScore addCommentToPost(final long commentTimestamp, final long commentId, final long commentUserId, final long postCommentedId) {
		PostScore scoreUpdate = super.scores.get(postCommentedId);
		
		if (scoreUpdate == null) { // there is no active post with the specified post_commented_id
			return null;
		}
		
		boolean isNewCommenter = super.commenters.get(postCommentedId).add(commentUserId);
		super.updatesQueue.get(postCommentedId).add(new Tuple2<Long, Long>(commentTimestamp, ModelCommons.INITIAL_SCORE));
		
		scoreUpdate.f0 = commentTimestamp;
		scoreUpdate.f5 = ModelCommons.INITIAL_SCORE;
		scoreUpdate.f6 = ((isNewCommenter) ? 1L : 0);
		scoreUpdate.f7 = commentTimestamp;
		
		return scoreUpdate;
	}	
	
	@Override
	public PostScore addCommentToComment(final long commentTimestamp, final long commentId, final long commentUserId, final long commentRepliedId) { // replies are NOT mapped on their own post_commented_id
		long postCommentedId = this.comment2Post.addCommentToComment(commentId, commentRepliedId);
		
		if (postCommentedId == -1) { // there is no active post with the specified post_commented_id
			return null;
		}	
		
		PostScore scoreUpdate = super.scores.get(postCommentedId);
		
		if (scoreUpdate == null) { // there is no active post with the specified post_commented_id
			return null;
		}
		
		boolean isNewCommenter = super.commenters.get(postCommentedId).add(commentUserId);
		super.updatesQueue.get(postCommentedId).add(new Tuple2<Long, Long>(commentTimestamp, ModelCommons.INITIAL_SCORE));
		
		scoreUpdate.f0 = commentTimestamp;
		scoreUpdate.f5 = ModelCommons.INITIAL_SCORE;
		scoreUpdate.f6 = ((isNewCommenter) ? 1L : 0);
		scoreUpdate.f7 = commentTimestamp;
		
		return scoreUpdate;
	}

	@Override
	public void update(long timestamp, Collector<PostScore> out) {
		List<Long> expiredPosts = new ArrayList<Long>();
		
		Iterator<Long> postIter = super.updatesQueue.keySet().iterator();		
		while (postIter.hasNext()) {			
			long postId = postIter.next();
			Queue<Tuple2<Long, Long>> scoresQueue = super.updatesQueue.get(postId);
			
			long expirations = 0;
			long updateTimestamp = 0;
			
			while ((scoresQueue.size() > 0) && (timestamp - scoresQueue.peek().f0 >= JodaTimeTool.DAY_MILLIS)) {				
				Tuple2<Long, Long> scoreEntry = scoresQueue.poll();
				long expirationTimestamp = scoreEntry.f0 + JodaTimeTool.DAY_MILLIS;
				expirations ++;
				updateTimestamp = (expirationTimestamp > updateTimestamp) ? expirationTimestamp : updateTimestamp;
				if (scoreEntry.f1 > 1) {					
					long newTs = scoreEntry.f0 + JodaTimeTool.DAY_MILLIS;	
					scoreEntry.f0 = newTs;
					scoreEntry.f1 -= 1;
					scoresQueue.add(scoreEntry);
				}
			}
			
			if (expirations > 0) {
				PostScore scoreUpdate = this.scores.get(postId);
				scoreUpdate.f0 = updateTimestamp;
				scoreUpdate.f5 = expirations * -1;
				scoreUpdate.f6 = 0L;				
				out.collect(scoreUpdate);
				
				if (scoresQueue.size() == 0) {
					expiredPosts.add(postId);
				}
			} 
		}
		
		for (long expiredPost : expiredPosts) {	
			this.comment2Post.removePost(expiredPost);
			super.updatesQueue.remove(expiredPost);
			super.commenters.remove(expiredPost);
			super.scores.remove(expiredPost);
		}
		
		super.ts = timestamp;
	}
	
}
