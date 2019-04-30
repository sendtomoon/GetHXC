package com.sendtommon.gethxc.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Map.Entry;

public class Config {

	static Properties properties = null;

	public static void init() {
		try {
			InputStream is = new FileInputStream(
					new File(System.getProperty("user.dir") + "//src//main//resources//config.properties"));
			properties = new Properties();
			properties.load(is);
			for (Entry<Object, Object> entry : properties.entrySet()) {
				System.setProperty((String) entry.getKey(), (String) entry.getValue());
			}
			is = new FileInputStream(
					new File(System.getProperty("user.dir") + "//src//main//resources//mongodb.properties"));
			properties = new Properties();
			properties.load(is);
			for (Entry<Object, Object> entry : properties.entrySet()) {
				System.setProperty((String) entry.getKey(), (String) entry.getValue());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String value(String key) {
		if (null == properties) {
			init();
		}
		return properties.getProperty(key);
	}

}
