package com.threecore.project.model;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class ModelCommons {
	
	public static final long UNDEFINED_LONG = -1;
	
	public static final String UNDEFINED_STRING = "-";
	
	public static final DateTime UNDEFINED_LDT = new DateTime(UNDEFINED_LONG, DateTimeZone.UTC);
	
	public static final String DELIMITER_IN = "|";
	
	public static final String DELIMITER_OUT = ",";
	
	public static final long INITIAL_SCORE = 10;

}
