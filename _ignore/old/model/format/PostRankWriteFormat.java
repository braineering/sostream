package com.threecore.project.model.format.specific;

import java.io.IOException;

import org.apache.flink.api.java.io.TextOutputFormat;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.core.fs.Path;

import com.threecore.project.model.PostRank;

public class PostRankWriteFormat extends TextOutputFormat<PostRank> {

	private static final long serialVersionUID = 3209016563777463158L;

	public PostRankWriteFormat(Path path) {
		super(path);
		super.setWriteMode(FileSystem.WriteMode.OVERWRITE);
	}
	
	@Override
	public void writeRecord(PostRank rank) throws IOException {
		byte[] bytes = rank.asString().getBytes(super.getCharsetName());
		super.stream.write(bytes);
		super.stream.write('\n');
	}

}
