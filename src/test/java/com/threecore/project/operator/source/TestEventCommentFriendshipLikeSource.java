package com.threecore.project.operator.source;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import com.threecore.project.SimpleTest;
import com.threecore.project.model.Comment;
import com.threecore.project.model.EventCommentFriendshipLike;
import com.threecore.project.model.Friendship;
import com.threecore.project.model.Like;
import com.threecore.project.model.time.ChronologicalComparator;

public class TestEventCommentFriendshipLikeSource extends SimpleTest {
	
	private static long ts[] = new long[3];

	@Test
	public void events() throws IOException {
		List<EventCommentFriendshipLike> LIST = getSortedEvents();
		
		FileInputStream commentFile = new FileInputStream(COMMENTS_TINY);
		FileInputStream friendshipFile = new FileInputStream(FRIENDSHIPS_TINY);
		FileInputStream likeFile = new FileInputStream(LIKES_TINY);
		
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
			
			nextEvent = this.getNextEvent();
			
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
			
			EventCommentFriendshipLike CORRECT_EVENT = LIST.remove(0);
			
			// System.out.println("EVENT: " + event.asString());
			// (DEBUG) System.out.println("\t\tSHOULD BE: " + CORRECT_EVENT.asString());
			
			if (CORRECT_EVENT.getTimestamp() != event.getTimestamp()) {
				assertEquals(CORRECT_EVENT, event);
			}			
		}
		
		commentReader.close();
		friendshipReader.close();
		likeReader.close();
	}
	
	private int getNextEvent() {
		if (ts[0] <= ts[1] && ts[0] <= ts[2]) {
			return 0;
		} else if (ts[1] <= ts[2] && ts[1] <= ts[0]) {
			return 1;
		} else {
			return 2;
		}
	}
	
	private List<EventCommentFriendshipLike> getSortedEvents() throws IOException {
		List<EventCommentFriendshipLike> list = new ArrayList<EventCommentFriendshipLike>();
		
		FileInputStream commentFile = new FileInputStream(COMMENTS_TINY);
		FileInputStream friendshipFile = new FileInputStream(FRIENDSHIPS_TINY);
		FileInputStream likeFile = new FileInputStream(LIKES_TINY);
		
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
		
		while (true) {			
			if (commentReader.ready()) {
				if (comment == null) {
					commentLine = commentReader.readLine();
					if (DEBUG) System.out.println(commentLine);
					if (commentLine != null) {
						comment = Comment.fromString(commentLine);
						assert (comment != null) : "comment must be != null";
						if (DEBUG) System.out.println("==C==> " + comment.asString());
					}
				}
			}
			
			if (friendshipReader.ready()) {
				if (friendship == null) {
					friendshipLine = friendshipReader.readLine();
					if (DEBUG) System.out.println(friendshipLine);
					if (friendshipLine != null) {
						friendship = Friendship.fromString(friendshipLine);
						if (DEBUG) System.out.println("==F==> " + friendship.asString());
					}
				}
			}
			
			if (likeReader.ready()) {
				if (like == null) {
					likeLine = likeReader.readLine();
					if (DEBUG) System.out.println(likeLine);
					if (likeLine != null) {
						like = Like.fromString(likeLine);
						if (DEBUG) System.out.println("==L==> " + like.asString());
					}
				}
			}
			
			if (comment == null && friendship == null && like == null) {
				break;
			}
			
			if (comment != null) {
				event = new EventCommentFriendshipLike(comment);
				list.add(event);
				comment = null;
			}
			
			if (friendship != null) {
				event = new EventCommentFriendshipLike(friendship);
				list.add(event);
				friendship = null;
			}
			
			if (like != null) {
				event = new EventCommentFriendshipLike(like);
				list.add(event);
				like = null;
			}
		}
		
		commentReader.close();
		friendshipReader.close();
		likeReader.close();	
		
		Collections.sort(list, new ChronologicalComparator<EventCommentFriendshipLike>());
		
		return list;
	}
	
	@Test
	public void serialization() {
		SerializationUtils.serialize(new EventCommentFriendshipLikeSource());
	}

}
