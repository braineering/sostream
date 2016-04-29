package com.threecore.project.operator.score.post.base;

import org.apache.flink.streaming.api.functions.co.CoFlatMapFunction;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.threecore.project.model.EventQueryOne;
import com.threecore.project.model.PostScore;
import com.threecore.project.model.score.post.PostScoreRepo2;
import com.threecore.project.tool.JodaTimeTool;

public abstract class AbstractPostScoreUpdaterWithTimeUpdate2 implements CoFlatMapFunction<EventQueryOne, Long, PostScore> {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPostScoreUpdaterWithTimeUpdate2.class.getSimpleName());
	
	protected PostScoreRepo2 scores;
	
	protected long ts; // timestamp flowing with events
	
	public AbstractPostScoreUpdaterWithTimeUpdate2() {
		this.ts = Long.MIN_VALUE;
	}

	@Override
	public void flatMap1(EventQueryOne event, Collector<PostScore> out) throws Exception {
		LOGGER.debug("EVENT IN: " + event.asString());
		if (event.isPost()) {
			long postTimestamp = event.getTimestamp();
			long postId = event.getId();			
			if (postTimestamp > this.ts) {
				LOGGER.debug("UPDATE (by ev) [from TS=" + JodaTimeTool.getStringFromMillis(this.ts) + " to TS=" + JodaTimeTool.getStringFromMillis(postTimestamp) + "]");
				this.scores.update(postTimestamp, out);
				this.ts = postTimestamp;
			} else {
				LOGGER.debug("ALREADY UP-TO-DATE (by ev) [TS=" + JodaTimeTool.getStringFromMillis(this.ts) + "]");	
			}			
			PostScore score = this.scores.addPost(postTimestamp, postId, event.getUserId(), event.getUser());			
			LOGGER.debug("NEW POST ADDED: " + postId);			
			out.collect(score);						
		} else if (event.isComment()) {
			long commentTimestamp = event.getTimestamp();
			long commentId = event.getId();
			long commentUserId = event.getUserId();
			long postCommentedId = event.getPostCommentedId();
			if (commentTimestamp > this.ts) {
				LOGGER.debug("UPDATE (by ev) [from TS=" + JodaTimeTool.getStringFromMillis(this.ts) + " to TS=" + JodaTimeTool.getStringFromMillis(commentTimestamp) + "]");
				this.scores.update(commentTimestamp, out);
				this.ts = commentTimestamp;
			} else {
				LOGGER.debug("ALREADY UP-TO-DATE (by ev) [TS=" + JodaTimeTool.getStringFromMillis(this.ts) + "]");
			}			
			PostScore score = this.scores.addComment(commentTimestamp, commentUserId, postCommentedId);			
			if (score == null) {
				LOGGER.debug("COMMENT IGNORED [" + commentId + "]");
				return;	
			}
			LOGGER.debug("NEW COMMENT ADDED [" + commentId + "->" + score.getPostId() + "]");
			out.collect(score);
		} else if (event.isEOF()) {
			LOGGER.debug("EOF");
			this.scores.executeEOF(out);		
			return;
		}		
	}

	@Override
	public void flatMap2(Long ts, Collector<PostScore> out) throws Exception {
		if (ts <= this.ts) {
			LOGGER.debug("ALREADY UP-TO-DATE (by ts) [TS=" + JodaTimeTool.getStringFromMillis(this.ts) + "]");	
			return;
		}
		LOGGER.debug("UPDATE (by ts) [from TS=" + JodaTimeTool.getStringFromMillis(this.ts) + " to TS=" + JodaTimeTool.getStringFromMillis(ts) + "]");
		this.scores.update(ts, out);					
	}
}
