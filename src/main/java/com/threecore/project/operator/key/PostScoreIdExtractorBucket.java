package com.threecore.project.operator.key;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

import com.threecore.project.model.PostScore;
import com.threecore.project.model.event.BucketId;

public class PostScoreIdExtractorBucket implements FlatMapFunction<PostScore, BucketId> {

	private static final long serialVersionUID = 1L;
	
	public static final int BUFFSIZE = 1000;
	
	private BucketId buffer;
	private int buffsize;
	
	public PostScoreIdExtractorBucket(final int buffsize) {
		this.buffer = new BucketId();
		this.buffsize = buffsize;
	}
	
	public PostScoreIdExtractorBucket() {
		this.buffer = new BucketId();
		this.buffsize = BUFFSIZE;
	}

	@Override
	public void flatMap(PostScore score, Collector<BucketId> out) throws Exception {
		this.buffer.add(score.getPostId());
		if (this.buffer.size() >= this.buffsize) {
			out.collect(this.buffer);
			this.buffer.clear();
		}		
	}

}
