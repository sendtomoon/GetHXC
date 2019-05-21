package com.sendtommon.gethxc.mongo;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

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

public class MongoDAO {
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
	 * 插入一条数据
	 * 
	 * @param list
	 */
	public static void insertOne(GetListByTagRespDataDTO list) {
		collection.insertOne(list);
	}

	public static GetListByTagRespDataDTO isExistOfId(Integer id) {
		return collection.find(eq("iD", id)).first();
	}

	public static List<GetListByTagRespDataDTO> firstName() {
		List<GetListByTagRespDataDTO> list = new ArrayList<GetListByTagRespDataDTO>();
		Consumer<GetListByTagRespDataDTO> sdfs = new Consumer<GetListByTagRespDataDTO>() {
			@Override
			public void accept(GetListByTagRespDataDTO t) {
				list.add(t);
			}

		};
		collection.find(and(eq("downloaded", 0))).sort(Sorts.descending("seeCount")).forEach(sdfs);
		return list;
	}

	public static List<GetListByTagRespDataDTO> getAll() {
		List<GetListByTagRespDataDTO> list = new ArrayList<GetListByTagRespDataDTO>();
		Consumer<GetListByTagRespDataDTO> sdfs = new Consumer<GetListByTagRespDataDTO>() {
			@Override
			public void accept(GetListByTagRespDataDTO t) {
				list.add(t);
			}

		};
		collection.find().forEach(sdfs);
		return list;
	}

	public static void updateDownRes(Object id, Integer i) {
		collection.updateOne(eq("iD", id), combine(set("downloaded", i)));
	}

	public static void updateFail(Object id, Integer i) {
		collection.updateOne(eq("iD", id), combine(set("fail", i)));
	}

	/**
	 * 锟睫改观匡拷锟斤拷锟斤拷锟斤拷锟斤拷锟截碉拷址
	 * 
	 * @param id
	 * @param seeCount
	 * @param url
	 */
	public static void updateSeeCount(Object id, Integer seeCount, String url) {
		collection.updateOne(eq("iD", id), combine(set("seeCount", seeCount), set("url", url)));
	}

	/**
	 * 锟睫革拷锟侥硷拷锟斤拷锟斤拷seq
	 * 
	 * @param id
	 * @param seeCount
	 * @param url
	 */
	public static void updateFilename(Object id, String filename, Integer seq) {
		collection.updateOne(eq("iD", id), combine(set("fileName", filename), set("seq", seq)));
	}

	/**
	 * 通锟斤拷seeCount锟斤拷锟斤拷锟饺★拷斜锟�
	 * 
	 * @return
	 */
	public static List<GetListByTagRespDataDTO> getOrderBySeeCount() {
		List<GetListByTagRespDataDTO> list = new ArrayList<GetListByTagRespDataDTO>();
		Consumer<GetListByTagRespDataDTO> sdfs = new Consumer<GetListByTagRespDataDTO>() {
			@Override
			public void accept(GetListByTagRespDataDTO t) {
				list.add(t);
			}

		};
		collection.find().sort(Sorts.descending("seeCount")).forEach(sdfs);
		return list;
	}

	public static Integer nextvalue() {
		int i = collection.find().sort(Sorts.descending("seq")).first().getSeq();
		i++;
		return i;
	}

}
