package com.threecore.project.draft.marco;

import org.apache.flink.api.java.tuple.Tuple3;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Like extends Tuple3<Long, Long, Long>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5512387212069307511L;

	public Like(){
		//
	}

	public Like(Long t0, Long t1, Long t2){
		this.f0 = t0;
		this.f1 = t1;
		this.f2 = t2;
	}
	
	public Long getTimestamp(){
		return this.f0;
	}
	
	public Long getUserId(){
		return this.f1;
	}
	
	public Long getCommentId(){
		return this.f2;
	}
	
	public static Like fromLine(String line){
		String[] fields = line.split("[|]");
		DateTimeFormatter formatter = 
				DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		
		DateTime ts = DateTime.parse(fields[0], formatter);
		
		return new Like(ts.getMillis(), Long.parseLong(fields[1]), Long.parseLong(fields[2]));
	}
}
