package com.threecore.project.operator.event;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;

import com.threecore.project.model.EventCommentFriendshipLike;
import com.threecore.project.model.Friendship;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class FriendshipOperator extends RichMapFunction<EventCommentFriendshipLike, Friendship> {
	
	private static final long serialVersionUID = 1L;

	private JedisPool pool;
	private Jedis jedis;
	
	@Override
	public Friendship map(EventCommentFriendshipLike event) throws Exception {
		long user1 = event.getFriendship().getUser1Id();
		long user2 = event.getFriendship().getUser2Id();
		
		this.jedis.rpush(String.valueOf(user1), String.valueOf(user2));
		this.jedis.rpush(String.valueOf(user2), String.valueOf(user1));
		
		return event.getFriendship();
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

}
