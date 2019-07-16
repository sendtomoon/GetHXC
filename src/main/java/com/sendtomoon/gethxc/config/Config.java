package com.sendtomoon.gethxc.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

	static Properties properties = null;

	private static final String BASE_DIR = System.getProperty("user.dir") + "//src//main//resources//";

	public static void init() {
		try {
			InputStream is = new FileInputStream(new File(BASE_DIR + "config.properties"));
			properties = new Properties();
			properties.load(is);
			properties.setProperty("user.dir", System.getProperty("user.dir"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取value
	 * 
	 * @param key
	 * @return
	 */
	public static String value(String key) {
		if (null == properties) {
			init();
		}
		return properties.getProperty(key);
	}

	/**
	 * 请求list header
	 * 
	 * @return
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
	 * @return
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

	/**
	 * 请求 login header
	 * 
	 * @return
	 */
	public static Properties getLoginHeader() {
		InputStream is;
		try {
			is = new FileInputStream(new File(BASE_DIR + "loginheader.properties"));
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
