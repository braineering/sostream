package com.threecore.project.analysis.performance.source;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.joda.time.DateTime;

import com.threecore.project.model.Comment;
import com.threecore.project.model.EventPostComment;
import com.threecore.project.model.ModelCommons;
import com.threecore.project.model.Post;
import com.threecore.project.tool.JodaTimeTool;

public class PostCommentSource2 {
	
	private static long ts[] = new long[2];
	
	public PostCommentSource2() {}
	
	public long read(final String posts, final String comments) throws IOException {
		
		FileInputStream postFile = new FileInputStream(posts);
		FileInputStream commentFile = new FileInputStream(comments);
		
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
		
		long events = 0;
		
		while (true) {
			if (postReader.ready()) {
				if (!postOutbox) {
					postLine = postReader.readLine();
					if (postLine != null) {
						final String array[] = postLine.split("[|]", -1);

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
						
						final String array[] = commentLine.split("[|]", -1);
						
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
			
			if (!postOutbox && !commentOutbox) {
				break;
			}
			
			if (ts[0] <= ts[1]) {
				nextEvent = 0;
			} else {
				nextEvent = 1;
			}
			
			if (nextEvent == 0) {
				event.f0 = post;
				event.f1 = Comment.UNDEFINED_COMMENT;
				event.f2 = EventPostComment.TYPE_POST;
				postOutbox = false;
				ts[0] = Long.MAX_VALUE;
			} else if (nextEvent == 1) {
				event.f0 = Post.UNDEFINED_POST;
				event.f1 = comment;
				event.f2 = EventPostComment.TYPE_COMMENT;
				commentOutbox = false;
				ts[1] = Long.MAX_VALUE;
			}	
			
			doSomethingWithEvent(event);
			
			events++;
			
			if (events % 100000 == 0)
				System.out.print(".");
			if (events % 8000000 == 0)
				System.out.print("\n");
		}
		
		postReader.close();		
		commentReader.close();	
		
		return events;
	}
	
	private void doSomethingWithEvent(final EventPostComment event) {
		//
	}

}
