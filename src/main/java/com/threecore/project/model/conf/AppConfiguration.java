package com.threecore.project.model.conf;

import java.util.StringJoiner;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.threecore.project.model.type.Stringable;

public final class AppConfiguration implements Stringable {
	
	private String friendships;
	private String posts;
	private String comments;
	private String likes;
	private int k;
	private int d;
	
	private String outdir;
	
	private boolean autoWatermark;
	private long watermarkDelay;
	
	private long iterationTimeout;
	private long bufferTimeout;
	
	private int parallelism;
	
	private double memoryThreshold;
	
	private String redisHostname;
	private int redisPort;
	
	private int sinkBufferSize;
	
	private boolean debug;	
	
	public AppConfiguration() {
		this.friendships = null;
		this.posts = null;
		this.comments = null;
		this.likes = null;
		this.k = 3;
		this.d = 7200;
		this.outdir = null;
		this.autoWatermark = false;
		this.watermarkDelay = 1000;
		this.iterationTimeout = 30000;
		this.bufferTimeout = -1;
		this.parallelism = 16;
		this.memoryThreshold = 0.75;
		this.redisHostname = "localhost";
		this.redisPort = 6379;
		this.sinkBufferSize = 24576;
		this.debug = false;		
	}
	
	public String getFriendships() {
		return this.friendships;
	}
	
	public void setFriendships(String friendships) {
		this.friendships = friendships;
	}
	
	public String getPosts() {
		return this.posts;
	}
	
	public void setPosts(String posts) {
		this.posts = posts;
	}
	
	public String getComments() {
		return this.comments;
	}
	
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public String getLikes() {
		return this.likes;
	}
	
	public void setLikes(String likes) {
		this.likes = likes;
	}
	
	public int getK() {
		return this.k;
	}

	public void setK(int k) {
		this.k = k;
	}

	public int getD() {
		return this.d;
	}

	public void setD(int d) {
		this.d = d;
	}
	
	public String getOutdir() {
		return this.outdir;
	}

	public void setOutdir(String outdir) {
		this.outdir = outdir;
	}
	
	public long getWatermarkDelay() {
		return this.watermarkDelay;
	}
	
	public void setWatermarkDelay(long watermarkDelay) {
		this.watermarkDelay = watermarkDelay;
	}
	
	public boolean getAutoWatermark() {
		return this.autoWatermark;
	}
	
	public void setAutoWatermark(boolean autoWatermark) {
		this.autoWatermark = autoWatermark;
	}
	
	public long getIterationTimeout() {
		return this.iterationTimeout;
	}
	
	public void setIterationTimeout(long iterationTimeout) {
		this.iterationTimeout = iterationTimeout;
	}
	
	public long getBufferTimeout() {
		return this.bufferTimeout;
	}
	
	public void setBufferTimeout(long bufferTimeout) {
		this.bufferTimeout = bufferTimeout;
	}
	
	public int getParallelism() {
		return this.parallelism;
	}

	public void setParallelism(int parallelism) {
		this.parallelism = parallelism;
	}

	public double getMemoryThreshold() {
		return this.memoryThreshold;
	}

	public void setMemoryThreshold(double memoryThreshold) {
		this.memoryThreshold = memoryThreshold;
	}
	
	public String getRedisHostname() {
		return this.redisHostname;
	}
	
	public void setRedisHostname(final String redisHostname) {
		this.redisHostname = redisHostname;
	}
	
	public int getRedisPort() {
		return this.redisPort;
	}
	
	public void setRedisPort(final int redisPort) {
		this.redisPort = redisPort;
	}
	
	public int getSinkBufferSize() {
		return this.sinkBufferSize;
	}
	
	public void setSinkBufferSize(final int buffsize) {
		this.sinkBufferSize = buffsize;
	}

	public boolean getDebug() {
		return this.debug;
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	public String getSinkPath(final String jobname) {
		return String.format("%s/%s/out.txt", this.getOutdir(), jobname);
	}	
	
	public String getPerformancePath(final String jobname) {
		return String.format("%s/%s/performance.txt", this.getOutdir(), jobname);
	}
	
	public String getLogPath(final String jobname) {
		return String.format("%s/%s/log.txt", this.getOutdir(), jobname);
	}
	
	// DEBUG
	
	public String getSinkPath(final String jobname, final String out) {
		return String.format("%s/%s/%s.txt", this.getOutdir(), jobname, out);
	}	
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("friendships", this.getFriendships())
				.append("posts", this.getPosts())
				.append("comments", this.getComments())
				.append("likes", this.getLikes())
				.append("outdir", this.getOutdir())
				.append("autoWatermark", this.getAutoWatermark())
				.append("watermarkDelay", this.getWatermarkDelay())
				.append("iterationTimeout", this.getIterationTimeout())
				.append("bufferTimeout", this.getBufferTimeout())
				.append("parallelism", this.getParallelism())
				.append("memoryThreshold", this.getMemoryThreshold())
				.append("redisHostname", this.getRedisHostname())
				.append("redisPort", this.getRedisPort())
				.append("sinkBufferSize", this.getSinkBufferSize())
				.append("debug", this.getDebug())
				.toString();
	}

	@Override
	public String asString() {
		return new StringJoiner(" | ")
				.add("friendships: " + this.getFriendships())
				.add("posts: " + this.getPosts())
				.add("comments: " + this.getComments())
				.add("likes: " + this.getLikes())
				.add("outdir: " + this.getOutdir())
				.add("autoWatermark: " + this.getAutoWatermark())
				.add("watermarkDelay: " + this.getWatermarkDelay())
				.add("iterationTimeout: " + this.getIterationTimeout())
				.add("bufferTimeout: " + this.getBufferTimeout())
				.add("parallelism: " + this.getParallelism())
				.add("memoryThreshold: " + this.getMemoryThreshold())
				.add("redisHostname: " + this.getRedisHostname())
				.add("redisPort: " + this.getRedisPort())
				.add("sinkBufferSize: " + this.getSinkBufferSize())
				.add("debug: " + this.getDebug())
				.toString();
	}	

}