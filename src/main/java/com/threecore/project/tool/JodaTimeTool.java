package com.threecore.project.tool;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class JodaTimeTool {
	
	public static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	
	public static final long DAY_MILLIS = (1000 * 60 * 60 * 24);
	
	// ok
	public static long getMillisFromString(final String str) {
		DateTime ts = DateTime.parse(str, FORMATTER);
		return ts.getMillis();
	}
	
	// ok
	public static long getMillisFromDateTime(final DateTime dt) {
		return dt.getMillis();
	}	
	
	public static DateTime getDateTimeFromString(final String str) {
		DateTime ts = FORMATTER.withOffsetParsed().parseDateTime(str);
		return ts;
	}
	
	// ok
	public static String getStringFromDateTime(final DateTime dt) {
		return dt.toString(FORMATTER);
	}
	
	// ok
	public static String getStringFromMillis(final long millis) {
		return JodaTimeTool.FORMATTER.withZoneUTC().print(millis);
	}
	
	
	
	
	
	public static DateTime getDateTimeFromMillis(final long millis) {
		DateTime ts = new DateTime(millis, DateTimeZone.UTC);
		return ts;
	}
	
	public static String getOffsetDateTimeStringFromMillis(final long millis) {
		DateTime ts = new DateTime(millis, DateTimeZone.UTC);
		return ts.toString();
	}
	
}
