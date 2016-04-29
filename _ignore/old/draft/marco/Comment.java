package com.threecore.project.draft.marco;

import java.util.HashSet;

import org.apache.flink.api.java.tuple.Tuple7;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

//Tuple7<Timestamp, CommentId, UserId, Comment, User, CommentReplied, PostCommented>
public class Comment extends Tuple7<Long, Long, Long, String, String, Long, Long>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4538958512279968945L;
	
	public HashSet<Like> likes;
	
	public Comment(){
		//
	}
	
	public Comment(Long t0, Long t1, Long t2, String t3,
			String t4, Long t5, Long t6){
		
		this.f0 = t0; 
		this.f1 = t1;
		this.f2 = t2;
		this.f3 = t3;
		this.f4 = t4;
		this.f5 = t5;
		this.f6 = t6;
		this.likes = new HashSet<Like>();
	}
	
	public Long getTimestamp(){
		return this.f0;
	}
	
	public Long getCommentId(){
		return this.f1;
	}

	public Long getUserId(){
		return this.f2;
	}
	
	public String getComment(){
		return this.f3;
	}
	
	public String getUser(){
		return this.f4;
	}
	
	public Long getCommentReplied(){
		return this.f5;
	}
	
	public Long getPostCommented(){
		return this.f6;
	}
	
	public void setPostCommented(Long postId){
		this.f6 = postId;
	}
	
	public void addLike(Like like){
		this.likes.add(like);
	}
	
	public HashSet<Long> getUserLikes(){
		HashSet<Long> userLikes = new HashSet<Long>();
		
		for(Like l : likes){
			userLikes.add(l.getUserId());
		}
		
		return userLikes;
	}
	

	public static Comment fromLine(String line){
		String[] fields = line.split("[|]");
		DateTimeFormatter formatter = 
				DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		
		DateTime ts = DateTime.parse(fields[0], formatter);
		if(fields.length < 7 || fields[6].replace(" ",  "").replace("\n",  "").equals("")){
			return new Comment(ts.getMillis(), Long.parseLong(fields[1]), Long.parseLong(fields[2]),
				fields[3], fields[4], Long.parseLong(fields[5]), -1L);
		}
		else{
			return new Comment(ts.getMillis(), Long.parseLong(fields[1]), Long.parseLong(fields[2]),
					fields[3], fields[4], -1L, Long.parseLong(fields[6]));
		}
	}
	
}
