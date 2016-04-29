package com.threecore.project.model.old;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import org.apache.flink.api.java.tuple.Tuple5;

public class Post extends Tuple5<DateTime, Long, Long, String, String>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6665797808869498680L;
	public Integer score;
	
	public Post(){
		//
	}

	public Post(DateTime t0, Long t1, Long t2, String t3, String t4){
		this.f0 = t0;
		this.f1 = t1;
		this.f2 = t2;
		this.f3 = t3;
		this.f4 = t4;
		this.score = 10;
	}
	
	public static Post fromLine(String line){
		String[] fields = line.split("[|]");
		DateTimeFormatter formatter = 
				DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		
		DateTime ts = DateTime.parse(fields[0], formatter);
		return new Post(ts, Long.parseLong(fields[1]), Long.parseLong(fields[2]),
				fields[3], fields[4]);
	}
	
	public DateTime getTimestamp(){
		return this.f0;
	}
	
	public Long getPostId(){
		return this.f1;
	}
	
	public Long getUserId(){
		return this.f2;
	}
	
	public String getPost(){
		return this.f3;
	}
	
	public String getUser(){
		return this.f4;
	}
	
	public int getScore(){
		return this.score;
	}
	
	public void setScore(int value){
		this.score = value;
	}
	
	public String toString(){
		return this.f0.getMillis() + "|" +
				this.f1 + "|" +
				this.f2 + "|" +
				this.f3 + "|" +
				this.f4 + "|" + 
				this.score;
	}
}
