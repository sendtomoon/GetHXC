package com.sendtommon.gethxc.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpUtils {
	public static String post(String url, String request, String proxyUrl, Map<String, String> header)
			throws Exception {
		CloseableHttpClient httpclient = HttpClients.custom().build();
		try {
			HttpPost httpPost = new HttpPost(url);
			// 设置头信息
			if (MapUtils.isNotEmpty(header)) {
				for (Map.Entry<String, String> entry : header.entrySet()) {
					httpPost.addHeader(entry.getKey(), entry.getValue());
				}
			}
			// 设置请求参数
			if (StringUtils.isNotBlank(request)) {
				HttpEntity entity = new StringEntity(request);
				httpPost.setEntity(entity);
			}

			if (StringUtils.isNotBlank(proxyUrl)) {
				HttpUtils.setProxy(httpPost, proxyUrl);
			}
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				HttpEntity respEntity = response.getEntity();
				if (respEntity != null) {
					InputStream inStream = respEntity.getContent();
					try {
						BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
						StringBuilder strber = new StringBuilder();
						String line = null;
						while ((line = reader.readLine()) != null) {
							strber.append(line);
						}
						return strber.toString();
					} catch (IOException ex) {
						throw ex;
					} finally {
						inStream.close();
					}
				}
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
		return null;
	}

	private static void setProxy(HttpPost httpPost, String proxyUrl) {
		String[] arr = proxyUrl.split(":");
		HttpHost httpHost = new HttpHost(arr[0], Integer.valueOf(arr[1]));
		RequestConfig config = RequestConfig.custom().setProxy(httpHost).build();
		httpPost.setConfig(config);
	}
}
