package com.threecore.project.operator.score;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.threecore.project.operator.score.comment.TestAllOperatorScoreComment;
import com.threecore.project.operator.score.post.TestAllOperatorScorePost;

@RunWith(Suite.class)
@SuiteClasses({ 
	TestAllOperatorScoreComment.class,
	TestAllOperatorScorePost.class
})
public class TestAllOperatorScore {

}
