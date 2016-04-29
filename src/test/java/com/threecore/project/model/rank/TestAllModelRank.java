package com.threecore.project.model.rank;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	TestCommentRanking.class,
	TestPostRanking.class 
})
public class TestAllModelRank {

}
