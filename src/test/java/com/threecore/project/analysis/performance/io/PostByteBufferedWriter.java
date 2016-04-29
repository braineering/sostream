package com.threecore.project.analysis.performance.io;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.threecore.project.model.Post;

public class PostByteBufferedWriter {
	
	public static int BUFFSIZE = 32768;
	public static int FLUSHSIZE = 10;
	
	private String path;
	private int buffsize;
	
	public PostByteBufferedWriter(final String path, final int buffsize) throws IOException {
		this.path = path;			
		this.buffsize = buffsize;
	}
	
	public long write(final Post post, final long n) throws IOException {
		File file = new File(this.path);
		if(!file.exists()) {
			file.createNewFile();
		} 		
			    
	    Path path = Paths.get(this.path);
	    AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.WRITE);

	    ByteBuffer buffer = ByteBuffer.allocate(this.buffsize);
		
		for (long i = 0; i < n; i++) {
			buffer.put((post.asString() + '\n').getBytes());
			if (i % FLUSHSIZE == 0) {
				buffer.flip();
				fileChannel.write(buffer, 0);
				//Future<Integer> operation = fileChannel.write(buffer, 0);
				//while(!operation.isDone());
				buffer.clear();
			}			
		}
		
		buffer.flip();
		fileChannel.write(buffer, 0);
		buffer.clear();
		
		return n;
	}

}
