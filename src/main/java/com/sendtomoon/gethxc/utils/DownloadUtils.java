package com.sendtomoon.gethxc.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;

import com.sendtomoon.gethxc.dto.M3U8DTO;

@Component
public class DownloadUtils {
	public void download(String url, String proxyUrl, File file) throws Exception {
		CloseableHttpClient httpclient = HttpClients.custom().build();
		HttpPost httpPost = new HttpPost(url);
		if (StringUtils.isNotBlank(proxyUrl)) {
			this.setProxy(httpPost, proxyUrl);
		}
		CloseableHttpResponse response = httpclient.execute(httpPost);
		try {
			HttpEntity respEntity = response.getEntity();
			if (respEntity != null) {
				InputStream inStream = respEntity.getContent();
				FileOutputStream fos = null;
				fos = new FileOutputStream(file);
				try {
					int len = 0;
					byte[] buf = new byte[1024000];
					while ((len = inStream.read(buf)) != -1) {
						fos.write(buf, 0, len);
					}
					System.err.println(file.getName() + "完成");
				} catch (IOException ex) {
					throw ex;
				} finally {
					try {
						if (inStream != null) {
							inStream.close();
						}
						if (fos != null) {
							fos.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} finally {
			response.close();
		}
	}

	public M3U8DTO getM3u8(String url) throws Exception {
		CloseableHttpClient httpclient = HttpClients.custom().build();
		try {
			HttpPost httpPost = new HttpPost(url);
			this.setProxy(httpPost, "127.0.0.1:1080");
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				HttpEntity respEntity = response.getEntity();
				if (response.getStatusLine().getStatusCode() != 200) {
					throw new Exception("找不到地址404");
				}
				if (respEntity != null) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(respEntity.getContent()));
					String basepath = url.substring(0, url.lastIndexOf("/") + 1);
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
							return getM3u8(basepath + line);
						}
						ret.addTs(new M3U8DTO.Ts(line, seconds));
						seconds = 0;
					}
					reader.close();
					return ret;
				}
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
		return null;
	}

	private void setProxy(HttpPost httpPost, String proxyUrl) {
		String[] arr = proxyUrl.split(":");
		HttpHost httpHost = new HttpHost(arr[0], Integer.valueOf(arr[1]));
		RequestConfig config = RequestConfig.custom().setProxy(httpHost).setConnectTimeout(10000).build();
		httpPost.setConfig(config);
	}
}
