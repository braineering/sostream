package com.threecore.project.operator.score.post.base;

import org.apache.flink.streaming.api.functions.co.CoFlatMapFunction;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.threecore.project.model.Comment;
import com.threecore.project.model.EventPostComment;
import com.threecore.project.model.Post;
import com.threecore.project.model.PostScore;
import com.threecore.project.model.score.post.PostScoreRepo;
import com.threecore.project.tool.JodaTimeTool;

public abstract class AbstractPostScoreUpdaterWithTimeUpdate implements CoFlatMapFunction<EventPostComment, Long, PostScore> {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPostScoreUpdaterWithTimeUpdate.class.getSimpleName());
	
	protected PostScoreRepo scores;
	
	protected long ts; // timestamp flowing with events
	
	public AbstractPostScoreUpdaterWithTimeUpdate() {
		this.ts = Long.MIN_VALUE;
	}

	@Override
	public void flatMap1(EventPostComment event, Collector<PostScore> out) throws Exception {
		LOGGER.debug("EVENT IN: " + event.asString());
		if (event.isPost()) {
			if (event.getTimestamp() > this.ts) {
				LOGGER.debug("UPDATE (by ev) [from TS=" + JodaTimeTool.getStringFromMillis(this.ts) + " to TS=" + JodaTimeTool.getStringFromMillis(event.getTimestamp()) + "]");
				this.scores.update(event.getTimestamp(), out);
				this.ts = event.getTimestamp();
			} else {
				LOGGER.debug("ALREADY UP-TO-DATE (by ev) [TS=" + JodaTimeTool.getStringFromMillis(this.ts) + "]");	
			}			
			Post post = event.getPost();
			PostScore score = this.scores.addPost(post);			
			LOGGER.debug("NEW POST ADDED: " + post.getPostId());			
			out.collect(score);			
		} else if (event.isComment()) {
			if (event.getTimestamp() > this.ts) {
				LOGGER.debug("UPDATE (by ev) [from TS=" + JodaTimeTool.getStringFromMillis(this.ts) + " to TS=" + JodaTimeTool.getStringFromMillis(event.getTimestamp()) + "]");
				this.scores.update(event.getTimestamp(), out);
				this.ts = event.getTimestamp();
			} else {
				LOGGER.debug("ALREADY UP-TO-DATE (by ev) [TS=" + JodaTimeTool.getStringFromMillis(this.ts) + "]");
			}			
			Comment comment = event.getComment();
			PostScore score = this.scores.addComment(comment);			
			if (score == null) {
				LOGGER.debug("COMMENT IGNORED [" + comment.getCommentId() + "]");
				return;	
			}
			LOGGER.debug("NEW COMMENT ADDED [" + comment.getCommentId() + "->" + score.getPostId() + "]");
			out.collect(score);
		} else if (event.isEOF()) {
			LOGGER.debug("EOF");
			this.scores.executeEOF(out);		
			return;
		}		
	}

	@Override
	public void flatMap2(Long ts, Collector<PostScore> out) throws Exception {
		if (ts > this.ts) {
			LOGGER.debug("UPDATE (by ts) [from TS=" + JodaTimeTool.getStringFromMillis(this.ts) + " to TS=" + JodaTimeTool.getStringFromMillis(ts) + "]");
			this.scores.update(ts, out);
			return;
		}
		LOGGER.debug("ALREADY UP-TO-DATE (by ts) [TS=" + JodaTimeTool.getStringFromMillis(this.ts) + "]");				
	}
}
