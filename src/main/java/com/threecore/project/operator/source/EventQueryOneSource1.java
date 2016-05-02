package com.threecore.project.operator.source;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.flink.api.common.accumulators.LongCounter;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.joda.time.DateTime;

import com.threecore.project.model.EventQueryOne;
import com.threecore.project.model.ModelCommons;
import com.threecore.project.tool.JodaTimeTool;

public class EventQueryOneSource1 extends RichSourceFunction<EventQueryOne> {
	
	private static final long serialVersionUID = 1L;

	public static final String DEFAULT_POSTS = "posts.dat";
	public static final String DEFAULT_COMMENTS = "comments.dat";
	public static final long DEFAULT_WATERMARK_DELAY = 1000;
	
	public static final int DEFAULT_BUFFSIZE = 24576;
	
	private String postPath;
	private String commentPath;
	
	private long watermarkDelay;
	private long nextWatermark;
	
	private transient BufferedReader postReader;
	private transient BufferedReader commentReader;
	
	private long ts[];
	
	private LongCounter tuples = new LongCounter();
	
	public EventQueryOneSource1(final String postPath, final String commentPath, final long watermarkDelay) {		
		this.postPath = postPath;
		this.commentPath = commentPath;
		this.watermarkDelay = watermarkDelay;
		this.nextWatermark = 0;
		
		this.ts = new long[2];
	}
	
	public EventQueryOneSource1(final String postPath, final String commentPath) {
		this(postPath, commentPath, DEFAULT_WATERMARK_DELAY);
	}
	
	public EventQueryOneSource1() {
		this(DEFAULT_POSTS, DEFAULT_COMMENTS, DEFAULT_WATERMARK_DELAY);
	}

	@Override
	public void run(SourceContext<EventQueryOne> ctx) throws Exception {
		super.getRuntimeContext().addAccumulator("tuples", this.tuples);
		
		FileInputStream postFile = new FileInputStream(this.postPath);
		FileInputStream commentFile = new FileInputStream(this.commentPath);
		
		InputStreamReader postInput = new InputStreamReader(postFile);
		InputStreamReader commentInput = new InputStreamReader(commentFile);
		
		this.postReader = new BufferedReader(postInput, DEFAULT_BUFFSIZE);
		this.commentReader = new BufferedReader(commentInput, DEFAULT_BUFFSIZE);
		
		String postLine = null;
		String commentLine = null;
		
		EventQueryOne eventPost = new EventQueryOne(EventQueryOne.TYPE_POST);
		EventQueryOne eventComment = new EventQueryOne(EventQueryOne.TYPE_COMMENT);
		
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
						final String user = array[4];
						
						eventPost.f1 = timestamp;
						eventPost.f2 = post_id;
						eventPost.f3 = user_id;
						eventPost.f4 = user;
						
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
						final long comment_replied_id = (array[5].isEmpty()) ? ModelCommons.UNDEFINED_LONG : Long.parseLong(array[5]);
						final long post_commented_id = (array[6].isEmpty()) ? ModelCommons.UNDEFINED_LONG : Long.parseLong(array[6]);
						
						eventComment.f1 = timestamp;
						eventComment.f2 = comment_id;
						eventComment.f3 = user_id;
						eventComment.f5 = comment_replied_id;
						eventComment.f6 = post_commented_id;
						
						ts[1] = timestamp;
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
				this.emitEvent(ctx, eventPost);	
				postOutbox = false;
				ts[0] = Long.MAX_VALUE;
			} else if (nextEvent == 1) {
				this.emitEvent(ctx, eventComment);	
				commentOutbox = false;
				ts[1] = Long.MAX_VALUE;
			}		
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
	
	private long tuple = 0;
	
	private void emitEvent(SourceContext<EventQueryOne> ctx, final EventQueryOne event) {
		//this.nextWatermark = event.getTimestamp() + this.watermarkDelay;		
		ctx.collectWithTimestamp(event, event.getTimestamp());
		//ctx.emitWatermark(new Watermark(this.nextWatermark));
		//ctx.collect(event);
		this.tuples.add(1L);
		this.tuple++;
	}
	
	private void emitEOF(SourceContext<EventQueryOne> ctx) {		
		this.tuples.add(1L);
		EventQueryOne event = EventQueryOne.EOF;
		ctx.collect(event);
		this.tuple++;
	}
	/*
	private long start = 0;
	
	@Override
	public void open(Configuration conf) throws IOException {
		this.start = System.currentTimeMillis();
		
	}
	
	@Override
	public void close() throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("tuples.txt", false));
		writer.write(String.valueOf(start) + "\n" + String.valueOf(this.tuple));
		writer.flush();
		writer.close();
	}
	*/

}
