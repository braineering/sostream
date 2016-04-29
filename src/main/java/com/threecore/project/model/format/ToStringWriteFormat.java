package com.threecore.project.model.format;

import java.io.IOException;

import org.apache.flink.api.java.io.TextOutputFormat;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.core.fs.Path;

public class ToStringWriteFormat<T> extends TextOutputFormat<T> {

	private static final long serialVersionUID = 1L;

	public ToStringWriteFormat(Path path) {
		super(path);
		super.setWriteMode(FileSystem.WriteMode.OVERWRITE);
	}
	
	@Override
	public void writeRecord(T element) throws IOException {
		byte[] bytes = element.toString().getBytes(super.getCharsetName());
		super.stream.write(bytes);
		super.stream.write('\n');
		super.stream.write('\n');
	}
}
