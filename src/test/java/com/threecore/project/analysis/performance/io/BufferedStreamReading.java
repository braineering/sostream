package com.threecore.project.analysis.performance.io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class BufferedStreamReading {	
	
	public static final int BUFFSIZE = 4096;
	
	public BufferedStreamReading() {}
	
	private int getNewLinePosition(byte buffer[]) {
		for (int i = 0; i < buffer.length; i++) {
			if (buffer[i] == '\n')
				return i;
		}
		return -1;
	}
	
	public long read(final String posts, final String comments) throws IOException {		        
		BufferedInputStream pstream = new BufferedInputStream(new FileInputStream(posts));
		
		byte[] pbytebuffer = new byte[BUFFSIZE];
		int pbytes = 0;
		
		String pline = null;
		
		long numevents = 0;
		int i = -1;
		while (true) {
			
			do {
			    pbytes = pstream.read(pbytebuffer, 0, BUFFSIZE);
			} while (pbytes >= 0 && (i = getNewLinePosition(pbytebuffer)) != -1);
			
			if (pbytes == 0) break;
			
			
			
			this.printProgress(numevents);
		}
		
		pstream.close();	
		
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
