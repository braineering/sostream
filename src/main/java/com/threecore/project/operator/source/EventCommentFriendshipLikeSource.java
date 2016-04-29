package com.threecore.project.operator.source;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.flink.api.common.accumulators.LongCounter;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.threecore.project.model.Comment;
import com.threecore.project.model.EventCommentFriendshipLike;
import com.threecore.project.model.Friendship;
import com.threecore.project.model.Like;

public class EventCommentFriendshipLikeSource extends RichSourceFunction<EventCommentFriendshipLike> {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EventCommentFriendshipLikeSource.class);

	public static final String DEFAULT_COMMENTS = "comments.dat";
	public static final String DEFAULT_FRIENDSHIPS = "friednships.dat";
	public static final String DEFAULT_LIKES = "likes.dat";
	public static final long DEFAULT_WATERMARK_DELAY = 1000;
	
	private String commentPath;
	private String friendshipPath;
	private String likePath;
	
	private long watermarkDelay;
	private long nextWatermark;
	
	private transient BufferedReader commentReader;
	private transient BufferedReader friendshipReader;
	private transient BufferedReader likeReader;
	
	private long ts[];
	
	private LongCounter tuples = new LongCounter();
	
	public EventCommentFriendshipLikeSource(final String commentPath, final String friendshipPath, final String likePath, final long watermarkDelay) {
		this.commentPath = commentPath;
		this.friendshipPath = friendshipPath;
		this.likePath = likePath;
		this.watermarkDelay = watermarkDelay;
		this.nextWatermark = 0;
		
		this.ts = new long[3];
	}
	
	public EventCommentFriendshipLikeSource(final String commentPath, final String friendshipPath, final String likePath) {
		this(commentPath, friendshipPath, likePath, DEFAULT_WATERMARK_DELAY);
	}
	
	public EventCommentFriendshipLikeSource() {
		this(DEFAULT_COMMENTS, DEFAULT_FRIENDSHIPS, DEFAULT_LIKES, DEFAULT_WATERMARK_DELAY);
	}

	@Override
	public void run(SourceContext<EventCommentFriendshipLike> ctx) throws Exception {
		super.getRuntimeContext().addAccumulator("tuples", this.tuples);
		
		FileInputStream commentFile = new FileInputStream(this.commentPath);
		FileInputStream friendshipFile = new FileInputStream(this.friendshipPath);
		FileInputStream likeFile = new FileInputStream(this.likePath);
		
		InputStreamReader commentInput = new InputStreamReader(commentFile);
		InputStreamReader friendshipInput = new InputStreamReader(friendshipFile);
		InputStreamReader likeInput = new InputStreamReader(likeFile);
		
		this.commentReader = new BufferedReader(commentInput);
		this.friendshipReader = new BufferedReader(friendshipInput);
		this.likeReader = new BufferedReader(likeInput);
		
		String commentLine = null;
		String friendshipLine = null;
		String likeLine = null;
		
		Comment comment = null;
		Friendship friendship = null;
		Like like = null;
		EventCommentFriendshipLike event = null;
		
		int nextEvent;
		this.ts[0] = Long.MAX_VALUE;
		this.ts[1] = Long.MAX_VALUE;
		this.ts[2] = Long.MAX_VALUE;		
		
		while (true) {			
			if (this.commentReader.ready()) {
				if (comment == null) {
					commentLine = this.commentReader.readLine();
					if (commentLine != null) {
						comment = Comment.fromString(commentLine);
						this.ts[0] = comment.getTimestamp();
					} else {
						this.ts[0] = Long.MAX_VALUE;
					}
				}
			}
			
			if (this.friendshipReader.ready()) {
				if (friendship == null) {
					friendshipLine = this.friendshipReader.readLine();
					if (friendshipLine != null) {
						friendship = Friendship.fromString(friendshipLine);
						this.ts[1] = friendship.getTimestamp();
					} else {
						this.ts[1] = Long.MAX_VALUE;
					}
				}
			}
			
			if (this.likeReader.ready()) {
				if (like == null) {
					likeLine = this.likeReader.readLine();
					if (likeLine != null) {
						like = Like.fromString(likeLine);
						this.ts[2] = like.getTimestamp();
					} else {
						this.ts[2] = Long.MAX_VALUE;
					}
				}
			}
			
			if (comment == null && friendship == null && like == null) {
				break;
			}
			
			nextEvent = this.getNextEvent();
			
			if (nextEvent == 0) {
				event = new EventCommentFriendshipLike(comment);
				comment = null;
				this.ts[0] = Long.MAX_VALUE;	
			} else if (nextEvent == 1) {
				event = new EventCommentFriendshipLike(friendship);
				friendship = null;
				this.ts[1] = Long.MAX_VALUE;
			} else if (nextEvent == 2) {
				event = new EventCommentFriendshipLike(like);
				like = null;
				this.ts[2] = Long.MAX_VALUE;
			}
			
			assert (event != null) : "event must be != null";
			assert (event.getFriendship() != null || event.getComment() != null || event.getLike() != null) : "at least one field must be != null";
			assert (event.getTimestamp() > 0 && event.getTimestamp() < Long.MAX_VALUE) : "timestamp must be > 0 and < MAX_VALUE";
			
			this.emitEvent(ctx, event);	
		}
		
		this.emitEOF(ctx);
		
		this.commentReader.close();
		this.friendshipReader.close();
		this.likeReader.close();
	}

	@Override
	public void cancel() {		
		if (this.commentReader != null) {
			try {
				this.commentReader.close();
			} catch (IOException exc) {
				exc.printStackTrace();
			} finally {
				this.commentReader = null;
			}
		}
		
		if (this.friendshipReader != null) {
			try {
				this.friendshipReader.close();
			} catch (IOException exc) {
				exc.printStackTrace();
			} finally {
				this.friendshipReader = null;
			}
		}
		
		if (this.likeReader != null) {
			try {
				this.likeReader.close();
			} catch (IOException exc) {
				exc.printStackTrace();
			} finally {
				this.likeReader = null;
			}
		}
	}
	
	private int getNextEvent() {
		if (this.ts[0] <= this.ts[1] && this.ts[0] <= this.ts[2]) {
			return 0;
		} else if (this.ts[1] <= this.ts[2] && this.ts[1] <= this.ts[0]) {
			return 1;
		} else {
			return 2;
		}
	}
	
	private void emitEvent(SourceContext<EventCommentFriendshipLike> ctx, final EventCommentFriendshipLike event) {
		LOGGER.debug("NEXT EVENT: " + event);
		this.nextWatermark = event.getTimestamp() + this.watermarkDelay;		
		ctx.collectWithTimestamp(event, event.getTimestamp());
		ctx.emitWatermark(new Watermark(this.nextWatermark));
		this.tuples.add(1L);
	}
	
	private void emitEOF(SourceContext<EventCommentFriendshipLike> ctx) {		
		EventCommentFriendshipLike event = EventCommentFriendshipLike.EOF;
		LOGGER.debug("NEXT EVENT: " + event);
		ctx.collect(event);
		this.tuples.add(1L);
	}

}
