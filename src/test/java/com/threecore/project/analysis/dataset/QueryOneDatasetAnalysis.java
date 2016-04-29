package com.threecore.project.analysis.dataset;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.flink.api.java.tuple.Tuple2;

import com.threecore.project.SimpleTest;
import com.threecore.project.model.Comment;
import com.threecore.project.model.EventPostComment;
import com.threecore.project.model.Post;

public class QueryOneDatasetAnalysis {
	
	private static final String POSTS = SimpleTest.POSTS_XLARGE;
	private static final String COMMENTS = SimpleTest.COMMENTS_XLARGE;
	
	private static long EVENTS;
	private static long NUMPOSTS;
	private static double PERCPOSTS;
	private static long NUMCOMMENTS;
	private static double PERCCOMMENTS;
	
	private static long CLUSTERS;
	private static long CLUSTERS_MAXDISTANCE;
	private static long CLUSTERS_MINDISTANCE;
	private static double CLUSTERS_AVGDISTANCE;
	
	private static long PCLUSTERS;
	private static double PERCPCLUSTERS;
	private static long PCLUSTERS_MAXSIZE;
	private static long PCLUSTERS_MINSIZE;
	private static double PCLUSTERS_AVGSIZE;
	
	private static long CCLUSTERS;
	private static double PERCCCLUSTERS;
	private static long CCLUSTERS_MAXSIZE;
	private static long CCLUSTERS_MINSIZE;
	private static double CCLUSTERS_AVGSIZE;
	

	public static void main(String[] args) throws IOException {
		String posts = (args.length >=1) ? args[0] : POSTS;
		String cmnts = (args.length >=1) ? args[1] : COMMENTS;
		
		System.out.println("********************************************************************************");
		System.out.println("* DATASET ANALYSIS: Q1");
		System.out.println("********************************************************************************");
		System.out.println("* posts: " + posts);
		System.out.println("* comments: " + cmnts);
		System.out.println("********************************************************************************");
		
		analyze(posts, cmnts);
		
		System.out.println("********************************************************************************");
		System.out.println("* DATASET ANALYSIS: Q1");
		System.out.println("********************************************************************************");		
		System.out.println(String.format("* EVENTS: total: %d | posts: %d (%.3f) | comments: %d (%.3f)", 
				EVENTS, NUMPOSTS, PERCPOSTS, NUMCOMMENTS, PERCCOMMENTS));
		System.out.println(String.format("* CLUSTERS: total: %d | max-distance: %d | min-distance: %d | avg-distance: %.3f", 
				CLUSTERS, CLUSTERS_MAXDISTANCE, CLUSTERS_MINDISTANCE, CLUSTERS_AVGDISTANCE));
		System.out.println(String.format("* CLUSTERS-P: clusters: %d (%.3f) | max-size: %d | min-size: %d | avg-size: %.3f", 
				PCLUSTERS, PERCPCLUSTERS, PCLUSTERS_MAXSIZE, PCLUSTERS_MINSIZE, PCLUSTERS_AVGSIZE));
		System.out.println(String.format("* CLUSTERS-C: clusters: %d (%.3f) | max-size: %d | min-size: %d | avg-size: %.3f", 
				CCLUSTERS, PERCCCLUSTERS, CCLUSTERS_MAXSIZE, CCLUSTERS_MINSIZE, CCLUSTERS_AVGSIZE));
		System.out.println("********************************************************************************");		
	}
	
	public static void analyze(final String posts, final String cmnts) throws IOException {
		long ltimestamp = 0, ltype = 0;
		
		Map<Long, Tuple2<Long, Long>> map = new HashMap<Long, Tuple2<Long, Long>>();
		
		FileInputStream postFile = new FileInputStream(posts);
		FileInputStream commentFile = new FileInputStream(cmnts);
		
		InputStreamReader postInput = new InputStreamReader(postFile);
		InputStreamReader commentInput = new InputStreamReader(commentFile);
		
		BufferedReader postReader = new BufferedReader(postInput);
		BufferedReader commentReader = new BufferedReader(commentInput);
		
		String postLine = null;
		String commentLine = null;
		
		Post post = null;
		Comment comment = null;
		EventPostComment event = null;
		
		int nextEvent;
		long ts[] = new long[2];
		ts[0] = Long.MAX_VALUE;
		ts[1] = Long.MAX_VALUE;
		
		long numevents = 0;
		while (true) {
			if (postReader.ready()) {
				if (post == null) {
					postLine = postReader.readLine();
					if (postLine != null) {
						post = Post.fromString(postLine);
						ts[0] = post.getTimestamp();
					} else {
						ts[0] = Long.MAX_VALUE;
					}
				}
			}
			
			if (commentReader.ready()) {
				if (comment == null) {
					commentLine = commentReader.readLine();
					if (commentLine != null) {
						comment = Comment.fromString(commentLine);
						ts[1] = comment.getTimestamp();
					} else {
						ts[1] = Long.MAX_VALUE;
					}
				}
			}
			
			if (post == null && comment == null) {
				break;
			}
			
			nextEvent = getNextEvent(ts);
			
			if (nextEvent == 0) {
				event = new EventPostComment(post);
				post = null;
				ts[0] = Long.MAX_VALUE;
			} else if (nextEvent == 1) {
				event = new EventPostComment(comment);
				comment = null;
				ts[1] = Long.MAX_VALUE;
			}	
			
			if (event.isPost()) {
				NUMPOSTS++;
			} else if (event.isComment()) {
				NUMCOMMENTS++;
			}
			
			if (event.getTimestamp() == ltimestamp) {
				if (!map.containsKey(ltimestamp)) {
					Tuple2<Long, Long> entry = new Tuple2<Long, Long>();
					entry.f0 = (ltype == EventPostComment.TYPE_POST) ? 1L : 0L;
					entry.f1 = (ltype == EventPostComment.TYPE_COMMENT) ? 1L : 0L;
					map.put(ltimestamp, entry);
				}
				if (event.isPost()) {
					map.get(ltimestamp).f0++;
				} else if (event.isComment()) {
					map.get(ltimestamp).f1++;
				}
			}
			
			ltimestamp = event.getTimestamp();
			ltype = event.getType();
			
			numevents++;
			
			printProgress(numevents);
		}
		
		postReader.close();		
		commentReader.close();	
		
		System.out.print("\n");
		
		EVENTS = NUMPOSTS + NUMCOMMENTS;
		
		PERCPOSTS = NUMPOSTS / (double) EVENTS;
		PERCCOMMENTS = NUMCOMMENTS / (double) EVENTS;
		
		PCLUSTERS = 0;
		CCLUSTERS = 0;
		
		PCLUSTERS_MAXSIZE = Long.MIN_VALUE;
		PCLUSTERS_MINSIZE = Long.MAX_VALUE;
		
		CCLUSTERS_MAXSIZE = Long.MIN_VALUE;
		CCLUSTERS_MINSIZE = Long.MAX_VALUE;
		
		CLUSTERS_MAXDISTANCE = Long.MIN_VALUE;
		CLUSTERS_MINDISTANCE = Long.MAX_VALUE;
		
		PCLUSTERS_AVGSIZE = 0.0;
		CCLUSTERS_AVGSIZE = 0.0;
		
		CLUSTERS_AVGDISTANCE = 0.0;
		
		for (Tuple2<Long, Long> entry : map.values()) {
			long pcluster_size = entry.f0;
			long ccluster_size = entry.f1;
			
			PCLUSTERS_AVGSIZE += pcluster_size;
			CCLUSTERS_AVGSIZE += ccluster_size;
			
			if (pcluster_size > 0) {
				PCLUSTERS++;
			}
			
			if (ccluster_size > 0) {
				CCLUSTERS++;
			}
			
			if (pcluster_size > PCLUSTERS_MAXSIZE) {
				PCLUSTERS_MAXSIZE = pcluster_size;
			}
			
			if (pcluster_size < PCLUSTERS_MINSIZE) {
				PCLUSTERS_MINSIZE = pcluster_size;
			}
			
			if (ccluster_size > CCLUSTERS_MAXSIZE) {
				CCLUSTERS_MAXSIZE = ccluster_size;
			}
			
			if (ccluster_size < CCLUSTERS_MINSIZE) {
				CCLUSTERS_MINSIZE = ccluster_size;
			}			
		}
		
		PCLUSTERS_AVGSIZE /= PCLUSTERS;
		CCLUSTERS_AVGSIZE /= CCLUSTERS;
		
		for (long timestamp1 : map.keySet()) {
			for (long timestamp2 : map.keySet()) {
				if (timestamp1 == timestamp2) {
					continue;
				}
				long distance = Math.abs(timestamp1 - timestamp2);
				CLUSTERS_AVGDISTANCE += distance;
				if (distance > CLUSTERS_MAXDISTANCE) {
					CLUSTERS_MAXDISTANCE = distance;
				}
				if (distance < CLUSTERS_MINDISTANCE) {
					CLUSTERS_MINDISTANCE = distance;
				}
			}
		}		
		
		CLUSTERS = PCLUSTERS + CCLUSTERS;
		
		PERCPCLUSTERS = PCLUSTERS / (double) CLUSTERS;
		PERCCCLUSTERS = CCLUSTERS / (double) CLUSTERS;		
		
		CLUSTERS_AVGDISTANCE /= Math.pow(CLUSTERS, 2);
	}
	
	public static int getNextEvent(long ts[]) {
		if (ts[0] <= ts[1]) {
			return 0;
		} else {
			return 1;
		}
	}
	
	public static void printProgress(long numevents) {
		if (numevents % 100000 == 0)
			System.out.print(".");
		if (numevents % 8000000 == 0)
			System.out.print("\n");
	}

}
