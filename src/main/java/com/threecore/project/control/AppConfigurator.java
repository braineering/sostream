package com.threecore.project.control;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.yaml.snakeyaml.Yaml;

import com.threecore.project.model.conf.AppConfiguration;
import com.threecore.project.tool.BaseOptions;

public class AppConfigurator {
	
	public static BaseOptions OPTS = BaseOptions.getInstance();
	
	public static AppConfiguration loadConfiguration(final String[] argv) {
		CommandLine cmd = getCommandLine(argv);
		AppConfiguration config = null;
		
		if (cmd.hasOption("help")) {
			AppController.printHelp(OPTS);
			System.exit(0);
		} else if (cmd.hasOption("version")) {
			AppController.printVersion();
			System.exit(0);
		}
		
		if (cmd.hasOption("configuration")) {			
			String configPath = cmd.getOptionValue("configuration");
			config = loadYAMLConfiguration(configPath);
		} else {
			config = new AppConfiguration();	
		}		
		
		if (cmd.hasOption("parallelism")) {			
			int parallelism = Integer.valueOf(cmd.getOptionValue("parallelism"));
			config.setParallelism(parallelism);
		}
		
		if (cmd.hasOption("debug")) {
			config.setDebug(true);
		}
		
		String args[] = cmd.getArgs();
		
		String FRNDS = args[0];
		String POSTS = args[1];
		String CMNTS = args[2];
		String LIKES = args[3];
		int K = Integer.valueOf(args[4]);
		int D = Integer.valueOf(args[5]);
		String OUTDIR = args[6];
		
		config.setFriendships(FRNDS);
		config.setPosts(POSTS);
		config.setComments(CMNTS);
		config.setLikes(LIKES);
		config.setK(K);
		config.setD(D);
		config.setOutdir(OUTDIR);		
		
		if (config.getDebug()) {
			System.out.println("[DEBS]> CONFIG: FRNDS: " + config.getFriendships());
			System.out.println("[DEBS]> CONFIG: POSTS: " + config.getPosts());
			System.out.println("[DEBS]> CONFIG: CMNTS: " + config.getComments());
			System.out.println("[DEBS]> CONFIG: LIKES: " + config.getLikes());
			System.out.println("[DEBS]> CONFIG: K: " + config.getK());
			System.out.println("[DEBS]> CONFIG: D: " + config.getD());
			System.out.println("[DEBS]> CONFIG: OUTDIR: " + config.getOutdir());		
			System.out.println("[DEBS]> CONFIG: APPCNF: " + config.asString());
		}
		
		return config;
	}	
	
	private static CommandLine getCommandLine(String argv[]) {
		CommandLineParser cmdParser = new DefaultParser();	
		CommandLine cmd = null;
		
		try {
			cmd = cmdParser.parse(OPTS, argv);
		} catch (ParseException exc) {
			System.err.println("[DEBS]> ERROR: " + exc.getMessage());
			AppController.printUsage();
		}
		
		return cmd;
	}
	
	private static AppConfiguration loadYAMLConfiguration(final String path) {
		AppConfiguration config = null;
		Yaml yaml = new Yaml();
		FileReader file;
		try {
			file = new FileReader(path);
		} catch (FileNotFoundException exc) {
			System.err.println(exc.getMessage());
			config = null;
			return null;
		}
		
		config = yaml.loadAs(file, AppConfiguration.class);
		
		if (config == null)
			config = new AppConfiguration();
		
		return config;
	}
}
