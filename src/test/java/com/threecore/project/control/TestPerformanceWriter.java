package com.threecore.project.control;

import org.junit.Test;

import com.threecore.project.SimpleTest;

public class TestPerformanceWriter extends SimpleTest {
	
	private static final String PATH = "src/test/resources/performance/performance-sample.txt";	
	private static final double ELAPSED = 9123456789.123456789123456789;
	private static final double LATENCY = 8123456789.123456789123456789;

	@Test
	public void writeSequential() {
		PerformanceWriter.write(PATH, ELAPSED, LATENCY);	
	}

}
