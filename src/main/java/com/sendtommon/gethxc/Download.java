package com.sendtommon.gethxc;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.Arrays;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static com.mongodb.client.model.Filters.*;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sendtommon.gethxc.config.Config;
import com.sendtommon.gethxc.dto.GetListByTagRespDataDTO;
import com.sendtommon.gethxc.dto.M3U8DTO;
import static com.mongodb.client.model.Updates.*;

public class Download {
	public static int connTimeout = 30 * 60 * 1000;
	public static int readTimeout = 30 * 60 * 1000;

	public static void main(String[] args) {
		MongoCredential credential = MongoCredential.createCredential(Config.value("mongodb.user"),
				Config.value("mongodb.database"), Config.value("mongodb.pwd").toCharArray());
		MongoClient mongoClient = MongoClients.create(MongoClientSettings.builder()
				.applyToClusterSettings(
						builder -> builder.hosts(Arrays.asList(new ServerAddress(Config.value("mongodb.address"),
								Integer.valueOf(Config.value("mongodb.port"))))))
				.credential(credential).build());
		MongoDatabase database = mongoClient.getDatabase("hanxiucao");
		MongoCollection<GetListByTagRespDataDTO> collection = database.getCollection("video_list",
				GetListByTagRespDataDTO.class);
		CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		collection = collection.withCodecRegistry(pojoCodecRegistry);
		int i = 1;
		while (true) {
			System.err.println(i);
			GetListByTagRespDataDTO dto = collection.find(eq("downloaded", 0)).first();
			if (null == dto) {
				break;
			}
			try {
				dto.setFileName(dto.getName().replace(" ", "_"));
				Download.download(dto.getUrl(), dto.getFileName());
				collection.updateOne(eq("_id", dto.getId()), combine(set("downloaded", 1)));
			} catch (Exception e) {
				e.printStackTrace();
			}
			i++;
		}

	}

	private static void download(String m3u8url, String fileName) {
		File tfile = new File(Config.value("tempDir"));
		if (!tfile.exists()) {
			tfile.mkdirs();
		}
		M3U8DTO m3u8ByURL = getM3U8ByURL(m3u8url);
		String basePath = m3u8ByURL.getBasepath();
		m3u8ByURL.getTsList().stream().parallel().forEach(m3U8Ts -> {
			File file = new File(Config.value("tempDir") + File.separator + m3U8Ts.getFile());
			if (!file.exists()) {// 下载过的就不管了
				FileOutputStream fos = null;
				InputStream inputStream = null;
				try {
					URL url = new URL(basePath + m3U8Ts.getFile());
					Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(Config.value("proxyAddress"),
							Integer.valueOf(Config.value("proxyPort"))));
					HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
					conn.setConnectTimeout(connTimeout);
					conn.setReadTimeout(readTimeout);
					if (conn.getResponseCode() == 200) {
						inputStream = conn.getInputStream();
						fos = new FileOutputStream(file);// 会自动创建文件
						int len = 0;
						byte[] buf = new byte[1024];
						while ((len = inputStream.read(buf)) != -1) {
							fos.write(buf, 0, len);// 写入流中
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {// 关流
					try {
						if (inputStream != null) {
							inputStream.close();
						}
						if (fos != null) {
							fos.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		System.out.println("文件下载完毕!");
		mergeFiles(tfile.listFiles(), Config.value("downloadDir") + fileName + ".ts");
	}

	public static M3U8DTO getM3U8ByURL(String m3u8URL) {
		try {
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 1080));
			HttpURLConnection conn = (HttpURLConnection) new URL(m3u8URL).openConnection(proxy);
			if (conn.getResponseCode() == 200) {
				String realUrl = conn.getURL().toString();
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String basepath = realUrl.substring(0, realUrl.lastIndexOf("/") + 1);
				M3U8DTO ret = new M3U8DTO();
				ret.setBasepath(basepath);

				String line;
				float seconds = 0;
				int mIndex;
				while ((line = reader.readLine()) != null) {
					if (line.startsWith("#")) {
						if (line.startsWith("#EXTINF:")) {
							line = line.substring(8);
							if ((mIndex = line.indexOf(",")) != -1) {
								line = line.substring(0, mIndex + 1);
							}
							try {
								seconds = Float.parseFloat(line);
							} catch (Exception e) {
								seconds = 0;
							}
						}
						continue;
					}
					if (line.endsWith("m3u8")) {
						return getM3U8ByURL(basepath + line);
					}
					ret.addTs(new M3U8DTO.Ts(line, seconds));
					seconds = 0;
				}
				reader.close();

				return ret;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean mergeFiles(File[] fpaths, String resultPath) {
		if (fpaths == null || fpaths.length < 1) {
			return false;
		}

		if (fpaths.length == 1) {
			return fpaths[0].renameTo(new File(resultPath));
		}
		for (int i = 0; i < fpaths.length; i++) {
			if (!fpaths[i].exists() || !fpaths[i].isFile()) {
				return false;
			}
		}
		File resultFile = new File(resultPath);

		try {
			FileOutputStream fs = new FileOutputStream(resultFile, true);
			FileChannel resultFileChannel = fs.getChannel();
			FileInputStream tfs;
			for (int i = 0; i < fpaths.length; i++) {
				tfs = new FileInputStream(fpaths[i]);
				FileChannel blk = tfs.getChannel();
				resultFileChannel.transferFrom(blk, resultFileChannel.size(), blk.size());
				tfs.close();
				blk.close();
			}
			fs.close();
			resultFileChannel.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

}
