package com.kuaima.utils.common;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

@Component
public class SystemConfig {
	private static Properties props;
	private static final Logger LOGGER = Logger.getLogger(SystemConfig.class);

	private SystemConfig() {

		try {
			Resource resource = new ClassPathResource("/application.properties");//
			props = PropertiesLoaderUtils.loadProperties(resource);
		} catch (IOException e) {
			LOGGER.error("IOException" + e);
		}
	}

	public static String getProperty(String key) {

		return props == null ? null : props.getProperty(key);

	}

	public static String getProperty(String key, String defaultValue) {

		return props == null ? null : props.getProperty(key, defaultValue);

	}

	public static Properties getProperties() {
		return props;
	}
}
