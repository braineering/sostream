package com.threecore.project.model.deprecated;

import java.io.IOException;

import org.apache.flink.api.java.io.TextOutputFormat;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.core.fs.Path;

@Deprecated
public class OLDPostRankEntryWriteFormat extends TextOutputFormat<OLDPostRankEntry> {

	private static final long serialVersionUID = 9149392063077936920L;

	public OLDPostRankEntryWriteFormat(Path path) {
		super(path);
		super.setWriteMode(FileSystem.WriteMode.OVERWRITE);
	}
	
	@Override
	public void writeRecord(OLDPostRankEntry entry) throws IOException {
		byte[] bytes = entry.asString().getBytes(super.getCharsetName());
		super.stream.write(bytes);
		super.stream.write('\n');
	}
}
