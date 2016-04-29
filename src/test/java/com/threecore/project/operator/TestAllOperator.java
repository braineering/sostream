package com.threecore.project.operator;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.threecore.project.operator.event.TestAllOperatorEvent;
import com.threecore.project.operator.filter.TestAllOperatorFilter;
import com.threecore.project.operator.key.TestAllOperatorKey;
import com.threecore.project.operator.rank.TestAllOperatorRank;
import com.threecore.project.operator.score.TestAllOperatorScore;
import com.threecore.project.operator.sink.TestAllOperatorSink;
import com.threecore.project.operator.source.TestAllOperatorSource;
import com.threecore.project.operator.time.TestAllOperatorTime;

@RunWith(Suite.class)
@SuiteClasses({ 
	TestAllOperatorEvent.class,
	TestAllOperatorFilter.class,
	TestAllOperatorKey.class,
	TestAllOperatorRank.class,
	TestAllOperatorScore.class,
	TestAllOperatorSink.class,
	TestAllOperatorSource.class,
	TestAllOperatorTime.class
})
public class TestAllOperator {

}
