package com.threecore.project.operator.sink;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import com.threecore.project.model.type.Stringable;

public class ToStringSink<T extends Stringable> extends RichSinkFunction<T> {
	
	private static final long serialVersionUID = 1L;
	
	public static final String DEFAULT_PATH = "out-as-string.txt";
	public static final int DEFAULT_BUFFSIZE = 24576;
	
	private BufferedWriter writer;
	private String path;
	private int buffsize;
	
	public ToStringSink(final String path, final int buffsize) throws IOException {
		this.path = path;
		this.buffsize = buffsize;
	}
	
	public ToStringSink(final String path) throws IOException {
		this.path = path;
		this.buffsize = DEFAULT_BUFFSIZE;
	}
	
	public ToStringSink() {
		this.path = DEFAULT_PATH;
		this.buffsize = DEFAULT_BUFFSIZE;
	}
	
	@Override
	public void open(Configuration conf) throws IOException {
		File file = new File(this.path);
		if(!file.exists()) {
			file.createNewFile();
		} 		
		this.writer = new BufferedWriter(new FileWriter(file.getAbsolutePath(), false), this.buffsize);
	}
	
	@Override
	public void close() throws IOException {
		this.writer.flush();
		this.writer.close();
	}

	@Override
	public void invoke(T elem) throws Exception {
		this.writer.write(elem.asString());
		this.writer.newLine();
	}

}
