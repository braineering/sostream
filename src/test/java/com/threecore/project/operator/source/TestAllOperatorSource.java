package com.threecore.project.operator.source;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.threecore.project.operator.source.TestEventCommentFriendshipLikeSource;
import com.threecore.project.operator.source.TestEventPostCommentSource;

@RunWith(Suite.class)
@SuiteClasses({ 
	TestEventPostCommentSource.class,
	TestEventCommentFriendshipLikeSource.class	
})
public class TestAllOperatorSource {

}
