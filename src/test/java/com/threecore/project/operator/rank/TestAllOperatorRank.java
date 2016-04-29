package com.threecore.project.operator.rank;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.threecore.project.operator.rank.comment.TestAllOperatorRankComment;
import com.threecore.project.operator.rank.post.TestAllOperatorRankPost;

@RunWith(Suite.class)
@SuiteClasses({ 
	TestAllOperatorRankComment.class,
	TestAllOperatorRankPost.class
})
public class TestAllOperatorRank {

}
