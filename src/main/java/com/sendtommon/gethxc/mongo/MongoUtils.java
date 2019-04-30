package com.sendtommon.gethxc.mongo;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.Arrays;
import java.util.List;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sendtommon.gethxc.dto.GetListByTagRespDataDTO;

public class MongoUtils {
	private static MongoDatabase database = null;
	private static CodecRegistry pojoCodecRegistry = null;
	static {
		MongoCredential credential = MongoCredential.createCredential(System.getProperty("mongodb.user"),
				System.getProperty("mongodb.database"), System.getProperty("mongodb.pwd").toCharArray());
		MongoClient mongoClient = MongoClients.create(MongoClientSettings.builder()
				.applyToClusterSettings(
						builder -> builder.hosts(Arrays.asList(new ServerAddress(System.getProperty("mongodb.address"),
								Integer.valueOf(System.getProperty("mongodb.port"))))))
				.credential(credential).build());
		database = mongoClient.getDatabase("hanxiucao");
		pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	}

	public static void insertMany(List<GetListByTagRespDataDTO> list) {
		MongoCollection<GetListByTagRespDataDTO> collection = database.getCollection("video_list",
				GetListByTagRespDataDTO.class);
		collection = collection.withCodecRegistry(pojoCodecRegistry);
		collection.insertMany(list);
	}

	public static GetListByTagRespDataDTO first(String filed, Object value) {
		MongoCollection<GetListByTagRespDataDTO> collection = database.getCollection("video_list",
				GetListByTagRespDataDTO.class);
		collection = collection.withCodecRegistry(pojoCodecRegistry);
		return collection.find(eq(filed, value)).first();
	}

	public static void updateDownRes(ObjectId id) {
		MongoCollection<GetListByTagRespDataDTO> collection = database.getCollection("video_list",
				GetListByTagRespDataDTO.class);
		collection = collection.withCodecRegistry(pojoCodecRegistry);
		collection.updateOne(eq("_id", id), combine(set("downloaded", 1)));
	}

}
