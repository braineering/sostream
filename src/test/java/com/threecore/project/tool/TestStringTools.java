package com.threecore.project.tool;

import static org.junit.Assert.*;

import org.junit.Test;

import com.threecore.project.SimpleTest;

public class TestStringTools extends SimpleTest {
	
	private static final String STRING_TYPE_A = "a|b|c";
	private static final String STRING_TYPE_B = "a|b|c|";
	private static final String STRING_TYPE_C = "a|b|c||e";
	private static final String STRING_TYPE_D = "a|b|c|d|e";
	
	private static final String TUPLE_TYPE_A[] = {"a", "b", "c"};
	private static final String TUPLE_TYPE_B[] = {"a", "b", "c", ""};
	private static final String TUPLE_TYPE_C[] = {"a", "b", "c", "", "e"};
	private static final String TUPLE_TYPE_D[] = {"a", "b", "c", "d", "e"};

	@Test
	public void test() {
		final String tupleA[] = STRING_TYPE_A.split("[|]", -1);
		for (int i = 0; i < tupleA.length; i++)
			assertEquals(TUPLE_TYPE_A[i], tupleA[i]);
		
		final String tupleB[] = STRING_TYPE_B.split("[|]", -1);
		for (int i = 0; i < tupleB.length; i++)
			assertEquals(TUPLE_TYPE_B[i], tupleB[i]);
		
		final String tupleC[] = STRING_TYPE_C.split("[|]", -1);
		for (int i = 0; i < tupleC.length; i++)
			assertEquals(TUPLE_TYPE_C[i], tupleC[i]);
		
		final String tupleD[] = STRING_TYPE_D.split("[|]", -1);
		for (int i = 0; i < tupleD.length; i++)
			assertEquals(TUPLE_TYPE_D[i], tupleD[i]);
	}

}
