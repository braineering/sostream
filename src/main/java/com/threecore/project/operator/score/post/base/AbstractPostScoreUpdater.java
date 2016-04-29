package com.threecore.project.operator.score.post.base;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.threecore.project.model.Comment;
import com.threecore.project.model.EventPostComment;
import com.threecore.project.model.Post;
import com.threecore.project.model.PostScore;
import com.threecore.project.model.score.post.PostScoreRepo;
import com.threecore.project.tool.JodaTimeTool;

public abstract class AbstractPostScoreUpdater implements FlatMapFunction<EventPostComment, PostScore> {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPostScoreUpdater.class.getSimpleName());
	
	protected PostScoreRepo scores;
	
	protected long ts; // timestamp flowing with events
	
	public AbstractPostScoreUpdater() {
		this.ts = Long.MIN_VALUE;
	}

	@Override
	public void flatMap(EventPostComment event, Collector<PostScore> out) throws Exception {
		LOGGER.debug("EVENT IN: " + event.asString());
		if (event.isPost()) {
			if (event.getTimestamp() > this.ts) {
				LOGGER.info("UPDATE (by ev) [from TS=" + JodaTimeTool.getStringFromMillis(this.ts) + " to TS=" + JodaTimeTool.getStringFromMillis(event.getTimestamp()) + "]");
				this.scores.update(event.getTimestamp(), out);
				this.ts = event.getTimestamp();
			} else {
				LOGGER.info("ALREADY UP-TO-DATE (by ev) [TS=" + JodaTimeTool.getStringFromMillis(this.ts) + "]");	
			}			
			Post post = event.getPost();
			PostScore score = this.scores.addPost(post);			
			LOGGER.info("NEW POST ADDED: " + post.getPostId());			
			out.collect(score);			
		} else if (event.isComment()) {
			if (event.getTimestamp() > this.ts) {
				LOGGER.info("UPDATE (by ev) [from TS=" + JodaTimeTool.getStringFromMillis(this.ts) + " to TS=" + JodaTimeTool.getStringFromMillis(event.getTimestamp()) + "]");
				this.scores.update(event.getTimestamp(), out);
				this.ts = event.getTimestamp();
			} else {
				LOGGER.info("ALREADY UP-TO-DATE (by ev) [TS=" + JodaTimeTool.getStringFromMillis(this.ts) + "]");
			}			
			Comment comment = event.getComment();
			PostScore score = this.scores.addComment(comment);			
			if (score == null) {
				LOGGER.info("COMMENT IGNORED [" + comment.getCommentId() + "]");
				return;	
			}
			LOGGER.info("NEW COMMENT ADDED [" + comment.getCommentId() + "->" + score.getPostId() + "]");
			out.collect(score);
		} else if (event.isEOF()) {
			LOGGER.info("EOF");
			this.scores.executeEOF(out);		
			return;
		}			
	}

}
