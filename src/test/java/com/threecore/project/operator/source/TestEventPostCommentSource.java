package com.threecore.project.operator.source;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import com.threecore.project.SimpleTest;
import com.threecore.project.model.Comment;
import com.threecore.project.model.EventPostComment;
import com.threecore.project.model.Post;

public class TestEventPostCommentSource extends SimpleTest {
	
	private static long ts[] = new long[2];

	@Test
	public void events() throws IOException {
		List<EventPostComment> LIST = CommonsSource.getSortedEvents(POSTS_TINY, COMMENTS_TINY);
		
		FileInputStream postFile = new FileInputStream(POSTS_TINY);
		FileInputStream commentFile = new FileInputStream(COMMENTS_TINY);
		
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
		ts[0] = Long.MAX_VALUE;
		ts[1] = Long.MAX_VALUE;
		
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
			
			nextEvent = getNextEvent();
			
			if (nextEvent == 0) {
				event = new EventPostComment(post);
				post = null;
				ts[0] = Long.MAX_VALUE;
			} else if (nextEvent == 1) {
				event = new EventPostComment(comment);
				comment = null;
				ts[1] = Long.MAX_VALUE;
			}	
			
			assertNotEquals(null, event);	
			if (event.isPost()) {
				assertNotNull(event.getPost());
				assertNotNull(event.getPost().getTimestamp());
				assertNotNull(event.getPost().getPostId());
				assertNotNull(event.getPost().getUserId());
				assertNotNull(event.getPost().getPost());
				assertNotNull(event.getPost().getUser());
			} else if (event.isComment()) {
				assertNotNull(event.getComment());
				assertNotNull(event.getComment().getTimestamp());
				assertNotNull(event.getComment().getCommentId());
				assertNotNull(event.getComment().getUserId());
				assertNotNull(event.getComment().getComment());
				assertNotNull(event.getComment().getPostCommentedId());
				assertNotNull(event.getComment().getCommentRepliedId());
			} else if (event.isEOF()) {
				assertNotNull(event.getPost());
				assertNotNull(event.getPost().getTimestamp());
				assertNotNull(event.getPost().getPostId());
				assertNotNull(event.getPost().getUserId());
				assertNotNull(event.getPost().getPost());
				assertNotNull(event.getPost().getUser());
				assertNotNull(event.getComment());
				assertNotNull(event.getComment().getTimestamp());
				assertNotNull(event.getComment().getCommentId());
				assertNotNull(event.getComment().getUserId());
				assertNotNull(event.getComment().getComment());
				assertNotNull(event.getComment().getPostCommentedId());
				assertNotNull(event.getComment().getCommentRepliedId());
			}
			
			EventPostComment CORRECT_EVENT = LIST.remove(0);
			
			//System.out.println("EVENT: " + event.asString());
			//System.out.println("\t\tSHOULD BE: " + CORRECT_EVENT.asString());
			
			if (CORRECT_EVENT.getTimestamp() != event.getTimestamp()) {
				assertEquals(CORRECT_EVENT, event);
			}
		}
		
		postReader.close();		
		commentReader.close();	
	}
	
	private int getNextEvent() {
		if (ts[0] <= ts[1]) {
			return 0;
		} else {
			return 1;
		}
	}
	
	@Test
	public void serialization() {
		SerializationUtils.serialize(new EventPostCommentSource());
	}

}
