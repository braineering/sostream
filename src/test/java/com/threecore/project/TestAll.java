package com.threecore.project;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.threecore.project.control.TestAllControl;
import com.threecore.project.model.TestAllModel;
import com.threecore.project.operator.TestAllOperator;
import com.threecore.project.tool.TestAllTools;

@RunWith(Suite.class)
@SuiteClasses({ 
	TestAllControl.class,
	TestAllModel.class,
	TestAllOperator.class,
	TestAllTools.class
})
public class TestAll {

}
