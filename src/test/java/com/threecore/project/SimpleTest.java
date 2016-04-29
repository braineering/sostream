package com.threecore.project;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

public class SimpleTest {
	
	@Rule 
	public TestName name = new TestName();
	
	@Before
	public void testInfo() {
		System.out.println("\n/********************************************************************************");
		System.out.println(" * TEST: " + this.getClass().getSimpleName() + " " + name.getMethodName());
		System.out.println(" ********************************************************************************/\n");
	}
	
	public static final boolean DEBUG = false;
	
	public static final String POSTS_XLARGE = "dataset/dist/xlarge/posts.dat";
	public static final String COMMENTS_XLARGE = "dataset/dist/xlarge/comments.dat";
	public static final String FRIENDSHIPS_XLARGE = "dataset/dist/xlarge/friendships.dat";
	public static final String LIKES_XLARGE = "dataset/dist/xlarge/likes.dat";
	
	public static final String POSTS_LARGE = "dataset/dist/large/posts.dat";
	public static final String COMMENTS_LARGE = "dataset/dist/large/comments.dat";
	public static final String FRIENDSHIPS_LARGE = "dataset/dist/large/friendships.dat";
	public static final String LIKES_LARGE = "dataset/dist/large/likes.dat";
	
	public static final String POSTS_MEDIUM = "dataset/dist/medium/posts.dat";
	public static final String COMMENTS_MEDIUM = "dataset/dist/medium/comments.dat";
	public static final String FRIENDSHIPS_MEDIUM = "dataset/dist/medium/friendships.dat";
	public static final String LIKES_MEDIUM = "dataset/dist/medium/likes.dat";
	
	public static final String POSTS_SMALL = "dataset/dist/small/posts.dat";
	public static final String COMMENTS_SMALL = "dataset/dist/small/comments.dat";
	public static final String FRIENDSHIPS_SMALL = "dataset/dist/small/friendships.dat";
	public static final String LIKES_SMALL = "dataset/dist/small/likes.dat";
	
	public static final String POSTS_XSMALL = "dataset/dist/xsmall/posts.dat";
	public static final String COMMENTS_XSMALL = "dataset/dist/xsmall/comments.dat";
	public static final String FRIENDSHIPS_XSMALL = "dataset/dist/xsmall/friendships.dat";
	public static final String LIKES_XSMALL = "dataset/dist/xsmall/likes.dat";
	
	public static final String POSTS_TINY = "dataset/dist/tiny/posts.dat";
	public static final String COMMENTS_TINY = "dataset/dist/tiny/comments.dat";
	public static final String FRIENDSHIPS_TINY = "dataset/dist/tiny/friendships.dat";
	public static final String LIKES_TINY = "dataset/dist/tiny/likes.dat";
	
	public static final String POSTS_SAMPLE = "dataset/test/sample/posts.dat";
	public static final String COMMENTS_SAMPLE = "dataset/test/sample/comments.dat";
	public static final String FRIENDSHIPS_SAMPLE = "dataset/test/sample/friendships.dat";
	public static final String LIKES_SAMPLE = "dataset/test/sample/likes.dat";

}
