package com.threecore.project.source.old;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.flink.streaming.api.functions.source.EventTimeSourceFunction;
import org.apache.flink.streaming.api.watermark.Watermark;

import com.threecore.project.model.Comment;

public class CommentEventTimeSource implements EventTimeSourceFunction<Comment>{
	
	private static final long serialVersionUID = -6268307110721612051L;
	private static final String commentPath = "data/comments100.dat";
	
	private transient BufferedReader reader;
	//private static transient volatile HashMap<Long, Long> commentToPost = new HashMap<Long, Long>();
	private static transient volatile Map<Long, Long> commentToPost = new HashMap<Long, Long>();
	
	private Long watermarkDelay;
	
	public CommentEventTimeSource(Long delay){
		this.watermarkDelay = delay;
	}

	@Override
	public void cancel() {}

	@Override
	public void run(SourceContext<Comment> ctx) throws Exception {
		
		this.reader = new BufferedReader(new FileReader(commentPath));
		
		String line;
		Long nextWatermark;
		
		Comment comment;
		while (this.reader.ready() && (line = this.reader.readLine()) != null) {
			comment = Comment.fromString(line);			
			
			if(comment.getPostCommentedId() == -1) {
				comment.setPostCommentedId(
						commentToPost.get(comment.getCommentRepliedId()));
			}
			else{
				commentToPost.put(
						comment.getCommentId(), 
						comment.getPostCommentedId());
			}
			
			ctx.collectWithTimestamp(comment, comment.getTimestampMillis());
			
			nextWatermark = comment.getTimestampMillis() + this.watermarkDelay;
			
			ctx.emitWatermark(new Watermark(nextWatermark));
		}
	}

}
