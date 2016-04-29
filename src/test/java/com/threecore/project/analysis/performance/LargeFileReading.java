package com.threecore.project.analysis.performance;
import java.io.IOException;

import com.threecore.project.analysis.performance.io.BufferedReaderReading;
import com.threecore.project.analysis.performance.io.BufferedStreamReading;
import com.threecore.project.analysis.performance.io.ByteBufferReading;

public class LargeFileReading {
	
	private static final long ITERATIONS = 3;
	
	private static final String POSTS = "data/dist/posts.dat";
	private static final String COMMENTS = "data/dist/comments.dat";

	public static void main(String[] args) throws IOException {
		String posts = (args.length >= 1) ? args[0] : POSTS;
		String comments = (args.length >= 2) ? args[1] : COMMENTS;
		long iterations = (args.length >= 3) ? Long.valueOf(args[2]) : ITERATIONS;
		
		System.out.println("********************************************************************************");
		System.out.println("* PERFORMANCE ANALYSIS: POST-COMMENT READING");
		System.out.println("********************************************************************************");
		System.out.println("* posts: " + posts);
		System.out.println("* comments: " + comments);
		System.out.println("* iterations: " + iterations);
		System.out.println("********************************************************************************");
		
		testBufferedReaderReading(posts, comments, iterations);
		testBufferedStreamReading(posts, comments, iterations);	
		//testSourceByteBuffer(posts, comments, iterations);
	}
	
	public static void testBufferedReaderReading(final String posts, final String comments, final long iterations) throws IOException {
		System.out.println("********************************************************************************");
		System.out.println("* BUFFERED READER");
		System.out.println("********************************************************************************");
		BufferedReaderReading reader = new BufferedReaderReading();
		double latency = 0.0;
		long numevents = 1;
		for (int i = 1; i <= iterations; i++) {
			System.out.print(i);
			long start = System.currentTimeMillis();
			numevents = reader.read(POSTS, COMMENTS);
			long end = System.currentTimeMillis();
			latency += end - start;			
		}
		latency /= iterations;
		double latency_per_event = latency / numevents;
		System.out.println("********************************************************************************");
		System.out.println("* BUFFERED READER");
		System.out.println("********************************************************************************");
		System.out.println("* LTC: " + latency + " (ms)");
		System.out.println("* EVN: " + numevents + " (events)");
		System.out.println("* LPE: " + latency_per_event + " (ms/event)");
		System.out.println("********************************************************************************");
	}
	
	public static void testBufferedStreamReading(final String posts, final String comments, final long iterations) throws IOException {
		System.out.println("********************************************************************************");
		System.out.println("* BUFFERED STREAM");
		System.out.println("********************************************************************************");
		BufferedStreamReading reader = new BufferedStreamReading();
		double latency = 0.0;
		long numevents = 1;
		for (int i = 1; i <= iterations; i++) {
			System.out.print(i);
			long start = System.currentTimeMillis();
			numevents = reader.read(POSTS, COMMENTS);
			long end = System.currentTimeMillis();
			latency += end - start;			
		}
		latency /= iterations;
		double latency_per_event = latency / numevents;
		System.out.println("********************************************************************************");
		System.out.println("* BUFFERED STREAM");
		System.out.println("********************************************************************************");
		System.out.println("* LTC: " + latency + " (ms)");
		System.out.println("* EVN: " + numevents + " (events)");
		System.out.println("* LPE: " + latency_per_event + " (ms/event)");
		System.out.println("********************************************************************************");
	}
	
	public static void testSourceByteBuffer(final String posts, final String comments, final long iterations) throws IOException {
		System.out.println("********************************************************************************");
		System.out.println("* BYTE BUFFER");
		System.out.println("********************************************************************************");
		ByteBufferReading reader = new ByteBufferReading();
		double latency = 0.0;
		long numevents = 1;
		for (int i = 1; i <= iterations; i++) {
			System.out.print(i);
			long start = System.currentTimeMillis();
			numevents = reader.read(POSTS, COMMENTS);
			long end = System.currentTimeMillis();
			latency += end - start;			
		}
		latency /= iterations;
		double latency_per_event = latency / numevents;
		System.out.println("********************************************************************************");
		System.out.println("* BYTE BUFFER");
		System.out.println("********************************************************************************");
		System.out.println("* LTC: " + latency + " (ms)");
		System.out.println("* EVN: " + numevents + " (events)");
		System.out.println("* LPE: " + latency_per_event + " (ms/event)");
		System.out.println("********************************************************************************");
	}
	
	public static void testVersion3(final long iterations) throws IOException {
		System.out.println("********************************************************************************");
		System.out.println("* VERSION 3");
		System.out.println("********************************************************************************");
		BufferedReaderReading reader = new BufferedReaderReading();
		double latency = 0.0;
		long numevents = 1;
		for (int i = 1; i <= iterations; i++) {
			System.out.print(i);
			long start = System.currentTimeMillis();
			numevents = reader.read(POSTS, COMMENTS);
			long end = System.currentTimeMillis();
			latency += end - start;			
		}
		latency /= iterations;
		double latency_per_event = latency / numevents;
		System.out.println("********************************************************************************");
		System.out.println("* VERSION 3");
		System.out.println("********************************************************************************");
		System.out.println("* LTC: " + latency + " (ms)");
		System.out.println("* EVN: " + numevents + " (events)");
		System.out.println("* LPE: " + latency_per_event + " (ms/event)");
		System.out.println("********************************************************************************");
	}
	
	public static void testVersion4(final long iterations) throws IOException {
		System.out.println("********************************************************************************");
		System.out.println("* VERSION 4");
		System.out.println("********************************************************************************");
		ByteBufferReading reader = new ByteBufferReading();
		double latency = 0.0;
		long numevents = 1;
		for (int i = 1; i <= iterations; i++) {
			System.out.print(i);
			long start = System.currentTimeMillis();
			numevents = reader.read(POSTS, COMMENTS);
			long end = System.currentTimeMillis();
			latency += end - start;			
		}
		latency /= iterations;
		double latency_per_event = latency / numevents;
		System.out.println("********************************************************************************");
		System.out.println("* VERSION 4");
		System.out.println("********************************************************************************");
		System.out.println("* LTC: " + latency + " (ms)");
		System.out.println("* EVN: " + numevents + " (events)");
		System.out.println("* LPE: " + latency_per_event + " (ms/event)");
		System.out.println("********************************************************************************");
	}

}
