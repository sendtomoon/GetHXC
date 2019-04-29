package com.sendtommon.gethxc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections4.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.sendtommon.gethxc.dto.GetListByTagReqDTO;
import com.sendtommon.gethxc.dto.GetListByTagRespDTO;
import com.sendtommon.gethxc.dto.OrdertextDTO;

public class Controller {
	static {
		try {
			InputStream is = new FileInputStream(
					new File(System.getProperty("user.dir") + "//src//main//resources//config.properties"));
			Properties properties = new Properties();
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

	public void mainService() {
		for (int i = 0; i <= 9999; i += 20) {
			GetListByTagReqDTO glbt = new GetListByTagReqDTO(1, 20, null, i, new OrdertextDTO("AddTime", "desc"));
			String str = null;
			try {
				str = HttpUtils.post(System.getProperty("getListByTag"), JSON.toJSONString(glbt), null,
						this.getHeader());
			} catch (Exception e) {
				e.printStackTrace();
			}
			GetListByTagRespDTO glbr = JSON.parseObject(str, GetListByTagRespDTO.class);
			if (CollectionUtils.isNotEmpty(glbr.getData())) {

			}
		}
	}

	private Properties loadProp(String fileName) {
		InputStream is;
		try {
			is = new FileInputStream(new File(System.getProperty("user.dir") + "//src//main//resources//" + fileName));
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

	@SuppressWarnings("unchecked")
	private Map<String, String> getHeader() {
		Properties properties = this.loadProp("header.properties");
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (Entry<Object, Object> entry : properties.entrySet()) {
			map.put((String) entry.getKey(), (String) entry.getValue());
		}
		return map;
	}
}
