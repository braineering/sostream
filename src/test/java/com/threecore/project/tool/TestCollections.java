package com.threecore.project.tool;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import com.threecore.project.model.ModelCommons;
import com.threecore.project.model.PostScore;
import com.threecore.project.model.score.post.comparator.PostScoreComparatorDesc;

public class TestCollections {

	@Test
	public void test() {
		List<PostScore> list_1 = new ArrayList<PostScore>(3);
		for (int i = 0 ; i < 3; i++) list_1.add(null);
		System.out.println("list_1: " + list_1.size());	
		list_1.set(1, new PostScore());
		System.out.println("list_1: " +  list_1);
	}
	
	@Test
	public void test2() {
		List<PostScore> list = new ArrayList<PostScore>();
		PostScore score = new PostScore(10, 10, 10, 10, "user", 10);	
		insertScore(list, score);
		score.setScore(1000);
		System.out.println(score.asString());
		System.out.println(list.get(0).asString());
	}
	
	private void insertScore(List<PostScore> list, PostScore score) {
		list.add(score.copy());
	}
	
	@Test
	public void testStream() {
		List<PostScore> scores = new ArrayList<PostScore>();
		for (long i = 0; i < 100; i++) {
			PostScore score = new PostScore(10L, i, 1L, 1L, "user-1", i, 0L, i);
			scores.add(score);
		}
		
		List<PostScore> ranking = scores.stream().sorted(PostScoreComparatorDesc.getInstance()).limit(3).collect(Collectors.toList());
		
		for (PostScore score : ranking) {
			System.out.println(score.asString());
		}
	}

}
