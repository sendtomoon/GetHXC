package com.sendtomoon.gethxc.utils;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.sendtomoon.gethxc.config.Config;

@Component
public class LoginHxcUtils {
	public static void main(String[] args) {
		LoginHxcUtils ll = new LoginHxcUtils();
		String token = ll.getToken();
		System.err.println(token);
	}

	public String getToken() {
		return this.login().getJSONObject("data").getString("Token");
	}

	private JSONObject login() {
		try {
			String result = HttpUtils.post(Config.value("loginURL"), Config.value("loginEntity"),
					Config.value("proxyURL"), null);
			JSONObject obj = JSONObject.parseObject(result);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
