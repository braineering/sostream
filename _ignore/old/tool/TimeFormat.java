package com.threecore.project.tool;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Deprecated
public class TimeFormat {
	
	public static final String TS_FORMAT_OFFSET = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	public static final String TS_FORMAT_SIMPLE = "yyyy-MM-dd HH:mm:ss";
	
	public static final long DAY_MILLIS = (1000 * 60 * 60 * 24);
	
	// QUICK method to parse
	
	public static long getLongFromOffsetDateTimeString(final String str) {
		OffsetDateTime odt = OffsetDateTime.parse(str, DateTimeFormatter.ofPattern(TS_FORMAT_OFFSET));
		LocalDateTime ldt = odt.toLocalDateTime();
		long ts = ldt.toInstant(ZoneOffset.UTC).toEpochMilli();
		return ts;
	}
	
	// String <==> (Offset/Local)DateTime
	
	public static OffsetDateTime parseOffsetDateTime(final String str) {
		return OffsetDateTime.parse(str, DateTimeFormatter.ofPattern(TS_FORMAT_OFFSET));
	}
	
	public static LocalDateTime parseLocalDateTimeWithOffset(final String str) {
		return LocalDateTime.parse(str, DateTimeFormatter.ofPattern(TS_FORMAT_OFFSET));
	}
	
	public static LocalDateTime parseLocalDateTimeWithoutOffset(final String str) {
		return LocalDateTime.parse(str, DateTimeFormatter.ofPattern(TS_FORMAT_SIMPLE));
	}
	
	public static String stringOffsetDateTime(final OffsetDateTime ts) {
		return ts.format(DateTimeFormatter.ofPattern(TS_FORMAT_OFFSET));
	}
	
	public static String stringLocalDateTimeWithOffset(final LocalDateTime ts) {
		return ts.format(DateTimeFormatter.ofPattern(TS_FORMAT_OFFSET));
	}	
	
	public static String stringLocalDateTimeWithoutOffset(final LocalDateTime ts) {
		return ts.format(DateTimeFormatter.ofPattern(TS_FORMAT_SIMPLE));
	}	
	
	// String <==> long
	
	public static long parseLongWithOffset(final String str) {
		return fromOffsetDateTime(parseOffsetDateTime(str));
	}
	
	public static long parseLongWithoutOffset(final String str) {
		return fromLocalDateTime(parseLocalDateTimeWithoutOffset(str));
	}	
	
	public static String stringWithOffset(final long ts) {
		return stringOffsetDateTime(toOffsetDateTime(ts));
	}
	
	public static String stringWithoutOffset(final long ts) {
		return stringLocalDateTimeWithoutOffset(toLocalDateTime(ts));
	}	
	
	// (Offset/Local)DateTime <==> long 
	
	public static long fromOffsetDateTime(final OffsetDateTime ts) {
		return fromLocalDateTime(ts.toLocalDateTime());
	}
	
	public static long fromLocalDateTime(final LocalDateTime ts) {
		return ts.toInstant(ZoneOffset.UTC).toEpochMilli();
	}
	
	public static OffsetDateTime toOffsetDateTime(final long ts) {
		return OffsetDateTime.ofInstant(Instant.ofEpochMilli(ts), ZoneOffset.UTC);
	}
	
	public static LocalDateTime toLocalDateTime(final long ts) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(ts), ZoneOffset.UTC);
	}	
	
	// Time operations
	
	public static long daysPassed(final LocalDateTime tsFrom, final LocalDateTime tsTo) {
		return tsFrom.until(tsTo, ChronoUnit.DAYS);
	}
	
	public static long daysPassed(final long tsFrom, final long tsTo) {
		return daysPassed(toLocalDateTime(tsFrom), toLocalDateTime(tsTo));
	}
	
	public static boolean isAfter(final long ts1, final long ts2) {
		return ts1 > ts2;
	}
	
	public static boolean isBefore(final long ts1, final long ts2) {
		return ts1 < ts2;
	}
	
}