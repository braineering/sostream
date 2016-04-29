package com.threecore.project.operator.score.post;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	TestPostScoreAggregator.class, 
	TestPostScoreMapperUpdater.class, 
	TestPostScoreMapperUpdaterTotal.class,
	TestPostScoreSplitter.class, 
	TestPostScoreUpdater.class, 
	TestPostScoreUpdaterTotal.class 
})
public class TestAllOperatorScorePost {

}
