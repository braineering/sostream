package com.threecore.project.analysis.dataset;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.flink.api.java.tuple.Tuple3;

import com.threecore.project.SimpleTest;
import com.threecore.project.model.Comment;
import com.threecore.project.model.EventCommentFriendshipLike;
import com.threecore.project.model.Friendship;
import com.threecore.project.model.Like;

public class QueryTwoDatasetAnalysis {
	
	private static final String COMMENTS = SimpleTest.COMMENTS_XLARGE;
	private static final String FRIENDSHIPS = SimpleTest.FRIENDSHIPS_XLARGE;
	private static final String LIKES = SimpleTest.LIKES_XLARGE;
	
	private static long EVENTS;
	private static long NUMCOMMENTS;
	private static double PERCCOMMENTS;
	private static long NUMFRIENDSHIPS;
	private static double PERCFRIENDSHIPS;
	private static long NUMLIKES;
	private static double PERCLIKES;	
	
	private static long CLUSTERS;
	private static long CLUSTERS_MAXDISTANCE;
	private static long CLUSTERS_MINDISTANCE;
	private static double CLUSTERS_AVGDISTANCE;
	
	private static long CCLUSTERS;
	private static double PERCCCLUSTERS;
	private static long CCLUSTERS_MAXSIZE;
	private static long CCLUSTERS_MINSIZE;
	private static double CCLUSTERS_AVGSIZE;
	
	private static long FCLUSTERS;
	private static double PERCFCLUSTERS;
	private static long FCLUSTERS_MAXSIZE;
	private static long FCLUSTERS_MINSIZE;
	private static double FCLUSTERS_AVGSIZE;
	
	private static long LCLUSTERS;
	private static double PERCLCLUSTERS;
	private static long LCLUSTERS_MAXSIZE;
	private static long LCLUSTERS_MINSIZE;
	private static double LCLUSTERS_AVGSIZE;
	
	private static long ts[] = new long[3];

	public static void main(String[] args) throws IOException {	
		String cmnts = (args.length >=1) ? args[0] : COMMENTS;
		String frnds = (args.length >=2) ? args[1] : FRIENDSHIPS;
		String likes = (args.length >=3) ? args[2] : LIKES;
		
		System.out.println("********************************************************************************");
		System.out.println("* DATASET ANALYSIS: Q2");
		System.out.println("********************************************************************************");
		System.out.println("* comments: " + cmnts);
		System.out.println("* friendships: " + frnds);
		System.out.println("* likes: " + likes);
		System.out.println("********************************************************************************");
		
		analyze(cmnts, frnds, likes);
		
		System.out.println("********************************************************************************");
		System.out.println("* DATASET ANALYSIS: Q2");
		System.out.println("********************************************************************************");		
		System.out.println(String.format("EVENTS: total: %d | comments: %d (%.3f) | friendships: %d (%f) | likes: %d (%f)", 
				EVENTS, NUMCOMMENTS, PERCCOMMENTS, NUMFRIENDSHIPS, PERCFRIENDSHIPS, NUMLIKES, PERCLIKES));
		System.out.println(String.format("CLUSTERS: total: %d | max-distance: %d | min-distance: %d | avg-distance: %.3f", 
				CLUSTERS, CLUSTERS_MAXDISTANCE, CLUSTERS_MINDISTANCE, CLUSTERS_AVGDISTANCE));
		System.out.println(String.format("CLUSTERS-C: clusters: %d (%.3f) | max-size: %d | min-size: %d | avg-size: %.3f", 
				CCLUSTERS, PERCCCLUSTERS, CCLUSTERS_MAXSIZE, CCLUSTERS_MINSIZE, CCLUSTERS_AVGSIZE));
		System.out.println(String.format("CLUSTERS-F: clusters: %d (%.3f) | max-size: %d | min-size: %d | avg-size: %.3f", 
				FCLUSTERS, PERCFCLUSTERS, FCLUSTERS_MAXSIZE, FCLUSTERS_MINSIZE, FCLUSTERS_AVGSIZE));
		System.out.println(String.format("CLUSTERS-L: clusters: %d (%.3f) | max-size: %d | min-size: %d | avg-size: %.3f", 
				LCLUSTERS, PERCLCLUSTERS, LCLUSTERS_MAXSIZE, LCLUSTERS_MINSIZE, LCLUSTERS_AVGSIZE));
		System.out.println("********************************************************************************");			
	}
	
	public static void analyze(final String comments, final String friendships, final String likes) throws IOException {
		long ltimestamp = 0, ltype = 0;
		
		Map<Long, Tuple3<Long, Long, Long>> map = new HashMap<Long, Tuple3<Long, Long, Long>>();
		
		FileInputStream commentFile = new FileInputStream(comments);
		FileInputStream friendshipFile = new FileInputStream(friendships);
		FileInputStream likeFile = new FileInputStream(likes);
		
		InputStreamReader commentInput = new InputStreamReader(commentFile);
		InputStreamReader friendshipInput = new InputStreamReader(friendshipFile);
		InputStreamReader likeInput = new InputStreamReader(likeFile);
		
		BufferedReader commentReader = new BufferedReader(commentInput);
		BufferedReader friendshipReader = new BufferedReader(friendshipInput);
		BufferedReader likeReader = new BufferedReader(likeInput);
		
		String commentLine = null;
		String friendshipLine = null;
		String likeLine = null;
		
		Comment comment = null;
		Friendship friendship = null;
		Like like = null;
		EventCommentFriendshipLike event = null;
		
		int nextEvent;
		ts[0] = Long.MAX_VALUE;
		ts[1] = Long.MAX_VALUE;
		ts[2] = Long.MAX_VALUE;		
		
		long numevents = 0;
		while (true) {			
			if (commentReader.ready()) {
				if (comment == null) {
					commentLine = commentReader.readLine();
					if (commentLine != null) {
						comment = Comment.fromString(commentLine);
						ts[0] = comment.getTimestamp();
					} else {
						ts[0] = Long.MAX_VALUE;
					}
				}
			}
			
			if (friendshipReader.ready()) {
				if (friendship == null) {
					friendshipLine = friendshipReader.readLine();
					if (friendshipLine != null) {
						friendship = Friendship.fromString(friendshipLine);
						ts[1] = friendship.getTimestamp();
					} else {
						ts[1] = Long.MAX_VALUE;
					}
				}
			}
			
			if (likeReader.ready()) {
				if (like == null) {
					likeLine = likeReader.readLine();
					if (likeLine != null) {
						like = Like.fromString(likeLine);
						ts[2] = like.getTimestamp();
					} else {
						ts[2] = Long.MAX_VALUE;
					}
				}
			}
			
			if (comment == null && friendship == null && like == null) {
				break;
			}
			
			nextEvent = getNextEvent();
			
			if (nextEvent == 0) {
				event = new EventCommentFriendshipLike(comment);
				comment = null;
				ts[0] = Long.MAX_VALUE;	
			} else if (nextEvent == 1) {
				event = new EventCommentFriendshipLike(friendship);
				friendship = null;
				ts[1] = Long.MAX_VALUE;
			} else if (nextEvent == 2) {
				event = new EventCommentFriendshipLike(like);
				like = null;
				ts[2] = Long.MAX_VALUE;
			}
			
			if (event.isComment()) {
				NUMCOMMENTS++;
			} else if (event.isFriendship()) {
				NUMFRIENDSHIPS++;
			} else if (event.isLike()) {
				NUMLIKES++;
			}
			
			if (event.getTimestamp() == ltimestamp) {
				if (!map.containsKey(ltimestamp)) {
					Tuple3<Long, Long, Long> entry = new Tuple3<Long, Long, Long>();
					entry.f0 = (ltype == EventCommentFriendshipLike.TYPE_COMMENT) ? 1L : 0L;
					entry.f1 = (ltype == EventCommentFriendshipLike.TYPE_FRIENDSHIP) ? 1L : 0L;
					entry.f2 = (ltype == EventCommentFriendshipLike.TYPE_LIKE) ? 1L : 0L;
					map.put(ltimestamp, entry);
				}
				if (event.isComment()) {
					map.get(ltimestamp).f0++;
				} else if (event.isFriendship()) {
					map.get(ltimestamp).f1++;
				} else if (event.isLike()) {
					map.get(ltimestamp).f2++;
				}
			}
			
			ltimestamp = event.getTimestamp();
			ltype = event.getType();
			
			numevents++;
			
			printProgress(numevents);
			
		}
		
		commentReader.close();
		friendshipReader.close();
		likeReader.close();
		
		System.out.print("\n");
		
		EVENTS = NUMCOMMENTS + NUMFRIENDSHIPS + NUMLIKES;
		
		PERCCOMMENTS = NUMCOMMENTS / (double) EVENTS;		
		PERCFRIENDSHIPS = NUMFRIENDSHIPS / (double) EVENTS;
		PERCLIKES = NUMLIKES / (double) EVENTS;
		
		CCLUSTERS = 0;
		FCLUSTERS = 0;
		LCLUSTERS = 0;
		
		CCLUSTERS_MAXSIZE = Long.MIN_VALUE;
		CCLUSTERS_MINSIZE = Long.MAX_VALUE;
		
		FCLUSTERS_MAXSIZE = Long.MIN_VALUE;
		FCLUSTERS_MINSIZE = Long.MAX_VALUE;
		
		LCLUSTERS_MAXSIZE = Long.MIN_VALUE;
		LCLUSTERS_MINSIZE = Long.MAX_VALUE;
		
		CCLUSTERS_AVGSIZE = 0.0;
		FCLUSTERS_AVGSIZE = 0.0;
		LCLUSTERS_AVGSIZE = 0.0;
		
		CLUSTERS_AVGDISTANCE = 0.0;
		
		for (Tuple3<Long, Long, Long> entry : map.values()) {
			long ccluster_size = entry.f0;
			long fcluster_size = entry.f1;
			long lcluster_size = entry.f2;
			
			CCLUSTERS_AVGSIZE += ccluster_size;
			FCLUSTERS_AVGSIZE += fcluster_size;
			LCLUSTERS_AVGSIZE += lcluster_size;
			
			if (ccluster_size > 0) {
				CCLUSTERS++;
			}
			
			if (fcluster_size > 0) {
				FCLUSTERS++;
			}
			
			if (lcluster_size > 0) {
				LCLUSTERS++;
			}
			
			if (ccluster_size > CCLUSTERS_MAXSIZE) {
				CCLUSTERS_MAXSIZE = ccluster_size;
			}
			
			if (ccluster_size < CCLUSTERS_MINSIZE) {
				CCLUSTERS_MINSIZE = ccluster_size;
			}	
			
			if (fcluster_size > FCLUSTERS_MAXSIZE) {
				FCLUSTERS_MAXSIZE = fcluster_size;
			}
			
			if (fcluster_size < FCLUSTERS_MINSIZE) {
				FCLUSTERS_MINSIZE = fcluster_size;
			}
			
			if (lcluster_size > LCLUSTERS_MAXSIZE) {
				LCLUSTERS_MAXSIZE = lcluster_size;
			}
			
			if (lcluster_size < LCLUSTERS_MINSIZE) {
				LCLUSTERS_MINSIZE = lcluster_size;
			}
		}
		
		CCLUSTERS_AVGSIZE /= CCLUSTERS;
		FCLUSTERS_AVGSIZE /= FCLUSTERS;
		LCLUSTERS_AVGSIZE /= LCLUSTERS;
		
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
		
		CLUSTERS = CCLUSTERS + FCLUSTERS + LCLUSTERS;
		
		PERCCCLUSTERS = CCLUSTERS / (double) CLUSTERS;
		PERCFCLUSTERS = FCLUSTERS / (double) CLUSTERS;
		PERCLCLUSTERS = LCLUSTERS / (double) CLUSTERS;
		
		CLUSTERS_AVGDISTANCE /= Math.pow(CLUSTERS, 2);
	}
	
	private static int getNextEvent() {
		if (ts[0] <= ts[1] && ts[0] <= ts[2]) {
			return 0;
		} else if (ts[1] <= ts[2] && ts[1] <= ts[0]) {
			return 1;
		} else {
			return 2;
		}
	}
	
	public static void printProgress(long numevents) {
		if (numevents % 100000 == 0)
			System.out.print(".");
		if (numevents % 8000000 == 0)
			System.out.print("\n");
	}

}
