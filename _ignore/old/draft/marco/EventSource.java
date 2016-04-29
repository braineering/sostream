package com.threecore.project.draft.marco;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
public class EventSource implements SourceFunction<Tuple4<Comment, Friendship, Like, Integer>>{

	private static final long serialVersionUID = -62683653252342L;
	
	public static final String commentPath = "data/comments.dat";
	public static final String friendPath = "data/friendships.dat";
	public static final String likePath = "data/likes.dat";
	
	public static final String commentEnd = "2037-12-31T23:59:59.999+0000|-1|-1|(nil)|(nil)||-1";
	public static final String friendEnd = "2037-12-31T23:59:59.999+0000|-1|-1";
	public static final String likeEnd = "2037-12-31T23:59:59.999+0000|-1|-1";
	
	public static final Long timeEnd = Comment.fromLine(commentEnd).f0;

	private transient BufferedReader commentReader;
	private transient BufferedReader friendReader;
	private transient BufferedReader likeReader;
	
	public volatile Long[] eventsTime;

	public EventSource(){
		
		eventsTime = new Long[3];
	}

	@Override
	public void cancel() {
		//
	}

	@Override
	public void run(
			SourceFunction.SourceContext<Tuple4<Comment, Friendship, Like, Integer>> ctx)
					throws Exception {
		
		
		String newComment;
		String newFriend;
		String newLike;
		Integer nextEvent;
		
		Comment comment;
		Friendship friend;
		Like like;
		
		Comment emptyComment = Comment.fromLine(commentEnd);
		Friendship emptyFriend = Friendship.fromLine(friendEnd);
		Like emptyLike = Like.fromLine(likeEnd);
		
		try {
			commentReader = new BufferedReader(new FileReader(commentPath));
			friendReader = new BufferedReader(new FileReader(friendPath));
			likeReader = new BufferedReader(new FileReader(likePath));
		
		
			while(!likeReader.ready() ||
					!commentReader.ready() ||
					!friendReader.ready()){
				Thread.sleep(100);
			}
			
			newComment = commentReader.readLine();
			newFriend = friendReader.readLine();
			newLike = likeReader.readLine();
			
			if(newComment == null){
				comment = Comment.fromLine(commentEnd);
			}
			else{
				comment = Comment.fromLine(newComment);
			}
			
			if(newFriend == null){
				friend = Friendship.fromLine(friendEnd);
			}
			else{
				friend = Friendship.fromLine(newFriend);
			}
			
			if(newLike == null){
				like = Like.fromLine(likeEnd);
			}
			else{
				like = Like.fromLine(newLike);
			}
			
			eventsTime[0] = comment.getTimestamp();
			eventsTime[1] = friend.getTimestamp();
			eventsTime[2] = like.getTimestamp();
			
			while(((eventsTime[0] < timeEnd) || 
					(eventsTime[1] < timeEnd) ||
					(eventsTime[2]) < timeEnd)){
				
				nextEvent = getNextEvent();
				if(nextEvent == 0){
					
					ctx.collect(new Tuple4<>(comment, emptyFriend, emptyLike, 0));
			
					newComment = commentReader.readLine();
					if(newComment != null){
						comment = Comment.fromLine(newComment);
					}
					else{
						comment = Comment.fromLine(commentEnd);
					}
					
					eventsTime[0] = comment.getTimestamp();
				}
				
				else if(nextEvent == 1){
					
					ctx.collectWithTimestamp(new Tuple4<>(emptyComment, friend, emptyLike, 1), friend.f0);
					
					ctx.emitWatermark(new Watermark(friend.f0 + 500L));
					
					newFriend = friendReader.readLine();
					if(newFriend != null){
						friend = Friendship.fromLine(newFriend);
						
					}
					else{
						friend = Friendship.fromLine(friendEnd);
					}
					
					eventsTime[1] = friend.getTimestamp();
				}
				
				else{
					
					ctx.collect(new Tuple4<>(emptyComment, emptyFriend, like, 2));
					
					newLike = likeReader.readLine();
					if(newLike != null){
						like = Like.fromLine(newLike);
						
					}
					else{
						like = Like.fromLine(likeEnd);
					}
					
					eventsTime[2] = like.getTimestamp();
				}
				
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Integer getNextEvent(){
		if(eventsTime[0] < eventsTime[1] && eventsTime[0] < eventsTime[2]){
			return 0;
		}
		else if(eventsTime[1] < eventsTime[2]){
			return 1;
		}
		else{
			return 2;
		}
	}
	

}
