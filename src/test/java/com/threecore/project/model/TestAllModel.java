package com.threecore.project.model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.threecore.project.model.event.TestAllModelEvent;
import com.threecore.project.model.rank.TestAllModelRank;
import com.threecore.project.model.rank.TestCommentRanking;
import com.threecore.project.model.rank.TestPostRanking;

@RunWith(Suite.class)
@SuiteClasses({ 
	TestPost.class,
	TestComment.class, 
	TestLike.class,
	TestFriendship.class,
	TestPostRank.class,
	TestPostScore.class,
	TestPostRanking.class,
	TestCommentRank.class,
	TestCommentScore.class,
	TestCommentRanking.class,
	TestAllModelEvent.class,
	TestAllModelRank.class
})
public class TestAllModel {

}
