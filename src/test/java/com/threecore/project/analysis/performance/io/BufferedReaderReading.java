package com.threecore.project.analysis.performance.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class BufferedReaderReading {	
	
	public BufferedReaderReading() {}
	
	public long read(final String posts, final String comments) throws IOException {		        
		BufferedReader postReader;
		
		FileInputStream postFile = new FileInputStream(posts);
		
		InputStreamReader postInput = new InputStreamReader(postFile);
		
		postReader = new BufferedReader(postInput);
		
		String postLine = null;
		
		long numevents = 0;
		while (postReader.ready()) {		
			postLine = postReader.readLine();
			
			if (postLine == null) break;
			
			numevents++;					
			
			this.printProgress(numevents);
		}
		
		postReader.close();			
		
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
