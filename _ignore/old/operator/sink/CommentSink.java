package com.threecore.project.operator.sink;

import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.functions.sink.OutputFormatSinkFunction;

import com.threecore.project.model.Comment;
import com.threecore.project.model.format.specific.CommentWriteFormat;

public class CommentSink extends OutputFormatSinkFunction<Comment> {
	
	private static final long serialVersionUID = -7944700379126179745L;

	public CommentSink(final String path) {
		super(new CommentWriteFormat(new Path(path)));
	}

}
