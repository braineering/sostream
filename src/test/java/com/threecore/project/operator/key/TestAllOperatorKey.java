package com.threecore.project.operator.key;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	TestCommentScoreKeyer.class, 
	TestEventCommentFriendshipLikeKeyer.class, 
	TestEventCommentLikeKeyer.class,
	TestEventPostCommentKeyer.class, 
	TestPostScoreKeyer.class,
	TestPostScoreIdExtractor.class,
	TestPostScoreIdExtractorBucket.class
})
public class TestAllOperatorKey {

}
