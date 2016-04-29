package com.threecore.project.operator.event;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	TestActiveEventPostCommentMapper.class,
	TestEventCommentLikeExtractor.class,
	TestEventPostCommentMapper.class,
	TestFriendshipOperator.class,
	TestFriendshipSplitter.class
})
public class TestAllOperatorEvent {

}
