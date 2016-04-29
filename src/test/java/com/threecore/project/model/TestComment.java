package com.threecore.project.model;

import static org.junit.Assert.*;

import org.apache.commons.lang3.SerializationUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import com.threecore.project.SimpleTest;

public class TestComment extends SimpleTest {
	
	private static final String COMMENT_LINE = "2016-01-01T12:00:00.500+0000|201|1|CMT-201|USR-1||101";
	private static final String REPLY_LINE = "2016-01-01T12:00:00.500+0000|202|1|CMT-202|USR-1|201|";
	private static final Comment COMMENT = new Comment(new DateTime(2016, 1, 1, 12, 0, 0, 500, DateTimeZone.UTC), 201, 1, "CMT-201", "USR-1", ModelCommons.UNDEFINED_LONG, 101);
	private static final Comment REPLY = new Comment(new DateTime(2016, 1, 1, 12, 0, 0, 500, DateTimeZone.UTC), 202, 1, "CMT-202", "USR-1", 201, ModelCommons.UNDEFINED_LONG);
	
	@Test
	public void fromLineComment() {
		Comment comment = Comment.fromString(COMMENT_LINE);
		if (DEBUG) System.out.println(comment);
		if (DEBUG) System.out.println(comment.asString());
		assertEquals(COMMENT, comment);
	}
	
	@Test
	public void fromLineReply() {
		Comment comment = Comment.fromString(REPLY_LINE);
		if (DEBUG) System.out.println(comment);
		if (DEBUG) System.out.println(comment.asString());
		assertEquals(REPLY, comment);
	}
	
	@Test
	public void asStringComment() {
		if (DEBUG) System.out.println(COMMENT);
		if (DEBUG) System.out.println(COMMENT.asString());
		assertEquals(COMMENT_LINE, COMMENT.asString());
	}
	
	@Test
	public void asStringReply() {
		if (DEBUG) System.out.println(REPLY);
		if (DEBUG) System.out.println(REPLY.asString());
		assertEquals(REPLY_LINE, REPLY.asString());
	}
	
	@Test
	public void serialization() {
		SerializationUtils.serialize(COMMENT);
	}

}
