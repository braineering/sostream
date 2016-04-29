package com.threecore.project.control;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.flink.api.java.utils.ParameterTool;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import com.threecore.project.SimpleTest;

public class TestParameter extends SimpleTest {
	
	private static final String SAMPLE_RD_CONFIG = "src/test/resources/sample-rd-config.properties";	
	private static final String SAMPLE_WR_CONFIG = "src/test/resources/sample-wr-config.properties";
	
	private static final String SAMPLE_RD_CONFIG_YAML = "src/test/resources/sample-rd-config.yaml";	
	private static final String SAMPLE_WR_CONFIG_YAML = "src/test/resources/sample-wr-config.yaml";
	@SuppressWarnings("unchecked")
	@Test
	public void yaml() throws IOException {
		Yaml yaml = new Yaml();
		FileReader fileRD1 = new FileReader(SAMPLE_RD_CONFIG_YAML);
		Map<String, Object> configRD = (Map<String, Object>) yaml.load(fileRD1);
		
		System.out.println(configRD.toString());
        
        assertEquals(1, configRD.getOrDefault("prop-1", 0));
        assertEquals(1.5, configRD.getOrDefault("prop-2", 1.0));
        assertEquals("Hello World!", configRD.getOrDefault("prop-3", "Default Hello"));        
        
        FileWriter fileWR1 = new FileWriter(SAMPLE_WR_CONFIG_YAML);
        yaml.dump(configRD, fileWR1); 
        
        FileReader fileRD2 = new FileReader(SAMPLE_WR_CONFIG_YAML);        
        Map<String, Object> configWR = (Map<String, Object>) yaml.load(fileRD2);
        
        System.out.println(configWR.toString());
        
        assertEquals(configRD.getOrDefault("prop-1", 0), configWR.getOrDefault("prop-1", 0));
        assertEquals(configRD.getOrDefault("prop-2", 1.0), configWR.getOrDefault("prop-2", 1.0));
        assertEquals(configRD.getOrDefault("prop-3", "Default Hello"), configWR.getOrDefault("prop-3", "Default Hello"));
	}
	
	@Test
	public void readParameters() {
		ParameterTool config = null;
		
		try {
			config = ParameterTool.fromPropertiesFile(SAMPLE_RD_CONFIG);
		} catch (IOException exc) {
			exc.printStackTrace();
		}
		
		int prop1 = Integer.valueOf(config.get("prop-1"));
		double prop2 = Double.valueOf(config.get("prop-2"));
		String prop3 = config.get("prop-3");	
		String prop4 = config.get("prop-4", "default");
		String prop5 = config.get("prop-5");
		
		assertEquals(prop1, 1);
		assertEquals(prop2, 1.5, 0.0);
		assertEquals(prop3, "Hello World!");
		assertEquals(prop4, "default");
		assertEquals(prop5, null);
	}
	
	@Test
	public void readProperties() {
		Properties config = new Properties();
		File file = new File(SAMPLE_RD_CONFIG);
		InputStream input = null;

		try {
			input = new FileInputStream(file);
			config.load(input);
		} catch (IOException exc) {
			exc.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException exc) {
					exc.printStackTrace();
				}
			}
		}
		
		int prop1 = Integer.valueOf(config.getProperty("prop-1"));
		double prop2 = Double.valueOf(config.getProperty("prop-2"));
		String prop3 = config.getProperty("prop-3");	
		String prop4 = config.getProperty("prop-4", "default");
		String prop5 = config.getProperty("prop-5");
		
		assertEquals(prop1, 1);
		assertEquals(prop2, 1.5, 0.0);
		assertEquals(prop3, "Hello World!");
		assertEquals(prop4, "default");
		assertEquals(prop5, null);
	}

	@Test
	public void writeProperties() {
		Properties config = new Properties();
		
		config.setProperty("prop-1", "value-1");
		config.setProperty("prop-2", "value-2");
		config.setProperty("prop-3", "value-3");
		
		File file = new File(SAMPLE_WR_CONFIG);
		
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException exc) {
				exc.printStackTrace();
			}
		}
			
		OutputStream out = null;
		
		try {
			out = new FileOutputStream(file, false);
			config.store(out, "Comments");
		} catch (IOException exc) {
			exc.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException exc) {
					exc.printStackTrace();
				}
			}
		}
		
	}
	
	

}
