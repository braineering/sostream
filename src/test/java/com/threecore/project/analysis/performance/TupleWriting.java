package com.threecore.project.analysis.performance;
import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.threecore.project.analysis.performance.io.PostBufferedWriter;
import com.threecore.project.analysis.performance.io.PostByteBufferedWriter;
import com.threecore.project.model.Post;

public class TupleWriting {
	
	private static final long OPERATIONS = 100000;
	private static final long ITERATIONS = 3;
	
	private static final String PATH = "src/test/resources/performance/LARGE-FILE-WRITE";
	
	private static final Post POST = new Post(new DateTime(2016, 1, 1, 12, 0, 0, 500, DateTimeZone.UTC), 101, 1, "PST-101", "USR-1");

	public static void main(String[] args) throws IOException {
		long operations = (args.length >= 1) ? Long.valueOf(args[0]) : OPERATIONS;
		long iterations = (args.length >= 2) ? Long.valueOf(args[1]) : ITERATIONS;
		
		System.out.println("********************************************************************************");
		System.out.println("* PERFORMANCE ANALYSIS: POST WRITING");
		System.out.println("********************************************************************************");
		System.out.println("* operations: " + operations);
		System.out.println("* iterations: " + iterations);
		System.out.println("********************************************************************************");
		
		testBufferedWriter(operations, 8192, iterations);
		testBufferedWriter(operations, 16384, iterations);
		testBufferedWriter(operations, 32768, iterations);
		testBufferedWriter(operations, 65536, iterations);
		
		testByteBufferWriter(operations, 8192, iterations);
		testByteBufferWriter(operations, 16384, iterations);
		testByteBufferWriter(operations, 32768, iterations);
		testByteBufferWriter(operations, 65536, iterations);
	}
	
	public static void testBufferedWriter(final long operations, final int buffsize, final long iterations) throws IOException {
		System.out.println("********************************************************************************");
		System.out.println("* BUFFERED WRITER (buffsize: " + buffsize + ")");
		System.out.println("********************************************************************************");
		PostBufferedWriter writer = new PostBufferedWriter(PATH, buffsize);
		double latency = 0.0;
		long numevents = 1;
		for (int i = 1; i <= iterations; i++) {
			System.out.println(i);
			long start = System.currentTimeMillis();
			numevents = writer.write(POST, operations);
			long end = System.currentTimeMillis();
			latency += end - start;			
		}
		latency /= iterations;
		double latency_per_event = latency / numevents;
		System.out.println("********************************************************************************");
		System.out.println("* BUFFERED WRITER (buffsize: " + buffsize + ")");
		System.out.println("********************************************************************************");
		System.out.println("* LTC: " + latency + " (ms)");
		System.out.println("* EVN: " + numevents + " (events)");
		System.out.println("* LPE: " + latency_per_event + " (ms/event)");
		System.out.println("********************************************************************************");
	}
	
	public static void testByteBufferWriter(final long operations, final int buffsize, final long iterations) throws IOException {
		System.out.println("********************************************************************************");
		System.out.println("* BYTE BUFFER WRITER (buffsize: " + buffsize + ")");
		System.out.println("********************************************************************************");
		PostByteBufferedWriter writer = new PostByteBufferedWriter(PATH, buffsize);
		double latency = 0.0;
		long numevents = 1;
		for (int i = 1; i <= iterations; i++) {
			System.out.println(i);
			long start = System.currentTimeMillis();
			numevents = writer.write(POST, operations);
			long end = System.currentTimeMillis();
			latency += end - start;			
		}
		latency /= iterations;
		double latency_per_event = latency / numevents;
		System.out.println("********************************************************************************");
		System.out.println("* BYTE BUFFER WRITER (buffsize: " + buffsize + ")");
		System.out.println("********************************************************************************");
		System.out.println("* LTC: " + latency + " (ms)");
		System.out.println("* EVN: " + numevents + " (events)");
		System.out.println("* LPE: " + latency_per_event + " (ms/event)");
		System.out.println("********************************************************************************");
	}

}
