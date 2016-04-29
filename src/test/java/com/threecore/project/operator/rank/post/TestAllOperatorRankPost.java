package com.threecore.project.operator.rank.post;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	TestPostRanker.class, 
	TestPostRankMerger.class, 
	TestPostRankUpdateFilter.class 
})
public class TestAllOperatorRankPost {

}
