package com.sendtomoon.gethxc.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service
public class LoginHxcUtils {

	@Value(value = "${loginURL}")
	String loginURL;

	@Value("${loginEntity}")
	String loginEntity;

	@Value("${proxyURL}")
	String proxyURL;

	public String getToken() {
		return this.login().getJSONObject("data").getString("Token");
	}

	private JSONObject login() {
		try {
			String result = HttpUtils.post(loginURL, loginEntity, proxyURL, null);
			JSONObject obj = JSONObject.parseObject(result);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
