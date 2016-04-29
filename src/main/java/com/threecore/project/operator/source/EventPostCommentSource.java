package com.threecore.project.operator.source;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.flink.api.common.accumulators.LongCounter;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.apache.flink.streaming.api.watermark.Watermark;

import com.threecore.project.model.Comment;
import com.threecore.project.model.EventPostComment;
import com.threecore.project.model.Post;

public class EventPostCommentSource extends RichSourceFunction<EventPostComment> {
	
	private static final long serialVersionUID = 1L;

	public static final String DEFAULT_POSTS = "posts.dat";
	public static final String DEFAULT_COMMENTS = "comments.dat";
	public static final long DEFAULT_WATERMARK_DELAY = 1000;
	
	private String postPath;
	private String commentPath;
	
	private long watermarkDelay;
	private long nextWatermark;
	
	private transient BufferedReader postReader;
	private transient BufferedReader commentReader;
	
	private long ts[];
	
	private LongCounter tuples = new LongCounter();
	
	public EventPostCommentSource(final String postPath, final String commentPath, final long watermarkDelay) {		
		this.postPath = postPath;
		this.commentPath = commentPath;
		this.watermarkDelay = watermarkDelay;
		this.nextWatermark = 0;
		
		this.ts = new long[2];
	}
	
	public EventPostCommentSource(final String postPath, final String commentPath) {
		this(postPath, commentPath, DEFAULT_WATERMARK_DELAY);
	}
	
	public EventPostCommentSource() {
		this(DEFAULT_POSTS, DEFAULT_COMMENTS, DEFAULT_WATERMARK_DELAY);
	}

	@Override
	public void run(SourceContext<EventPostComment> ctx) throws Exception {
		super.getRuntimeContext().addAccumulator("tuples", this.tuples);
		
		FileInputStream postFile = new FileInputStream(this.postPath);
		FileInputStream commentFile = new FileInputStream(this.commentPath);
		
		InputStreamReader postInput = new InputStreamReader(postFile);
		InputStreamReader commentInput = new InputStreamReader(commentFile);
		
		this.postReader = new BufferedReader(postInput);
		this.commentReader = new BufferedReader(commentInput);
		
		String postLine = null;
		String commentLine = null;
		
		Post post = null;
		Comment comment = null;
		EventPostComment event = new EventPostComment();
		
		int nextEvent;
		this.ts[0] = Long.MAX_VALUE;
		this.ts[1] = Long.MAX_VALUE;
		
		while (true) {
			if (this.postReader.ready()) {
				if (post == null) {
					postLine = this.postReader.readLine();
					if (postLine != null) {
						post = Post.fromString(postLine);
						this.ts[0] = post.getTimestamp();
					} else {
						this.ts[0] = Long.MAX_VALUE;
					}
				}
			}
			
			if (this.commentReader.ready()) {
				if (comment == null) {
					commentLine = this.commentReader.readLine();
					if (commentLine != null) {
						comment = Comment.fromString(commentLine);
						this.ts[1] = comment.getTimestamp();
					} else {
						this.ts[1] = Long.MAX_VALUE;
					}
				}
			}
			
			if (post == null && comment == null) {
				break;
			}
			
			nextEvent = this.getNextEvent();
			
			if (nextEvent == 0) {
				event.setType(EventPostComment.TYPE_POST);
				event.setPost(post);
				event.setComment(Comment.UNDEFINED_COMMENT);
				post = null;
				ts[0] = Long.MAX_VALUE;
			} else if (nextEvent == 1) {
				event.setType(EventPostComment.TYPE_COMMENT);
				event.setComment(comment);
				event.setPost(Post.UNDEFINED_POST);
				comment = null;
				ts[1] = Long.MAX_VALUE;
			}
			
			this.emitEvent(ctx, event);	
		}
		
		this.emitEOF(ctx);
		
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
	
	private int getNextEvent() {
		if (this.ts[0] <= this.ts[1]) {
			return 0;
		} else {
			return 1;
		}
	}
	
	private void emitEvent(SourceContext<EventPostComment> ctx, final EventPostComment event) {
		this.nextWatermark = event.getTimestamp() + this.watermarkDelay;		
		ctx.collectWithTimestamp(event, event.getTimestamp());
		ctx.emitWatermark(new Watermark(this.nextWatermark));
		this.tuples.add(1L);
	}
	
	private void emitEOF(SourceContext<EventPostComment> ctx) {		
		EventPostComment event = EventPostComment.EOF;
		ctx.collect(event);
		this.tuples.add(1L);
	}

}
