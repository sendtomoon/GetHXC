package com.sendtomoon.gethxc.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

	private static final String BASE_DIR = System.getProperty("user.dir") + "//src//main//resources//";

	/**
	 * 请求list header
	 * 
	 */
	public static Properties getHeader() {
		InputStream is;
		try {
			is = new FileInputStream(new File(BASE_DIR + "header.properties"));
			Properties properties = new Properties();
			properties.load(is);
			return properties;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 请求client header
	 * 
	 */
	public static Properties getClientHeader() {
		InputStream is;
		try {
			is = new FileInputStream(new File(BASE_DIR + "clientheader.properties"));
			Properties properties = new Properties();
			properties.load(is);
			return properties;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
