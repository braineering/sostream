package com.threecore.project.model.score.post;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;
import com.threecore.project.model.ModelCommons;
import com.threecore.project.model.PostScore;
import com.threecore.project.tool.JodaTimeTool;

public class PostScoreRepoStandardTotal2 extends PostScoreRepoStandard2 {

	private static final long serialVersionUID = 1L;

	public PostScoreRepoStandardTotal2() {
		super();
	}
	
	@Override
	public PostScore addComment(final long commentTimestamp, final long commentUserId, final long postCommentedId) { // replies are mapped on their own post_commented_id		
		PostScore scoreUpdate = super.scores.get(postCommentedId);
		
		if (scoreUpdate == null) { // there is no active post with the specified post_commented_id
			return null;
		}
		
		boolean isNewCommenter = super.commenters.get(postCommentedId).add(commentUserId);
		super.updatesQueue.get(postCommentedId).add(new Tuple2<Long, Long>(commentTimestamp, ModelCommons.INITIAL_SCORE));
		
		scoreUpdate.f0 = commentTimestamp;
		scoreUpdate.f5 += ModelCommons.INITIAL_SCORE;
		scoreUpdate.f6 += ((isNewCommenter) ? 1 : 0);
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
					scoreEntry.f1 -=1;
					scoresQueue.add(scoreEntry);
				}
			}
			
			if (expirations > 0) {
				PostScore score = super.scores.get(postId);				
				score.f0 = updateTimestamp;
				score.f5 -= expirations;				
				out.collect(score);
				
				if (scoresQueue.size() == 0) {
					expiredPosts.add(postId);
				}
			} 
		}
		
		for (long expiredPost : expiredPosts) {			
			super.updatesQueue.remove(expiredPost);
			super.commenters.remove(expiredPost);
			super.scores.remove(expiredPost);
		}
		
		super.ts = timestamp;
	}
	
}
