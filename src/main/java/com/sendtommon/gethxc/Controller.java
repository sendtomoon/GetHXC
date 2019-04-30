package com.sendtommon.gethxc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections4.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.sendtommon.gethxc.config.Config;
import com.sendtommon.gethxc.dto.GetListByTagReqDTO;
import com.sendtommon.gethxc.dto.GetListByTagRespDTO;
import com.sendtommon.gethxc.dto.GetListByTagRespDataDTO;
import com.sendtommon.gethxc.dto.OrdertextDTO;
import com.sendtommon.gethxc.mongo.MongoUtils;

public class Controller {

	public void mainService() {
		GetListByTagReqDTO glbt = new GetListByTagReqDTO(1, 6000, null, 0, new OrdertextDTO("AddTime", "desc"));
		String str = null;
		try {
			str = HttpUtils.post(Config.value("getListByTag"), JSON.toJSONString(glbt), null, this.getHeader());
		} catch (Exception e) {
			e.printStackTrace();
		}
		GetListByTagRespDTO glbr = JSON.parseObject(str, GetListByTagRespDTO.class);
		if (CollectionUtils.isNotEmpty(glbr.getData())) {
			this.insertMongodb(glbr.getData());
		}
	}

	private Properties loadProp(String fileName) {
		InputStream is;
		try {
			is = new FileInputStream(new File(Config.value("user.dir") + "//src//main//resources//" + fileName));
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

	private Map<String, String> getHeader() {
		Properties properties = this.loadProp("header.properties");
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (Entry<Object, Object> entry : properties.entrySet()) {
			map.put((String) entry.getKey(), (String) entry.getValue());
		}
		return map;
	}

	private void insertMongodb(List<GetListByTagRespDataDTO> list) {
		MongoUtils.insertMany(list);
	}
}
