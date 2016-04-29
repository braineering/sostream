package com.threecore.project.analysis.performance.rank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.threecore.project.model.PostScore;
import com.threecore.project.model.score.post.comparator.PostScoreComparatorDesc;

public class SortBasedRanking {
	
	private static final int INIT = 100000;
	private static final int MAX = 1000000;
	private static final int STEP = 100000;	
	private static final int ITERATIONS = 10;
	
	public static void main(String[] args) throws IOException {
		int init = (args.length >= 1) ? Integer.valueOf(args[0]) : INIT;
		int max = (args.length >= 2) ? Integer.valueOf(args[1]) : MAX;
		int step = (args.length >= 3) ? Integer.valueOf(args[2]) : STEP;
		int iterations = (args.length >= 4) ? Integer.valueOf(args[3]) : ITERATIONS;
		
		System.out.println("********************************************************************************");
		System.out.println("* PERFORMANCE ANALYSIS: POST-SCORE SORTING");
		System.out.println("********************************************************************************");
		System.out.println("* init: " + init);
		System.out.println("* max: " + max);
		System.out.println("* step: " + step);
		System.out.println("* iterations: " + iterations);
		System.out.println("********************************************************************************");
		
		testSorting1(init, max, step, iterations);
		testSorting2(init, max, step, iterations);
		testSorting3(init, max, step, iterations);
	}
	
	public static List<PostScore> getPostScores(final int max) {
		List<PostScore> scores = new ArrayList<PostScore>(max);
		for (long n = 1; n <= max; n++) {
			scores.add(new PostScore(1, n, n, n, "USR-1", n % 10, n % 20, n % 30));
		}		
		return scores;
	}

	public static void testSorting1(final int init, final int max, final int step, final int iterations) throws IOException {
		System.out.println("********************************************************************************");
		System.out.println("* COLLECTIONS.SORT");
		System.out.println("********************************************************************************");
		Map<Integer, Double> avg_latencies = new HashMap<Integer, Double>();
		List<PostScore> scores = getPostScores(max);
		for (int n = init; n <= max; n += step) {
			double latency = 0.0;
			for (int i = 1; i <= iterations; i++) {
				List<PostScore> list = scores.subList(0, n-1);
				long start = System.currentTimeMillis();
				Collections.sort(list, PostScoreComparatorDesc.getInstance());
				long end = System.currentTimeMillis();
				latency += end - start;			
			}
			latency /= iterations;
			avg_latencies.put(n, latency);
		}
		
		System.out.println("********************************************************************************");
		System.out.println("* COLLECTIONS.SORT");
		System.out.println("********************************************************************************");
		for (Map.Entry<Integer, Double> entry : avg_latencies.entrySet()) {
			System.out.println("* " + entry.getKey() + ": " + entry.getValue() + "(ms)");
		}
		System.out.println("********************************************************************************");
	}
	
	public static void testSorting2(final int init, final int max, final int step, final int iterations) throws IOException {
		System.out.println("********************************************************************************");
		System.out.println("* STREAM.SORT");
		System.out.println("********************************************************************************");
		Map<Integer, Double> avg_latencies = new HashMap<Integer, Double>();
		List<PostScore> scores = getPostScores(max);
		for (int n = init; n <= max; n += step) {
			double latency = 0.0;
			for (int i = 1; i <= iterations; i++) {
				List<PostScore> list = scores.subList(0, n-1);
				long start = System.currentTimeMillis();
				list.stream().sorted(PostScoreComparatorDesc.getInstance()).collect(Collectors.toList());
				long end = System.currentTimeMillis();
				latency += end - start;			
			}
			latency /= iterations;
			avg_latencies.put(n, latency);
		}
		
		System.out.println("********************************************************************************");
		System.out.println("* STREAM.SORT");
		System.out.println("********************************************************************************");
		for (Map.Entry<Integer, Double> entry : avg_latencies.entrySet()) {
			System.out.println("* " + entry.getKey() + ": " + entry.getValue() + "(ms)");
		}
		System.out.println("********************************************************************************");
	}
	
	public static void testSorting3(final int init, final int max, final int step, final int iterations) throws IOException {
		System.out.println("********************************************************************************");
		System.out.println("* PARALLELSTREAM.SORT");
		System.out.println("********************************************************************************");
		Map<Integer, Double> avg_latencies = new HashMap<Integer, Double>();
		List<PostScore> scores = getPostScores(max);
		for (int n = init; n <= max; n += step) {
			double latency = 0.0;
			for (int i = 1; i <= iterations; i++) {
				List<PostScore> list = scores.subList(0, n-1);
				long start = System.currentTimeMillis();
				list.parallelStream().sorted(PostScoreComparatorDesc.getInstance()).collect(Collectors.toList());
				long end = System.currentTimeMillis();
				latency += end - start;			
			}
			latency /= iterations;
			avg_latencies.put(n, latency);
		}
		
		System.out.println("********************************************************************************");
		System.out.println("* PARALLELSTREAM.SORT");
		System.out.println("********************************************************************************");
		for (Map.Entry<Integer, Double> entry : avg_latencies.entrySet()) {
			System.out.println("* " + entry.getKey() + ": " + entry.getValue() + "(ms)");
		}
		System.out.println("********************************************************************************");
	}

}
