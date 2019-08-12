package com.sendtomoon.gethxc.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import com.sendtomoon.gethxc.config.Config;

public class HeaderUtils {
	public static Map<String, String> getListHeader() {
		Properties properties = Config.getHeader();
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (Entry<Object, Object> entry : properties.entrySet()) {
			map.put((String) entry.getKey(), (String) entry.getValue());
		}
		return map;
	}

	public static Map<String, String> getClientHeader() {
		Properties properties = Config.getClientHeader();
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (Entry<Object, Object> entry : properties.entrySet()) {
			map.put((String) entry.getKey(), (String) entry.getValue());
		}
		return map;
	}

}
