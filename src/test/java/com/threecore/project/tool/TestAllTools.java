package com.threecore.project.tool;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	TestStringTools.class,
	TestTimeTools.class
})
public class TestAllTools {

}
