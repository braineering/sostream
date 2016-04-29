package com.threecore.project.model.format.specific;

import java.io.IOException;

import org.apache.flink.api.java.io.TextOutputFormat;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.core.fs.Path;

import com.threecore.project.model.Like;

public class LikeWriteFormat extends TextOutputFormat<Like> {

	private static final long serialVersionUID = -6406366031940005520L;

	public LikeWriteFormat(Path path) {
		super(path);
		super.setWriteMode(FileSystem.WriteMode.OVERWRITE);
	}
	
	@Override
	public void writeRecord(Like like) throws IOException {
		byte[] bytes = like.asString().getBytes(super.getCharsetName());
		super.stream.write(bytes);
		super.stream.write('\n');
	}

}
