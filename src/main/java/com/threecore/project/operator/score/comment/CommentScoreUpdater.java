package com.threecore.project.operator.score.comment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.co.RichCoFlatMapFunction;
import org.apache.flink.util.Collector;

import com.threecore.project.model.Comment;
import com.threecore.project.model.CommentScore;
import com.threecore.project.model.EventCommentLike;
import com.threecore.project.model.Friendship;
import com.threecore.project.model.Like;
import com.threecore.project.model.score.comment.CliqueUtil;
import com.threecore.project.tool.SetTools;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class CommentScoreUpdater extends RichCoFlatMapFunction<EventCommentLike, Friendship, CommentScore> {

	private static final long serialVersionUID = 1L;
	
	public static final int DEFAULT_WNDSIZE = 60;

	private LinkedList<Comment> window;
	private Map<Long, Comment> commentMap;
	private Map<Long, CliqueUtil> cliqueMap;
	private int d;
	
	private JedisPool pool;
	private Jedis jedis;
	
	public CommentScoreUpdater(final int d) {
		super();
		this.window = new LinkedList<Comment>();
		this.commentMap = new HashMap<Long, Comment>();
		this.cliqueMap = new HashMap<Long, CliqueUtil>();
		this.d = d;
	}
	
	public CommentScoreUpdater() {		
		super();
		this.window = new LinkedList<Comment>();
		this.commentMap = new HashMap<Long, Comment>();
		this.cliqueMap = new HashMap<Long, CliqueUtil>();
		this.d = DEFAULT_WNDSIZE;
	}
	
	@Override
	public void open(Configuration config) throws Exception {
		String hostname = config.getString("redisHostname", "localhost");
		int port = config.getInteger("redisPort", 6379);
		this.pool = new JedisPool(hostname, port);
		this.jedis = pool.getResource();		
	}

	@Override
	public void close() throws Exception {
		this.jedis.close();
		this.pool.close();
	}
	
	@Override
	public void flatMap1(EventCommentLike event, Collector<CommentScore> out) throws Exception {		
		if (event.isComment()) {		
			Comment comment = event.getComment();
			
			this.window.addFirst(comment);
			this.commentMap.put(comment.getCommentId(), comment);
			this.cliqueMap.put(comment.getCommentId(), new CliqueUtil(comment.getTimestamp()));
			checkComments(event.getComment().getTimestamp(), out);		
		} else if (event.isLike()) {		
			Like like = event.getLike();
			if (!this.jedis.exists(String.valueOf(like.getUserId()))) {
				this.jedis.rpush(String.valueOf(like.getUserId()), "NULL");
			}			
			if (this.cliqueMap.containsKey(like.getCommentId())) {
				this.cliqueMap.get(like.getCommentId()).addLike(like);
				this.cliqueMap.get(like.getCommentId()).updateLastEventTs(like.getTimestamp());
				checkComments(like.getTimestamp(), out);
			}			
		}		
	}

	@Override
	public void flatMap2(Friendship friendship, Collector<CommentScore> out) throws Exception {
		updateComments(friendship);
		checkComments(friendship.getTimestamp(), out);
	}
	
	private void updateComments(Friendship friendship) {
		Iterator<Long> iterComment = this.cliqueMap.keySet().iterator();
		
		long key;
		
		while(iterComment.hasNext()){
			key = iterComment.next();
			this.cliqueMap.get(key).updateLastEventTs(friendship.getTimestamp());
		}
		
	}
	
	private void checkComments(final long ts, Collector<CommentScore> out) {		
		Iterator<Comment> iterWindow = this.window.iterator();
		
		Comment tmp;
		Integer index = 0;
		LinkedList<Comment> tmpWindow = null;
		LinkedList<Comment> removeWindow = null;
		
		while(iterWindow.hasNext()){
			tmp = iterWindow.next();
			
			if(!this.cliqueMap.get(tmp.getCommentId()).needsRecheck()){
				continue;
			}
			
			if(tmp.getTimestamp() < (ts - (d*1000))){
				
				tmpWindow = subList(this.window, 0, index);
				removeWindow = subList(this.window, index, this.window.size());
				
				break;
			}
			else{
				processCliques(tmp, ts, out);
			}
			
			this.cliqueMap.get(tmp.getCommentId()).updateLastUpdateTs(ts);
			index++;
		}
		
		
		if(tmpWindow != null){
			this.window = tmpWindow;
			if(!removeWindow.isEmpty()){
				remove(removeWindow);
			}
		}
		
	}
	
	private <T> LinkedList<T> subList(LinkedList<T> list, final int start, final int end){
		LinkedList<T> tmp = new LinkedList<T>();
		
		for(int i=start; i<end; i++){
			tmp.add(list.poll());
		}
		
		return tmp;
	}

	private void processCliques(Comment tmp, final long ts, Collector<CommentScore> out) {
		HashSet<Set<String>> maxCliques = new HashSet<Set<String>>();
		
		HashSet<String> R = new HashSet<String>();
		HashSet<String> P = new HashSet<String>();
		HashSet<String> X = new HashSet<String>();
		
		HashSet<String> likes = this.cliqueMap.get(tmp.getCommentId()).getUserLikes();
		
		
		long maxRange = 0L;
		long tmpRange;
		
		for(String s : likes){
			P.addAll(SetTools.SetOnObjUnion(getNeighbors(s), s));
			
			BronKerbosch(R, P, X, maxCliques);
			
			maxCliques = filter(maxCliques, likes);
			
			tmpRange = findMax(maxCliques);
			
			if(tmpRange > maxRange){
				maxRange = tmpRange;
			}

		}
		
		out.collect(new CommentScore(ts, tmp.getCommentId(), tmp.getComment(), maxRange));
	}

	private HashSet<String> getNeighbors(String s) { 
		HashSet<String> set =  new HashSet<String>(this.jedis.lrange(s, 0, -1));
		if(set.contains("NULL")){
			set.remove("NULL");
		}
		return set;		
	}

	private HashSet<Set<String>> filter(HashSet<Set<String>> maxCliques, HashSet<String> likes) {		
		HashSet<Set<String>> tmpCliques = new HashSet<Set<String>>();
		Set<String> tmpSet;
		
		for(Set<String> s : maxCliques){
			tmpSet = SetTools.intersection(s, likes);
			if(!tmpSet.isEmpty()){
				tmpCliques.add(tmpSet);
	
			}
		}
		
		return tmpCliques;		
	}

	private Long findMax(HashSet<Set<String>> maxCliques) {		
		Iterator<Set<String>> iterCliques = maxCliques.iterator();
		
		Long max = 0L;
		Long tmp;
		
		while(iterCliques.hasNext()){
			tmp = (long) iterCliques.next().size();
			
			if(tmp > max){
				max = tmp;
			}
		}
		
		return max;
	}

	private void remove(LinkedList<Comment> removeWindow) {
		Iterator<Comment> iterRemove = removeWindow.iterator();
		Comment tmp;
		
		while(iterRemove.hasNext()){
			tmp = iterRemove.next();
			
			this.commentMap.remove(tmp.getCommentId());
			this.cliqueMap.remove(tmp.getCommentId());
			
		}
	}

	public void BronKerbosch(
			Set<String> R,
			Set<String> P,
			Set<String> X, // This could be useless, it needs to be checked
			Set<Set<String>> maxCliques
			){
		
		if(P.isEmpty() && X.isEmpty()){
			maxCliques.add(R);
		}
		else{
			for(String s : P){
				BronKerbosch(SetTools.SetOnObjUnion(R, s),
						SetTools.intersection(P, getNeighbors(s)),
						SetTools.intersection(X, getNeighbors(s)),
						maxCliques);
				
				P = SetTools.SetOnObjDiff(P, s);
				X.add(s);
			
			}
		}		
	}	
	
}
