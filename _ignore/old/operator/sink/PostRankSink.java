package com.threecore.project.operator.sink;

import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.functions.sink.OutputFormatSinkFunction;

import com.threecore.project.model.PostRank;
import com.threecore.project.model.format.specific.PostRankWriteFormat;

public class PostRankSink extends OutputFormatSinkFunction<PostRank> {
	
	private static final long serialVersionUID = -5897836600889430224L;

	public PostRankSink(final String path) {
		super(new PostRankWriteFormat(new Path(path)));
	}

}
