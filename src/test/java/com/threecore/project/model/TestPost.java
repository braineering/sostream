package com.threecore.project.model;

import static org.junit.Assert.*;

import org.apache.commons.lang3.SerializationUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import com.threecore.project.SimpleTest;

public class TestPost extends SimpleTest {
	
	private static final String POST_LINE = "2016-01-01T12:00:00.500+0000|101|1|PST-101|USR-1";
	private static final Post POST = new Post(new DateTime(2016, 1, 1, 12, 0, 0, 500, DateTimeZone.UTC), 101, 1, "PST-101", "USR-1");
	
	@Test
	public void fromLine() {
		Post post = Post.fromString(POST_LINE);
		if (DEBUG) System.out.println(post);
		if (DEBUG) System.out.println(post.asString());
		assertEquals(POST, post);
	}
	
	@Test
	public void asString() {
		if (DEBUG) System.out.println(POST);
		if (DEBUG) System.out.println(POST.asString());
		assertEquals(POST_LINE, POST.asString());
	}
	
	@Test
	public void serialization() {
		SerializationUtils.serialize(POST);
	}

}
