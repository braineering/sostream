package com.threecore.project.draft.marco;

import org.apache.flink.api.java.tuple.Tuple3;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Friendship extends Tuple3<Long, Long, Long>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3599750201211027828L;
	
	public Friendship(){
		//
	}
	
	public Friendship(Long t0, Long t1, Long t2){
		this.f0 = t0;
		this.f1 = t1;
		this.f2 = t2;
	}
	
	public Long getTimestamp(){
		return this.f0;
	}
	
	public Long getUserId1(){
		return this.f1;
	}
	
	public Long getUserId2(){
		return this.f2;
	}
	
	public static Friendship fromLine(String line){
		String[] fields = line.split("[|]");
		DateTimeFormatter formatter = 
				DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		
		DateTime ts = DateTime.parse(fields[0], formatter);
		
		return new Friendship(ts.getMillis(), Long.parseLong(fields[1]), Long.parseLong(fields[2]));
	}
	
}
