package com.threecore.project.source;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.flink.streaming.api.functions.source.EventTimeSourceFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.watermark.Watermark;

import com.threecore.project.model.Friendship;

public class FriendshipSource implements EventTimeSourceFunction<Friendship> {
	
	private static final long serialVersionUID = 2168073550967884888L;

	public static final long DELAY_MILLIS = 1000;
	
	private String dataPath;
	private long delayMillis;
	
	private transient BufferedReader reader;
	
	public FriendshipSource(final String dataPath) {
		this(dataPath, DELAY_MILLIS);
	}
	
	public FriendshipSource(final String dataPath, final long delayMillis) {
		assert (dataPath != null) : "dataPath must be not null.";
		assert (delayMillis > 0) : "delayMillis must be greater than 0.";
		
		this.dataPath = dataPath;
		this.delayMillis = delayMillis;
	}

	@Override
	public void run(SourceFunction.SourceContext<Friendship> ctx) throws Exception {
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
				reader.close();
			} catch (IOException exc) {
				exc.printStackTrace();
			} finally {
				reader = null;
			}
		}		
	}
	
	private void emitElementFromLine(SourceFunction.SourceContext<Friendship> ctx, final String line) {
		Friendship element = this.parseElement(line);
		long timestamp = this.getElementTimestampMillis(element);
		long nextWatermark = timestamp + this.delayMillis;
		ctx.collectWithTimestamp(element, timestamp);		
		ctx.emitWatermark(new Watermark(nextWatermark));
	}	

	private Friendship parseElement(String line) {
		return Friendship.fromString(line);
	}

	private long getElementTimestampMillis(Friendship object) {
		return object.getTimestampMillis();
	}

}
