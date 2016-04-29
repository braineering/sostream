package com.threecore.project.source;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.flink.streaming.api.functions.source.EventTimeSourceFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.watermark.Watermark;

import com.threecore.project.model.Comment;
import com.threecore.project.tool.map.CommentMap;
import com.threecore.project.tool.map.SimpleCommentMapper;

public class CommentSource implements EventTimeSourceFunction<Comment> {

	private static final long serialVersionUID = -4502596774220952181L;

	public static final long DELAY_MILLIS = 1000;
	
	private String dataPath;
	private long delayMillis;
	
	private transient BufferedReader reader;
	
	private static transient volatile CommentMap map = new SimpleCommentMapper();
	
	public CommentSource(final String dataPath) {
		this(dataPath, DELAY_MILLIS);
	}
	
	public CommentSource(final String dataPath, final long delayMillis) {
		assert (dataPath != null) : "dataPath must be not null.";
		assert (delayMillis > 0) : "delayMillis must be greater than 0.";
		
		this.dataPath = dataPath;
		this.delayMillis = delayMillis;
	}

	@Override
	public void run(SourceFunction.SourceContext<Comment> ctx) throws Exception {
		FileInputStream file = new FileInputStream(this.dataPath);		
		InputStreamReader in = new InputStreamReader(file);
		
		this.reader = new BufferedReader(in);
		
		String line;
		
		while (this.reader.ready() && (line = this.reader.readLine()) != null)
			this.emitElementFromLine(ctx, line);
		
		this.reader.close();			
	}

	@Override
	public void cancel() {
		if (this.reader != null) {
			try {
				this.reader.close();
			} catch (IOException exc) {
				exc.printStackTrace();
			} finally {
				this.reader = null;
			}
		}		
	}
	
	private void emitElementFromLine(SourceFunction.SourceContext<Comment> ctx, final String line) {
		Comment element = Comment.fromString(line);
		//long postCommentedId = map.addComment(comment);
		//comment.setPostCommentedId(postCommentedId);
		long timestamp = element.getTimestampMillis();
		long nextWatermark = timestamp + this.delayMillis;
		ctx.collectWithTimestamp(element, timestamp);		
		ctx.emitWatermark(new Watermark(nextWatermark));
	}	

}
