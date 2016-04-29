package com.threecore.project.analysis.performance.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.threecore.project.model.Post;

public class PostBufferedWriter {
	
	public static int BUFFSIZE = 32768;
	
	private String path;
	private int buffsize;
	
	public PostBufferedWriter(final String path, final int buffsize) throws IOException {
		this.path = path;			
		this.buffsize = buffsize;
	}
	
	public long write(final Post post, final long n) throws IOException {
		File file = new File(this.path);
		if(!file.exists()) {
			file.createNewFile();
		} 		
		BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsolutePath(), false), this.buffsize);	
		
		for (long i = 0; i < n; i++) {
			writer.write(post.asString());
		}
		
		writer.close();		
		
		return n;
	}

}
