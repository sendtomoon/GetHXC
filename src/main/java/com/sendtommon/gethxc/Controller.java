package com.sendtommon.gethxc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections4.CollectionUtils;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.alibaba.fastjson.JSON;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sendtommon.gethxc.dto.GetListByTagReqDTO;
import com.sendtommon.gethxc.dto.GetListByTagRespDTO;
import com.sendtommon.gethxc.dto.GetListByTagRespDataDTO;
import com.sendtommon.gethxc.dto.OrdertextDTO;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

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

	public void mainService() {
		GetListByTagReqDTO glbt = new GetListByTagReqDTO(1, 6000, null, 0, new OrdertextDTO("AddTime", "desc"));
		String str = null;
		try {
			str = HttpUtils.post(System.getProperty("getListByTag"), JSON.toJSONString(glbt), null, this.getHeader());
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

	private void insertMongodb(List<GetListByTagRespDataDTO> list) {
		MongoCredential credential = MongoCredential.createCredential(System.getProperty("mongodb.user"),
				System.getProperty("mongodb.database"), System.getProperty("mongodb.pwd").toCharArray());
		MongoClient mongoClient = MongoClients.create(MongoClientSettings.builder()
				.applyToClusterSettings(
						builder -> builder.hosts(Arrays.asList(new ServerAddress(System.getProperty("mongodb.address"),
								Integer.valueOf(System.getProperty("mongodb.port"))))))
				.credential(credential).build());
		MongoDatabase database = mongoClient.getDatabase("hanxiucao");
		MongoCollection<GetListByTagRespDataDTO> collection = database.getCollection("video_list",
				GetListByTagRespDataDTO.class);
		CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		collection = collection.withCodecRegistry(pojoCodecRegistry);
		collection.insertMany(list);
	}
}
