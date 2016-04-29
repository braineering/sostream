package com.threecore.project.operator.rank.comment;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	TestCommentRanker.class, 
	TestCommentRankMerger.class, 
	TestCommentRankUpdateFilter.class 
})
public class TestAllOperatorRankComment {

}
