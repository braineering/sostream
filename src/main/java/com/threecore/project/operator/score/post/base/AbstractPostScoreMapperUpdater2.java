package com.threecore.project.operator.score.post.base;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.threecore.project.model.EventQueryOne;
import com.threecore.project.model.PostScore;
import com.threecore.project.model.score.post.PostScoreMapRepo2;
import com.threecore.project.tool.JodaTimeTool;

public abstract class AbstractPostScoreMapperUpdater2 implements FlatMapFunction<EventQueryOne, PostScore> {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPostScoreMapperUpdater2.class.getSimpleName());
	
	protected PostScoreMapRepo2 scores;
	
	protected long ts; // timestamp flowing with events
	
	public AbstractPostScoreMapperUpdater2() {
		this.ts = Long.MIN_VALUE;
	}

	@Override
	public void flatMap(EventQueryOne event, Collector<PostScore> out) throws Exception {
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
			if (commentTimestamp > this.ts) {
				LOGGER.debug("UPDATE (by ev) [from TS=" + JodaTimeTool.getStringFromMillis(this.ts) + " to TS=" + JodaTimeTool.getStringFromMillis(commentTimestamp) + "]");
				this.scores.update(commentTimestamp, out);
				this.ts = commentTimestamp;
			} else {
				LOGGER.debug("ALREADY UP-TO-DATE (by ev) [TS=" + JodaTimeTool.getStringFromMillis(this.ts) + "]");
			}			
			PostScore score = null;
			if (event.isReply()) {
				long commentRepliedId = event.getCommentRepliedId();
				score = this.scores.addCommentToComment(commentTimestamp, commentId, commentUserId, commentRepliedId);
			} else {
				long postCommentedId = event.getPostCommentedId();
				score = this.scores.addCommentToPost(commentTimestamp, commentId, commentUserId, postCommentedId);
			}		
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

}
