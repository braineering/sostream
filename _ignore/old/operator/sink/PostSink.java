package com.threecore.project.operator.sink;

import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.functions.sink.OutputFormatSinkFunction;

import com.threecore.project.model.Post;
import com.threecore.project.model.format.specific.PostWriteFormat;

public class PostSink extends OutputFormatSinkFunction<Post> {

	private static final long serialVersionUID = 6008497887713380306L;
	
	public PostSink(final String path) {
		super(new PostWriteFormat(new Path(path)));
	}

}
