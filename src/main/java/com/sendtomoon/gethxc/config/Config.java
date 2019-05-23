package com.sendtomoon.gethxc.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

	static Properties properties = null;

	public static void init() {
		try {
			InputStream is = new FileInputStream(
					new File(System.getProperty("user.dir") + "//src//main//resources//config.properties"));
			properties = new Properties();
			properties.load(is);
			is = new FileInputStream(
					new File(System.getProperty("user.dir") + "//src//main//resources//mongodb.properties"));
			properties.load(is);
			properties.setProperty("user.dir", System.getProperty("user.dir"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡvalue
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
	 * �������ļ���ȡͷ��Ϣ
	 * 
	 * @return
	 */
	public static Properties getHeader() {
		InputStream is;
		try {
			is = new FileInputStream(new File(Config.value("user.dir") + "//src//main//resources//header.properties"));
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
