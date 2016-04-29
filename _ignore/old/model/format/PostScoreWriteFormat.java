package com.threecore.project.model.format.specific;

import java.io.IOException;

import org.apache.flink.api.java.io.TextOutputFormat;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.core.fs.Path;

import com.threecore.project.model.PostScore;

public class PostScoreWriteFormat extends TextOutputFormat<PostScore> {

	private static final long serialVersionUID = 8243194847569710327L;

	public PostScoreWriteFormat(Path path) {
		super(path);
		super.setWriteMode(FileSystem.WriteMode.OVERWRITE);
	}
	
	@Override
	public void writeRecord(PostScore score) throws IOException {
		byte[] bytes = score.asString().getBytes(super.getCharsetName());
		super.stream.write(bytes);
		super.stream.write('\n');
	}

}
