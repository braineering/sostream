package com.threecore.project.operator.sink;

import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.functions.sink.OutputFormatSinkFunction;

import com.threecore.project.model.format.FormattedStringWriteFormat;
import com.threecore.project.model.type.Stringable;

public class FormattedSink<T extends Stringable> extends OutputFormatSinkFunction<T> {
	
	private static final long serialVersionUID = 5500049288099405831L;

	public FormattedSink(final String path) {
		super(new FormattedStringWriteFormat<T>(new Path(path)));
	}

}
