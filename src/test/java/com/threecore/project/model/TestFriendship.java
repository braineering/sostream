package com.threecore.project.model;

import static org.junit.Assert.*;

import org.apache.commons.lang3.SerializationUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import com.threecore.project.SimpleTest;

public class TestFriendship extends SimpleTest {
	
	private static final String FRIENDSHIP_LINE = "2016-01-01T12:00:00.500+0000|1|2";
	private static final Friendship FRIENDSHIP = new Friendship(new DateTime(2016, 1, 1, 12, 0, 0, 500, DateTimeZone.UTC), 1, 2);
	
	@Test
	public void fromLine() {
		Friendship friendship = Friendship.fromString(FRIENDSHIP_LINE);
		if (DEBUG) System.out.println(friendship);
		if (DEBUG) System.out.println(friendship.asString());
		assertEquals(friendship, FRIENDSHIP);
	}
	
	@Test
	public void asString() {
		if (DEBUG) System.out.println(FRIENDSHIP);
		if (DEBUG) System.out.println(FRIENDSHIP.asString());
		assertEquals(FRIENDSHIP_LINE, FRIENDSHIP.asString());
	}
	
	@Test
	public void serialization() {
		SerializationUtils.serialize(FRIENDSHIP);
	}

}
