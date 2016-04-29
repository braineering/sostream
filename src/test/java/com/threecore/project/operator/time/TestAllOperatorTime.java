package com.threecore.project.operator.time;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	TestAscendingTimestamper.class,
	TestEventPostCommentTimestamper.class,
	TestTimestamper.class	
})
public class TestAllOperatorTime {

}
