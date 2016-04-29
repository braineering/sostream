package com.threecore.project.tool;

public final class StringTools {
	
	public static String[] getWordsFromSentence(final String sentence) {
		String[] words = sentence.split("\\s+");
		
		for (int i = 0; i < words.length; i++)
		    words[i] = words[i].replaceAll("[^\\w]", "");		
		
		return words;
	}

}
