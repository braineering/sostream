package com.threecore.project.operator.source;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.threecore.project.SimpleTest;
import com.threecore.project.model.Comment;
import com.threecore.project.model.EventPostComment;
import com.threecore.project.model.Post;
import com.threecore.project.model.time.ChronologicalComparator;

public class CommonsSource extends SimpleTest {
	
	public static List<EventPostComment> getSortedEvents(final String postsPath, final String commentsPath) throws IOException {
		List<EventPostComment> list = new ArrayList<EventPostComment>();
		
		FileInputStream postFile = new FileInputStream(postsPath);
		FileInputStream commentFile = new FileInputStream(commentsPath);
		
		InputStreamReader postInput = new InputStreamReader(postFile);
		InputStreamReader commentInput = new InputStreamReader(commentFile);
		
		BufferedReader postReader = new BufferedReader(postInput);
		BufferedReader commentReader = new BufferedReader(commentInput);
		
		String postLine = null;
		String commentLine = null;
		
		Post post = null;
		Comment comment = null;
		EventPostComment event = null;
		
		while (true) {
			if (postReader.ready()) {
				if (post == null) {
					postLine = postReader.readLine();
					if (DEBUG) System.out.println(postLine);
					if (postLine != null) {
						post = Post.fromString(postLine);
						if (DEBUG) System.out.println("==P==> " + post.asString());
					}
				}
			}
			
			if (commentReader.ready()) {
				if (comment == null) {
					commentLine = commentReader.readLine();
					if (DEBUG) System.out.println(commentLine);
					if (commentLine != null) {
						comment = Comment.fromString(commentLine);
						if (DEBUG) System.out.println("==C==> " + comment.asString());
					} 
				}
			}
			
			if (post == null && comment == null) {
				break;
			}
			
			if (post != null) {
				event = new EventPostComment(post);
				list.add(event);
				post = null;
			}
			
			if (comment != null) {
				event = new EventPostComment(comment);
				list.add(event);
				comment = null;
			}
		}
		
		postReader.close();		
		commentReader.close();	
		
		Collections.sort(list, new ChronologicalComparator<EventPostComment>());
		
		return list;
	}

}
