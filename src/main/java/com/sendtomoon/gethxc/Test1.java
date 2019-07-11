package com.sendtomoon.gethxc;

import java.util.HashMap;

import com.alibaba.fastjson.JSON;
import com.sendtomoon.gethxc.config.Config;
import com.sendtomoon.gethxc.utils.HeaderUtils;
import com.sendtomoon.gethxc.utils.HttpUtils;

public class Test1 {
	public static void main(String[] args) {
		HashMap<String, String> req = new HashMap<String, String>();
		req.put("UserName", "qwer789@qq.com");
		req.put("Password", "5913215");
		req.put("AgentID", "0");
		req.put("ClientType", "5");
		try {
			String result = HttpUtils.post("http://hxcuc3.com/Common/GetPayContact", null, "127.0.0.1:1080",
					HeaderUtils.getHeader());
			result = HttpUtils.post("http://hxcuc3.com/User/Login", JSON.toJSONString(req), "127.0.0.1:1080",
					HeaderUtils.getHeader());
			System.err.println(result);
			result = HttpUtils.post("http://hxcuc3.com/User/GetDetail", null, "127.0.0.1:1080", null);
			System.err.println(result);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
