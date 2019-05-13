package com.sendtommon.gethxc;

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
import java.util.List;

import com.sendtommon.gethxc.config.Config;
import com.sendtommon.gethxc.dto.GetListByTagRespDataDTO;
import com.sendtommon.gethxc.dto.M3U8DTO;
import com.sendtommon.gethxc.mongo.MongoDAO;
import com.sendtommon.gethxc.utils.DateUtils;

public class Download {
	public static int connTimeout = 60 * 1000;
	public static int readTimeout = 60 * 1000;

	public static void main(String[] args) {
		List<GetListByTagRespDataDTO> list = MongoDAO.firstName();
		for (int i = 0; i < list.size(); i++) {
			GetListByTagRespDataDTO dto = list.get(i);
			System.err.println(DateUtils.date() + " 当前下载第" + dto.getSeq() + "个。数量：" + dto.getSeeCount());
			try {
				Download.download(dto.getUrl(), dto.getFileName());
				// 下载完毕，设置为1，成功
				MongoDAO.updateDownRes(dto.getID());
			} catch (Exception e) {
				e.printStackTrace();
				MongoDAO.updateFail(dto.getID());
				continue;
			}
		}

	}

	private static void download(String m3u8url, String fileName) throws Exception {
		deleteDir(new File(Config.value("tempDir")));
		File tfile = new File(Config.value("tempDir"));
		if (!tfile.exists()) {
			tfile.mkdirs();
		}
		M3U8DTO m3u8ByURL = getM3U8ByURL(m3u8url);
		if (null == m3u8ByURL) {
			throw new Exception("资源不存在" + "   " + fileName);
		}
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
		System.out.println(DateUtils.date() + " 文件下载完毕!");
		mergeFiles(tfile.listFiles(), Config.value("downloadDir") + fileName + ".ts");
	}

	public static M3U8DTO getM3U8ByURL(String m3u8URL) {
		try {
			Proxy proxy = new Proxy(Proxy.Type.HTTP,
					new InetSocketAddress(Config.value("proxyAddress"), Integer.valueOf(Config.value("proxyPort"))));
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

	private static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}

}
