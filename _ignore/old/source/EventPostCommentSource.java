package com.threecore.project.operator.source;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.watermark.Watermark;

import com.threecore.project.model.Comment;
import com.threecore.project.model.EventPostComment;
import com.threecore.project.model.Post;
import com.threecore.project.tool.TimeFormat;

public class EventPostCommentSource implements SourceFunction<EventPostComment> {

	private static final long serialVersionUID = -2713394032361507233L;
	
	public static final long WATERMARK_DELAY = 1000;
	
	private String postPath;
	private String commentPath;
	private long watermarkDelay;
	private long nextWatermark;
	
	private transient BufferedReader postReader;
	private transient BufferedReader commentReader;
	
	public EventPostCommentSource(final String postPath, final String commentPath, final long watermarkDelay) {
		assert (postPath != null) : "postPath must be != null.";
		assert (commentPath != null) : "commentPath must be != null.";
		assert (watermarkDelay > 0) : "watermarkDelay must be > 0.";
		
		this.postPath = postPath;
		this.commentPath = commentPath;
		this.watermarkDelay = watermarkDelay;
		this.nextWatermark = 0;
	}
	
	public EventPostCommentSource(final String postPath, final String commentPath) {
		this(postPath, commentPath, WATERMARK_DELAY);
	}

	@Override
	public void run(SourceContext<EventPostComment> ctx) throws Exception {
		FileInputStream postFile = new FileInputStream(this.postPath);
		FileInputStream commentFile = new FileInputStream(this.commentPath);
		
		InputStreamReader postInput = new InputStreamReader(postFile);
		InputStreamReader commentInput = new InputStreamReader(commentFile);
		
		this.postReader = new BufferedReader(postInput);
		this.commentReader = new BufferedReader(commentInput);
		
		String postLine;
		String commentLine;
		
		Post post = null;
		Comment comment = null;
		EventPostComment event = null;
		
		while (true) {
			if (this.postReader.ready()) {
				if (post == null) {
					postLine = this.postReader.readLine();
					if (postLine != null) {
						post = Post.fromString(postLine);
					}
				}
			}
			
			if (this.commentReader.ready()) {
				if (comment == null) {
					commentLine = this.commentReader.readLine();
					if (commentLine != null) {
						comment = Comment.fromString(commentLine);
					}
				}
			}
			
			if (post != null && comment == null) {
				event = new EventPostComment(post);
				post = null;
			} else if (comment != null && post == null) {
				event = new EventPostComment(comment);
				comment = null;
			} else if (post != null && comment != null) {
				if (TimeFormat.isBefore(post.getTimestamp(), comment.getTimestamp())) {
					event = new EventPostComment(post);
					post = null;
				} else {
					event = new EventPostComment(comment);
					comment = null;
				}
			} else {
				break;
			}
			
			this.emitEvent(ctx, event);	
		}
		
		this.postReader.close();
		
		this.commentReader.close();	
	}

	@Override
	public void cancel() {
		if (this.postReader != null) {
			try {
				this.postReader.close();
			} catch (IOException exc) {
				exc.printStackTrace();
			} finally {
				this.postReader = null;
			}
		}	
		
		if (this.commentReader != null) {
			try {
				this.commentReader.close();
			} catch (IOException exc) {
				exc.printStackTrace();
			} finally {
				this.commentReader = null;
			}
		}
	}
	
	private void emitEvent(SourceContext<EventPostComment> ctx, final EventPostComment event) {
		this.nextWatermark = event.getTimestamp() + this.watermarkDelay;		
		ctx.collectWithTimestamp(event, event.getTimestamp());
		ctx.emitWatermark(new Watermark(this.nextWatermark));
	}

}
