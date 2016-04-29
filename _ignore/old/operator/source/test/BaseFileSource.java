package com.threecore.project.operator.source.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

public abstract class BaseFileSource<T> implements SourceFunction<T> {
	
	private static final long serialVersionUID = 1L;

	public static final long DELAY_MILLIS = 1000;
	
	private String path;
	
	private transient BufferedReader reader;
	
	public BaseFileSource(final String path) {
		assert (path != null) : "path must be != null.";
		
		this.path = path;
	}

	@Override
	public void run(SourceFunction.SourceContext<T> ctx) throws Exception {
		FileInputStream file = new FileInputStream(this.path);		
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
	
	public abstract T parseElement(String line);
	
	public abstract void doSomethingWithElementBeforeEmitting(T element);
	
	public abstract long getElementTimestampMillis(T object);
	
	private void emitElementFromLine(SourceFunction.SourceContext<T> ctx, String line) {
		T element = this.parseElement(line);
		this.doSomethingWithElementBeforeEmitting(element);
		long timestamp = this.getElementTimestampMillis(element);
		//long nextWatermark = timestamp + this.delayMillis;
		ctx.collectWithTimestamp(element, timestamp);		
		//ctx.emitWatermark(new Watermark(nextWatermark));
	}	

}
