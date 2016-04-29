package com.threecore.project.model.format.specific;

import java.io.IOException;

import org.apache.flink.api.java.io.TextOutputFormat;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.core.fs.Path;

import com.threecore.project.model.Friendship;

public class FriendshipWriteFormat extends TextOutputFormat<Friendship> {

	private static final long serialVersionUID = 8570824411134657426L;

	public FriendshipWriteFormat(Path path) {
		super(path);
		super.setWriteMode(FileSystem.WriteMode.OVERWRITE);
	}
	
	@Override
	public void writeRecord(Friendship friendship) throws IOException {
		byte[] bytes = friendship.asString().getBytes(super.getCharsetName());
		super.stream.write(bytes);
		super.stream.write('\n');
	}
}
