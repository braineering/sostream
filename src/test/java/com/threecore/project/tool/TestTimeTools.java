package com.threecore.project.tool;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import com.threecore.project.SimpleTest;

public class TestTimeTools extends SimpleTest {

	private static final String TS_LINE_OFFSET = "2016-01-01T12:00:00.765+0000";
	
	private static final DateTime TS_DT = new DateTime(2016, 1, 1, 12, 0, 0, 765, DateTimeZone.UTC);
	
	@Test
	public void parse() {
		long ts_long_from_string = JodaTimeTool.getMillisFromString(TS_LINE_OFFSET);
		long ts_long_from_datetime = JodaTimeTool.getMillisFromDateTime(TS_DT);
		DateTime ts_dt = JodaTimeTool.getDateTimeFromString(TS_LINE_OFFSET);
		
		if (DEBUG) System.out.println("TS_DT (Date crated): " + JodaTimeTool.getStringFromDateTime(TS_DT));
		if (DEBUG) System.out.println("ts_dt (Date parsed): " + JodaTimeTool.getStringFromDateTime(ts_dt));		
		if (DEBUG) System.out.println("ts_long_from_string (Long parsed): " + JodaTimeTool.getStringFromMillis(ts_long_from_string));
		if (DEBUG) System.out.println("ts_long_from_datetime (Long from DateTime): " + JodaTimeTool.getStringFromMillis(ts_long_from_datetime));
		
		assertTrue(ts_long_from_string == ts_long_from_datetime);
		assertTrue(ts_long_from_string == ts_dt.getMillis());
		
		assertEquals(TS_LINE_OFFSET, JodaTimeTool.getStringFromDateTime(TS_DT));
		assertEquals(TS_LINE_OFFSET, JodaTimeTool.getStringFromDateTime(ts_dt));
		assertEquals(TS_LINE_OFFSET, JodaTimeTool.getStringFromMillis(ts_long_from_string));
		assertEquals(TS_LINE_OFFSET, JodaTimeTool.getStringFromMillis(ts_long_from_datetime));
	}
	
}
