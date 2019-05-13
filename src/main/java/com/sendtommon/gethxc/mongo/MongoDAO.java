package com.sendtommon.gethxc.mongo;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.Arrays;
import java.util.List;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import com.sendtommon.gethxc.config.Config;
import com.sendtommon.gethxc.dto.GetListByTagRespDataDTO;

public class MongoUtils {
	private static MongoDatabase database = null;
	private static CodecRegistry pojoCodecRegistry = null;
	private static MongoCollection<GetListByTagRespDataDTO> collection = null;
	static {
		MongoCredential credential = MongoCredential.createCredential(Config.value("mongodb.user"),
				Config.value("mongodb.database"), Config.value("mongodb.pwd").toCharArray());
		MongoClient mongoClient = MongoClients.create(MongoClientSettings.builder()
				.applyToClusterSettings(
						builder -> builder.hosts(Arrays.asList(new ServerAddress(Config.value("mongodb.address"),
								Integer.valueOf(Config.value("mongodb.port"))))))
				.credential(credential).build());
		database = mongoClient.getDatabase("hanxiucao");
		pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		collection = database.getCollection("video_list", GetListByTagRespDataDTO.class);
		collection = collection.withCodecRegistry(pojoCodecRegistry);

	}

	public static void insertMany(List<GetListByTagRespDataDTO> list) {
		MongoCollection<GetListByTagRespDataDTO> collection = database.getCollection("video_list",
				GetListByTagRespDataDTO.class);
		collection = collection.withCodecRegistry(pojoCodecRegistry);
		collection.insertMany(list);
	}

	/**
	 * 插入一条对象
	 * 
	 * @param list
	 */
	public static void insertOne(GetListByTagRespDataDTO list) {
		MongoCollection<GetListByTagRespDataDTO> collection = database.getCollection("video_list",
				GetListByTagRespDataDTO.class);
		collection = collection.withCodecRegistry(pojoCodecRegistry);
		collection.insertOne(list);
	}

	/**
	 * 通过iD获取对象
	 * 
	 * @param filed
	 * @param value
	 * @return
	 */
	public static GetListByTagRespDataDTO isExistOfId(Integer id) {
		return collection.find(eq("iD", id)).first();
	}

	public static GetListByTagRespDataDTO firstName() {
		MongoCollection<GetListByTagRespDataDTO> collection = database.getCollection("video_list",
				GetListByTagRespDataDTO.class);
		collection = collection.withCodecRegistry(pojoCodecRegistry);
		return collection.find(and(eq("fail", 0), eq("downloaded", 0))).sort(Sorts.descending("seeCount")).first();
	}

	public static void updateDownRes(Object id) {
		MongoCollection<GetListByTagRespDataDTO> collection = database.getCollection("video_list",
				GetListByTagRespDataDTO.class);
		collection = collection.withCodecRegistry(pojoCodecRegistry);
		collection.updateOne(eq("iD", id), combine(set("downloaded", 1)));
	}

	public static void updateFail(Object id) {
		MongoCollection<GetListByTagRespDataDTO> collection = database.getCollection("video_list",
				GetListByTagRespDataDTO.class);
		collection = collection.withCodecRegistry(pojoCodecRegistry);
		collection.updateOne(eq("iD", id), combine(set("fail", 1)));
	}

	public static void updateMany() {
		MongoCollection<GetListByTagRespDataDTO> collection = database.getCollection("video_list",
				GetListByTagRespDataDTO.class);
		collection = collection.withCodecRegistry(pojoCodecRegistry);
		collection.updateMany(eq("downloaded", 0), combine(set("fail", 0)));
	}

	public static void updateSeeCount(Object id, Integer seeCount) {
		MongoCollection<GetListByTagRespDataDTO> collection = database.getCollection("video_list",
				GetListByTagRespDataDTO.class);
		collection = collection.withCodecRegistry(pojoCodecRegistry);
		collection.updateOne(eq("iD", id), combine(set("seeCount", seeCount)));
	}

}
