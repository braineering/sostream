package com.threecore.project.control;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.yaml.snakeyaml.Yaml;

import com.threecore.project.model.conf.AppConfiguration;
import com.threecore.project.view.BaseOptions;

public class AppControl {
	
	public static final String APP_NAME = "ACM DEBS 2016 SOLUTION";
	public static final String APP_VERSION = "1.0.0";
	public static final String APP_DESCRIPTION = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
												 "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n";
	
	public static AppConfiguration getAppConfiguration(final String[] args) {
		CommandLineParser cmdParser = new GnuParser();
		CommandLine cmd = null;
		BaseOptions opts = BaseOptions.getInstance();
		AppConfiguration config = null;
		Yaml yaml = new Yaml();
		
		try {
			cmd = cmdParser.parse(opts, args);
		} catch (ParseException exc) {
			System.err.println("Parsing error: " + exc.getMessage());
			AppControl.printUsage();
			return null;
		}				
		
		if (cmd.hasOption("configuration")) {			
			String parametersPath = cmd.getOptionValue("configuration");
			FileReader file;
			try {
				file = new FileReader(parametersPath);
			} catch (FileNotFoundException exc) {
				System.err.println(exc.getMessage());
				config = null;
				return null;
			}
			config = yaml.loadAs(file, AppConfiguration.class);
		} else if (cmd.hasOption("help")) {
			AppControl.printHelp(opts);
			System.exit(0);
		} else if (cmd.hasOption("version")) {
			AppControl.printVersion();
			System.exit(0);
		}	
		
		if (config == null)
			config = new AppConfiguration();
		
		return config;
	}
	
	public static final void printUsage() {
		System.out.format("%s version %s\n", APP_NAME, APP_VERSION);
		System.out.format("%s\n", APP_DESCRIPTION);
		System.out.format("Usage: %s [FRIENDSHIPS] [POSTS] [COMMENTS] [LIKES] [K] [D] [QEURY1_OUT] [QUERY2_OUT] [PERFMC_OUT] [LOGGER_OUT] [options,...]\n", APP_NAME);
	}
	
	public static final void printHelp(final Options opts) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(APP_NAME, opts, true);
	}
	
	public static final void printVersion() {
		System.out.format("%s version %s\n", APP_NAME, APP_VERSION);
	}
	
	public static final void quit() {
		System.exit(0);
	}
	
}
