package com.threecore.project.source.old;

import java.io.BufferedReader;
import java.io.FileReader;

import org.apache.flink.streaming.api.functions.source.EventTimeSourceFunction;
import org.apache.flink.streaming.api.watermark.Watermark;

import com.threecore.project.model.Post;

public class PostEventTimeSource implements EventTimeSourceFunction<Post> {

	private static final long serialVersionUID = -6268307110721612052L;
	private static final String postPath = "data/posts100.dat";
	
	private transient BufferedReader reader;
	
	private Long watermarkDelay;
	
	public PostEventTimeSource(Long delay){
		this.watermarkDelay = delay;
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void run(SourceContext<Post> ctx) throws Exception {
		
		this.reader = new BufferedReader(new FileReader(postPath));
		
		String line;
		Long nextWatermark;
		
		Post post;
		while (this.reader.ready() && (line = this.reader.readLine()) != null) {
			post = Post.fromString(line);
			
			ctx.collectWithTimestamp(post, post.getTimestampMillis());
			
			nextWatermark = post.getTimestampMillis() + this.watermarkDelay;
			
			ctx.emitWatermark(new Watermark(nextWatermark));
		}

	}

}
