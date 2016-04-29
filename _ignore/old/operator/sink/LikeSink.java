package com.threecore.project.operator.sink;

import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.functions.sink.OutputFormatSinkFunction;

import com.threecore.project.model.Like;
import com.threecore.project.model.format.specific.LikeWriteFormat;

public class LikeSink extends OutputFormatSinkFunction<Like> {

	private static final long serialVersionUID = -7437532244425017125L;

	public LikeSink(final String path) {
		super(new LikeWriteFormat(new Path(path)));
	}

}
