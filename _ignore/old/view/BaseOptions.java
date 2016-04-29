package com.threecore.project.view;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;

public class BaseOptions extends Options {

	private static final long serialVersionUID = -8703637480188586507L;
	
	public static final String DESCRIPTION_PARAMETERS = "YAML configuration file (absolute path).";
	public static final String DESCRIPTION_HELP = "Project helper.";
	public static final String DESCRIPTION_VERSION = "Project version.";
	
	private static BaseOptions instance;
	
	public static BaseOptions getInstance() {
		if (instance == null)
			instance = new BaseOptions();
		return instance;
	}
	
	private BaseOptions() {
		Option configuration = this.optConfiguration();
		Option help = this.optHelp();
		Option version = this.optVersion();

		OptionGroup optGroup = new OptionGroup();
		optGroup.addOption(configuration);
		optGroup.addOption(help);
		optGroup.addOption(version);

		super.addOptionGroup(optGroup);
	}
	
	@SuppressWarnings("static-access")
	private Option optConfiguration() {
		return OptionBuilder
				.withLongOpt("configuration")
				.withDescription(DESCRIPTION_PARAMETERS)
				.isRequired(false)
				.hasArg(true)
				.withArgName("CONFIG-FILE")
				.create('c');
	}
	
	@SuppressWarnings("static-access")
	private Option optHelp() {
		return OptionBuilder
				.withLongOpt("help")
				.withDescription(DESCRIPTION_HELP)
				.isRequired(false)
				.hasArg(false)
				.create('h');
	}

	@SuppressWarnings("static-access")
	private Option optVersion() {
		return OptionBuilder
				.withLongOpt("version")
				.withDescription(DESCRIPTION_VERSION)
				.isRequired(false)
				.hasArg(false)
				.create('v');
	}

}
