package com.threecore.project.control;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import com.threecore.project.SimpleTest;
import com.threecore.project.model.conf.AppConfiguration;

public class TestAppConfiguration extends SimpleTest {

	private static final String SAMPLE_RD_CONFIG_YAML = "src/test/resources/config/app-conf-sample.yaml";	
	
	@Test
	public void readYAML() throws IOException {		
		Yaml yaml = new Yaml();		
		FileReader file = new FileReader(SAMPLE_RD_CONFIG_YAML);
		AppConfiguration config = yaml.loadAs(file, AppConfiguration.class);
		assertEquals(20000, config.getSinkBufferSize());
        assertEquals(true, config.getDebug());
	}

}
