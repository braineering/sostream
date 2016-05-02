package com.threecore.project.operator.sink;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import com.threecore.project.model.type.Stringable;

public class AsStringSink<T extends Stringable> extends RichSinkFunction<T> {
	
	private static final long serialVersionUID = 1L;
	
	public static final String DEFAULT_PATH = "out-as-string.txt";
	public static final int DEFAULT_BUFFSIZE = 24576;
	
	private BufferedWriter writer;
	private String path;
	private int buffsize;
	
	public AsStringSink(final String path, final int buffsize) throws IOException {
		this.path = path;
		this.buffsize = buffsize;
	}
	
	public AsStringSink(final String path) throws IOException {
		this.path = path;
		this.buffsize = DEFAULT_BUFFSIZE;
	}
	
	public AsStringSink() {
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
		/*BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("tuples.txt")));
		
		long tuples = 0;
		long start = 0;
		
		String line = reader.readLine();
		if (line != null) {
			start = Long.valueOf(line);
		}
		
		line = reader.readLine();
		if (line != null) {
			tuples = Long.valueOf(line);
		}
		
		long end = System.currentTimeMillis();
		long elapsed = end - start;
		
		double latencyPerTuple = elapsed / tuples;
		
		reader.close();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter("stats.txt", false));
		String performanceString = String.format("%.6f %.6f", elapsed / 1000.0, latencyPerTuple / 1000.0);
		writer.write(performanceString);
		
		writer.flush();
		writer.close();
		
		System.exit(0);
		*/
		
	}

	@Override
	public void invoke(T elem) throws Exception {
		this.writer.write(elem.asString());
		this.writer.newLine();
	}

}
