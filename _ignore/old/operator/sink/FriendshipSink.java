package com.threecore.project.operator.sink;

import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.functions.sink.OutputFormatSinkFunction;

import com.threecore.project.model.Friendship;
import com.threecore.project.model.format.specific.FriendshipWriteFormat;

public class FriendshipSink extends OutputFormatSinkFunction<Friendship> {

	private static final long serialVersionUID = -7437532244425017125L;

	public FriendshipSink(final String path) {
		super(new FriendshipWriteFormat(new Path(path)));
	}

}
