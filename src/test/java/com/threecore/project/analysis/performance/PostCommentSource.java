package com.threecore.project.analysis.performance;
import java.io.IOException;

import com.threecore.project.analysis.performance.source.PostCommentSource1;
import com.threecore.project.analysis.performance.source.PostCommentSource2;

public class PostCommentSource {	
	
	private static final String POSTS = "dataset/dist/xlarge/posts.dat";
	private static final String COMMENTS = "dataset/dist/xlarge/comments.dat";
	
	private static final long ITERATIONS = 3;

	public static void main(String[] args) throws IOException {
		String posts = (args.length >= 1) ? args[0] : POSTS;
		String comments = (args.length >= 2) ? args[1] : COMMENTS;
		long iterations = (args.length >= 3) ? Long.valueOf(args[2]) : ITERATIONS;
		
		System.out.println("********************************************************************************");
		System.out.println("* PERFORMANCE ANALYSIS: POST-COMMENT SOURCE");
		System.out.println("********************************************************************************");
		System.out.println("* posts: " + posts);
		System.out.println("* comments: " + comments);
		System.out.println("* iterations: " + iterations);
		System.out.println("********************************************************************************");
		
		testPostCommentSource1(posts, comments, iterations);
		testPostCommentSource2(posts, comments, iterations);
	}
	
	public static void testPostCommentSource1(final String posts, final String comments, final long iterations) throws IOException {
		System.out.println("********************************************************************************");
		System.out.println("* VERSION 1");
		System.out.println("********************************************************************************");
		PostCommentSource1 reader = new PostCommentSource1();
		double avglatency = 0.0;
		long latency = 0;
		long numevents = 1;
		for (int i = 1; i <= iterations; i++) {
			System.out.println(i);
			long start = System.currentTimeMillis();
			numevents = reader.read(posts, comments);
			long end = System.currentTimeMillis();
			latency = end - start;
			avglatency += latency;			
			System.out.println(latency);
		}
		avglatency /= iterations;
		double latency_per_event = avglatency / numevents;
		System.out.print("\n");
		System.out.println("********************************************************************************");
		System.out.println("* VERSION 1");
		System.out.println("********************************************************************************");
		System.out.println("* LTC: " + avglatency + " (ms)");
		System.out.println("* EVN: " + numevents + " (events)");
		System.out.println("* LPE: " + latency_per_event + " (ms/event)");
		System.out.println("********************************************************************************");
	}
	
	public static void testPostCommentSource2(final String posts, final String comments, final long iterations) throws IOException {
		System.out.println("********************************************************************************");
		System.out.println("* VERSION 2");
		System.out.println("********************************************************************************");
		PostCommentSource2 reader = new PostCommentSource2();
		double avglatency = 0.0;
		long latency = 0;
		long numevents = 1;
		for (int i = 1; i <= iterations; i++) {
			System.out.println(i);
			long start = System.currentTimeMillis();
			numevents = reader.read(POSTS, COMMENTS);
			long end = System.currentTimeMillis();
			latency = end - start;
			avglatency += latency;
			System.out.println(latency);
		}
		avglatency /= iterations;
		double latency_per_event = avglatency / numevents;
		System.out.print("\n");
		System.out.println("********************************************************************************");
		System.out.println("* VERSION 2");
		System.out.println("********************************************************************************");
		System.out.println("* LTC: " + avglatency + " (ms)");
		System.out.println("* EVN: " + numevents + " (events)");
		System.out.println("* LPE: " + latency_per_event + " (ms/event)");
		System.out.println("********************************************************************************");
	}

}
