package com.threecore.project.analysis.performance.io;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ByteBufferReading {	
	
	private static final int BUFFSIZE = 1024;
	
	public ByteBufferReading() {}
	
	public long read(final String posts, final String comments) throws IOException {
		RandomAccessFile pfile = new RandomAccessFile(posts, "r");
		RandomAccessFile cfile = new RandomAccessFile(comments, "r");
		
		FileChannel pChannel = pfile.getChannel();
		FileChannel cChannel = cfile.getChannel();
        
		ByteBuffer pbuffer = ByteBuffer.allocate(BUFFSIZE);
		ByteBuffer cbuffer = ByteBuffer.allocate(BUFFSIZE);  
		
		String postLine = "";
		String commentLine = "";
		
		long numevents = 0;
		int pb = 0;
		int cb = 0;
		while (true) {
			
			postLine = "";
			commentLine = "";
			
			while ((pb = pChannel.read(pbuffer)) > 0) {
				pbuffer.flip();
				boolean foundLine = false;
				for (int i = 0; i < pbuffer.limit(); i++) {
					char c = (char)pbuffer.get();
					if (c == '\n') {
						foundLine = true;
						break;
					}
					postLine += c;
	            }
				pbuffer.clear();
				if (foundLine) break;
			}
			
			while ((cb = cChannel.read(cbuffer)) > 0) {
				cbuffer.flip();
				boolean foundLine = false;
				for (int i = 0; i < cbuffer.limit(); i++) {
					char c = (char)cbuffer.get();
					if (c == '\n') {
						foundLine = true;
						break;
					}
					commentLine += c;
	            }
				cbuffer.clear();
				if (foundLine) break;
			}	
			
			if (pb != 0) numevents++;
			if (cb != 0) numevents++;			
			if (pb == 0 && cb == 0) break;	
			
			this.printProgress(numevents);			
		}
		
		pChannel.close();
		cChannel.close();
		pfile.close();
		cfile.close();
		
		System.out.print("\n");
		
		return numevents;
	}
	
	public void printProgress(long numevents) {
		if (numevents % 10000 == 0)
			System.out.print(".");
		if (numevents % 800000 == 0)
			System.out.print("\n");
	}	

}
