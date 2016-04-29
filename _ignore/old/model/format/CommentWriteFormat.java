package com.threecore.project.model.format.specific;

import java.io.IOException;

import org.apache.flink.api.java.io.TextOutputFormat;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.core.fs.Path;

import com.threecore.project.model.Comment;

public class CommentWriteFormat extends TextOutputFormat<Comment> {

	private static final long serialVersionUID = -7318081740790508154L;

	public CommentWriteFormat(Path path) {
		super(path);
		super.setWriteMode(FileSystem.WriteMode.OVERWRITE);
	}
	
	@Override
	public void writeRecord(Comment comment) throws IOException {
		byte[] bytes = comment.asString().getBytes(super.getCharsetName());
		super.stream.write(bytes);
		super.stream.write('\n');
	}

}
