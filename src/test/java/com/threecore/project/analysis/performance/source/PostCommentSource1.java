package com.threecore.project.analysis.performance.source;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import com.threecore.project.model.Comment;
import com.threecore.project.model.EventPostComment;
import com.threecore.project.model.Post;

public class PostCommentSource1 {
	
	private static long ts[] = new long[2];
	
	public PostCommentSource1() {}
	
	public long read(final String posts, final String comments) throws IOException {
		
		FileInputStream postFile = new FileInputStream(posts);
		FileInputStream commentFile = new FileInputStream(comments);
		
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
		
		long events = 0;
		
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
	
	private int getNextEvent() {
		if (ts[0] <= ts[1]) {
			return 0;
		} else {
			return 1;
		}
	}
	
	private void doSomethingWithEvent(final EventPostComment event) {
		//
	}

}
