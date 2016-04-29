package com.threecore.project.operator.source;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.flink.api.common.accumulators.LongCounter;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.joda.time.DateTime;

import com.threecore.project.model.Comment;
import com.threecore.project.model.EventPostComment;
import com.threecore.project.model.ModelCommons;
import com.threecore.project.model.Post;
import com.threecore.project.tool.JodaTimeTool;

public class EventPostCommentSource2 extends RichSourceFunction<EventPostComment> {
	
	private static final long serialVersionUID = 1L;

	public static final String DEFAULT_POSTS = "posts.dat";
	public static final String DEFAULT_COMMENTS = "comments.dat";
	public static final long DEFAULT_WATERMARK_DELAY = 1000;
	
	public static final int DEFAULT_BUFFSIZE = 16384;
	
	private String postPath;
	private String commentPath;
	
	private long watermarkDelay;
	private long nextWatermark;
	
	private transient BufferedReader postReader;
	private transient BufferedReader commentReader;
	
	private long ts[];
	
	private LongCounter tuples = new LongCounter();
	
	public EventPostCommentSource2(final String postPath, final String commentPath, final long watermarkDelay) {		
		this.postPath = postPath;
		this.commentPath = commentPath;
		this.watermarkDelay = watermarkDelay;
		this.nextWatermark = 0;
		
		this.ts = new long[2];
	}
	
	public EventPostCommentSource2(final String postPath, final String commentPath) {
		this(postPath, commentPath, DEFAULT_WATERMARK_DELAY);
	}
	
	public EventPostCommentSource2() {
		this(DEFAULT_POSTS, DEFAULT_COMMENTS, DEFAULT_WATERMARK_DELAY);
	}

	@Override
	public void run(SourceContext<EventPostComment> ctx) throws Exception {
		super.getRuntimeContext().addAccumulator("tuples", this.tuples);
		
		FileInputStream postFile = new FileInputStream(this.postPath);
		FileInputStream commentFile = new FileInputStream(this.commentPath);
		
		InputStreamReader postInput = new InputStreamReader(postFile);
		InputStreamReader commentInput = new InputStreamReader(commentFile);
		
		this.postReader = new BufferedReader(postInput, DEFAULT_BUFFSIZE);
		this.commentReader = new BufferedReader(commentInput, DEFAULT_BUFFSIZE);
		
		String postLine = null;
		String commentLine = null;
		
		Post post = new Post();
		Comment comment = new Comment();
		EventPostComment event = new EventPostComment();
		
		int nextEvent;
		ts[0] = Long.MAX_VALUE;
		ts[1] = Long.MAX_VALUE;
		
		boolean postOutbox = false;
		boolean commentOutbox = false;
		
		while (true) {
			if (this.postReader.ready()) {
				if (!postOutbox) {
					postLine = this.postReader.readLine();
					if (postLine != null) {
						final String array[] = postLine.split("[|]", -1);

						final long timestamp = DateTime.parse(array[0], JodaTimeTool.FORMATTER).getMillis();
						final long post_id = Long.parseLong(array[1]);
						final long user_id = Long.parseLong(array[2]);
						//final String content = array[3];
						final String content = "";
						final String user = array[4];
						
						post.f0 = timestamp;
						post.f1 = post_id;
						post.f2 = user_id;
						post.f3 = content;
						post.f4 = user;
						
						ts[0] = timestamp;
						postOutbox = true;
					} else {
						ts[0] = Long.MAX_VALUE;
					}
				}
			}
			
			if (this.commentReader.ready()) {
				if (!commentOutbox) {
					commentLine = this.commentReader.readLine();
					if (commentLine != null) {
						
						final String array[] = commentLine.split("[|]", -1);
						
						final long timestamp = DateTime.parse(array[0], JodaTimeTool.FORMATTER).getMillis();
						final long comment_id = Long.parseLong(array[1]);
						final long user_id = Long.parseLong(array[2]);
						//final String content = array[3];
						final String content = "";
						//final String user = array[4];
						final String user = "";
						
						final long comment_replied_id = (array[5].isEmpty()) ? ModelCommons.UNDEFINED_LONG : Long.parseLong(array[5]);
						final long post_commented_id = (array[6].isEmpty()) ? ModelCommons.UNDEFINED_LONG : Long.parseLong(array[6]);
						
						comment.f0 = timestamp;
						comment.f1 = comment_id;
						comment.f2 = user_id;
						comment.f3 = content;
						comment.f4 = user;
						comment.f5 = comment_replied_id;
						comment.f6 = post_commented_id;
						
						ts[1] = comment.f0;
						commentOutbox = true;
					} else {
						ts[1] = Long.MAX_VALUE;
					}
				}
			}
			
			if (!postOutbox && !commentOutbox) {
				break;
			}
			
			if (ts[0] <= ts[1]) {
				nextEvent = 0;
			} else {
				nextEvent = 1;
			}
			
			if (nextEvent == 0) {
				event.f0 = post;
				event.f1 = Comment.UNDEFINED_COMMENT;
				event.f2 = EventPostComment.TYPE_POST;
				postOutbox = false;
				ts[0] = Long.MAX_VALUE;
			} else if (nextEvent == 1) {
				event.f0 = Post.UNDEFINED_POST;
				event.f1 = comment;
				event.f2 = EventPostComment.TYPE_COMMENT;
				commentOutbox = false;
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
