package com.threecore.project.operator.source.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.watermark.Watermark;

public abstract class BaseCollectionSource<T> implements SourceFunction<T> {

	private static final long serialVersionUID = 1L;

	public static final long DELAY = 10;
	
	private long delay;
	private List<T> collection;	
	
	public BaseCollectionSource(final Collection<T> collection) {
		this(collection, DELAY);
		
	}
	
	public BaseCollectionSource(final Collection<T> collection, final long delay) {
		assert (collection != null) : "collection must be != null.";
		assert (delay >= 1) : "delay must be >= 1.";
		
		this.collection = new ArrayList<T>();
		this.collection.addAll(collection);
		this.delay = delay;
	}

	@Override
	public void run(SourceFunction.SourceContext<T> ctx) throws Exception {		
		for (T element : this.collection)
			this.emitElement(ctx, element);			
	}

	@Override
	public void cancel() {
		// TODO Auto-generated constructor stub
	}
	
	public abstract void doSomethingWithElementBeforeEmitting(T element);
	
	public abstract long getElementTimestampMillis(final T object);
	
	private void emitElement(SourceFunction.SourceContext<T> ctx, T element) {
		this.doSomethingWithElementBeforeEmitting(element);
		long timestamp = this.getElementTimestampMillis(element);
		long nextWatermark = timestamp + this.delay;
		ctx.collectWithTimestamp(element, timestamp);	
		ctx.emitWatermark(new Watermark(nextWatermark));
	}	

}
