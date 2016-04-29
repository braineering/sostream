package com.threecore.project.model.score.post;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

import com.threecore.project.model.Comment;
import com.threecore.project.model.ModelCommons;
import com.threecore.project.model.Post;
import com.threecore.project.model.PostScore;
import com.threecore.project.model.event.CommentMap;
import com.threecore.project.model.event.StandardCommentMap;
import com.threecore.project.model.score.post.base.AbstractPostScoreRepo;
import com.threecore.project.tool.JodaTimeTool;

public class PostScoreRepoMapStandard extends AbstractPostScoreRepo {

	private static final long serialVersionUID = 1L;
	
	protected CommentMap comment2Post;

	public PostScoreRepoMapStandard() {
		super();		
		this.comment2Post = new StandardCommentMap();
	}	

	@Override
	public PostScore addPost(final Post post) {
		long postId = post.getPostId();
		long postTimestamp = post.getTimestamp();
		
		this.comment2Post.addPost(post);
		super.updatesQueue.put(postId, new LinkedBlockingQueue<Tuple2<Long, Long>>());
		super.updatesQueue.get(postId).add(new Tuple2<Long, Long>(postTimestamp, ModelCommons.INITIAL_SCORE));
		super.commenters.put(postId, new HashSet<Long>());		
		
		PostScore scoreUpdate = new PostScore(postTimestamp, 
				postTimestamp, 
				postId, 
				post.getUserId(), 
				post.getUser(),
				ModelCommons.INITIAL_SCORE, 
				0, 
				ModelCommons.UNDEFINED_LONG);
		
		super.scores.put(postId, scoreUpdate);
		
		return scoreUpdate;
	}
	
	@Override
	public PostScore addComment(final Comment comment) { // replies are NOT mapped on their own post_commented_id
		long postCommentedId = this.comment2Post.addComment(comment);	
		
		if (postCommentedId == -1) { // there is no active post with the specified post_commented_id
			return null;		
		}
		
		comment.setPostCommentedId(postCommentedId); // useless (?)
		
		PostScore scoreUpdate = super.scores.get(postCommentedId);
		
		if (scoreUpdate == null) { // there is no active post with the specified post_commented_id // useless (?)
			return null;
		}
		
		long commentTimestamp = comment.getTimestamp();
		
		boolean isNewCommenter = super.commenters.get(postCommentedId).add(comment.getUserId());
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
