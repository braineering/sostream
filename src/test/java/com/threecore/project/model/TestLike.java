package com.threecore.project.model;

import static org.junit.Assert.*;

import org.apache.commons.lang3.SerializationUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import com.threecore.project.SimpleTest;

public class TestLike extends SimpleTest {
	
	private static final String LIKE_LINE = "2016-01-01T12:00:00.500+0000|1|201";
	private static final Like LIKE = new Like(new DateTime(2016, 1, 1, 12, 0, 0, 500, DateTimeZone.UTC), 1, 201);
	
	@Test
	public void fromLine() {
		Like like = Like.fromString(LIKE_LINE);
		if (DEBUG) System.out.println(like);
		if (DEBUG) System.out.println(like.asString());
		assertEquals(like, LIKE);
	}
	
	@Test
	public void asString() {
		if (DEBUG) System.out.println(LIKE);
		if (DEBUG) System.out.println(LIKE.asString());
		assertEquals(LIKE_LINE, LIKE.asString());
	}
	
	@Test
	public void serialization() {
		SerializationUtils.serialize(LIKE);
	}

}
