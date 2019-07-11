package com.sendtomoon.gethxc;

import com.sendtomoon.gethxc.utils.HttpUtils;

public class M3u8Download {
	public static void main(String[] args) {
		try {
			String req = "callback=__g_mini_login_dialog_callback__1561969439843__785412__&response_type=cross-domain&uid=18566768539&password=L853093366o&f=pc_s_daohang_index_gh";
			HttpUtils.post("https://login.koolearn.com/sso/login.do", req, null, null);
			Download.download(
					"https://pl.koolearn.com/api/hls/sgmt_m3u8?consumerType=1002001&playerVersion=1.0.1&videoType=0&sign=rso1q-EjvvnpGDKKxgTiWjFQIaA&videoId=136832&userId=76591641&timestamp=1561968207103",
					"ff.mp4");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
