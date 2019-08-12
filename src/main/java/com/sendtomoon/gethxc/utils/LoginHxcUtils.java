package com.sendtomoon.gethxc.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component
public class LoginHxcUtils {
	@Value("${loginURL}")
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
