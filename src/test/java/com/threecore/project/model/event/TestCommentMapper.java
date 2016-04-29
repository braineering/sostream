package com.threecore.project.model.event;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.threecore.project.SimpleTest;
import com.threecore.project.control.EventPostCommentStreamgen;
import com.threecore.project.model.EventPostComment;
import com.threecore.project.model.event.CommentMap;
import com.threecore.project.model.event.StandardCommentMap;

public class TestCommentMapper extends SimpleTest {
	
	@Test
	public void standardCommentMapper() {
		CommentMap mapper = new StandardCommentMap();
		
		List<EventPostComment> events = EventPostCommentStreamgen.getDefault();
		
		for (EventPostComment event : events) {
			if (event.isPost()) {
				mapper.addPost(event.getPost());
			} else if (event.isComment()) {
				long postCommentedId = mapper.addComment(event.getComment());
				event.getComment().setPostCommentedId(postCommentedId);
			}
		}
		
		assertTrue(events.get(1).getComment().getPostCommentedId() == 1L);
		assertTrue(events.get(4).getComment().getPostCommentedId() == 2L);
		assertTrue(events.get(5).getComment().getPostCommentedId() == 2L);
		assertTrue(events.get(6).getComment().getPostCommentedId() == 2L);
		assertTrue(events.get(8).getComment().getPostCommentedId() == 4L);
	}
	
}
