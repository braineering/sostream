package com.threecore.project.operator.source;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;
import org.joda.time.DateTime;
import org.junit.Test;

import com.threecore.project.SimpleTest;
import com.threecore.project.model.Comment;
import com.threecore.project.model.EventPostComment;
import com.threecore.project.model.ModelCommons;
import com.threecore.project.model.Post;
import com.threecore.project.tool.JodaTimeTool;

public class TestEventPostCommentSource2 extends SimpleTest {
	
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
		
		Post post = new Post();
		Comment comment = new Comment();
		EventPostComment event = new EventPostComment();
		
		int nextEvent;
		ts[0] = Long.MAX_VALUE;
		ts[1] = Long.MAX_VALUE;
		
		boolean postOutbox = false;
		boolean commentOutbox = false;
		
		while (true) {
			if (postReader.ready()) {
				if (!postOutbox) {
					postLine = postReader.readLine();
					if (postLine != null) {
						final String array[] = postLine.split("[|]", -1);

						//final long timestamp = JodaTimeTool.getMillisFromString(array[0]);
						final long timestamp = DateTime.parse(array[0], JodaTimeTool.FORMATTER).getMillis();
						final long post_id = Long.parseLong(array[1]);
						final long user_id = Long.parseLong(array[2]);
						final String content = array[3];
						final String user = array[4];
						
						post.f0 = timestamp;
						post.f1 = post_id;
						post.f2 = user_id;
						post.f3 = content;
						post.f4 = user;
						
						ts[0] = timestamp;
						postOutbox = true;
					} else {
						ts[0] = Long.MAX_VALUE;
					}
				}
			}
			
			if (commentReader.ready()) {
				if (!commentOutbox) {
					commentLine = commentReader.readLine();
					if (commentLine != null) {
						//comment = Comment.fromString(commentLine);
						
						final String array[] = commentLine.split("[|]", -1);
						
						//final long timestamp = JodaTimeTool.getMillisFromString(array[0]);
						final long timestamp = DateTime.parse(array[0], JodaTimeTool.FORMATTER).getMillis();
						final long comment_id = Long.parseLong(array[1]);
						final long user_id = Long.parseLong(array[2]);
						final String content = array[3];
						final String user = array[4];
						
						final long comment_replied_id = (array[5].isEmpty()) ? ModelCommons.UNDEFINED_LONG : Long.parseLong(array[5]);
						final long post_commented_id = (array[6].isEmpty()) ? ModelCommons.UNDEFINED_LONG : Long.parseLong(array[6]);
						
						comment.f0 = timestamp;
						comment.f1 = comment_id;
						comment.f2 = user_id;
						comment.f3 = content;
						comment.f4 = user;
						comment.f5 = comment_replied_id;
						comment.f6 = post_commented_id;
						
						ts[1] = comment.f0;
						commentOutbox = true;
					} else {
						ts[1] = Long.MAX_VALUE;
					}
				}
			}
			
			/*if (post == null && comment == null) {
				break;
			}*/
			
			if (!postOutbox && !commentOutbox) {
				break;
			}
			
			if (ts[0] <= ts[1]) {
				nextEvent = 0;
			} else {
				nextEvent = 1;
			}
			
			if (nextEvent == 0) {
				//event = new EventPostComment(post);
				event.f0 = post;
				event.f1 = Comment.UNDEFINED_COMMENT;
				event.f2 = EventPostComment.TYPE_POST;
				//post = null;
				postOutbox = false;
				ts[0] = Long.MAX_VALUE;
			} else if (nextEvent == 1) {
				//event = new EventPostComment(comment);
				event.f0 = Post.UNDEFINED_POST;
				event.f1 = comment;
				event.f2 = EventPostComment.TYPE_COMMENT;
				//comment = null;
				commentOutbox = false;
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
			
			// System.out.println("EVENT: " + event.asString());
			// System.out.println("\t\tSHOULD BE: " + CORRECT_EVENT.asString());
			
			if (CORRECT_EVENT.getTimestamp() != event.getTimestamp()) {
				assertEquals(CORRECT_EVENT, event);
			}
		}
		
		postReader.close();		
		commentReader.close();	
	}
	
	@Test
	public void serialization() {
		SerializationUtils.serialize(new EventPostCommentSource());
	}

}
