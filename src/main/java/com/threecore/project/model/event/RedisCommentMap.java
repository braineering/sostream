package com.threecore.project.model.event;

import com.threecore.project.model.Comment;
import com.threecore.project.model.Post;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisCommentMap implements CommentMap {

	private static final long serialVersionUID = 1L;
	
	private JedisPool jedisPool;
	private Jedis jedis;
	
	public RedisCommentMap() {}
	
	public void connect(final String hostname, final int port) {
		this.jedisPool = new JedisPool(hostname, port);
		this.jedis = this.jedisPool.getResource();
	}
	
	public void disconnect() {
		this.jedis.close();
		this.jedisPool.close();
	}	
	
	@Override
	public void addPost(final Post post) {		
		this.jedis.lpush(String.valueOf(post.getPostId()), post.getUser());
	}
	
	public void addPost(final long postId, final String user) {
		this.jedis.lpush(String.valueOf(postId), user);
	}

	@Override
	public void addPost(final long postId) {
		this.jedis.lpush(String.valueOf(postId), "-1");
	}

	@Override
	public void removePost(final long postId) {		
		this.jedis.del(String.valueOf(postId));
	}
	
	@Override
	public long addComment(final Comment comment) {
		if (comment.isReply()) {
			return this.addCommentToComment(comment.getCommentId(), comment.getCommentRepliedId());
		} else {
			return this.addCommentToPost(comment.getCommentId(), comment.getPostCommentedId());
		}	
	}

	@Override
	public long addCommentToPost(final long commentId, final long postId) {
		long res = this.jedis.rpushx(String.valueOf(postId), "-1");
		if (res == 1) {
			return postId;
		} else {
			return -1;
		}
	}

	@Override
	@Deprecated
	public long addCommentToComment(final long commentId, final long commentedId) {	
		/*for (long pId : this.jedis.k) {
			List<String> l = this.jedis.lrange(String.valueOf(pId), 0, -1);
			if (l.contains(String.valueOf(commentedId))) {
				this.jedis.rpushx(String.valueOf(pId), String.valueOf(commentId));
				return pId;
			}
		}*/
		return -1;
	}

	@Override
	@Deprecated
	public long getPostCommented(final long commentId) {
		/*for (long pId : this.map.keySet()) {
			List<Long> l = this.map.get(pId);
			if (l.contains(commentId)) {
				return pId;
			}
		}*/
		return -1;
	}
	
	@Override
	public String toString() {
		return super.toString();
	}

}
