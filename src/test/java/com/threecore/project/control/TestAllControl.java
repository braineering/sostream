package com.threecore.project.control;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	TestAppConfiguration.class,
	TestPerformanceWriter.class
})
public class TestAllControl {

}
