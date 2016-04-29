package com.threecore.project.control;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

public class AppController {
	
	public static final String APP_NAME = "ACM DEBS 2016 PROJECT";
	public static final String TEAM_NAME = "ThreeCores Team";
	public static final String APP_VERSION = "1.0.0";
	public static final String APP_DESCRIPTION = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
												 "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n";
	
	public static final void printUsage() {
		System.out.format("%s version %s (by %s)\n", APP_NAME, APP_VERSION, TEAM_NAME);
		System.out.format("%s\n", APP_DESCRIPTION);
		System.out.format("Usage: %s [FRIENDSHIPS] [POSTS] [COMMENTS] [LIKES] [K] [D] [OUTPUT_DIR] [options,...]\n", APP_NAME);
	}
	
	public static final void printHelp(final Options opts) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(APP_NAME, opts, true);
	}
	
	public static final void printVersion() {
		System.out.format("%s version %s (by %s)\n", APP_NAME, APP_VERSION, TEAM_NAME);
	}
	
	public static final void quit() {
		System.exit(0);
	}
	
}
