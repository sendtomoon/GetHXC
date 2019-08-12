package com.sendtomoon.gethxc;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GetHXC {
	public static void main(String[] args) {
		AbstractApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
		context.start();
		Controller controller = context.getBean(Controller.class);
		String SWITCH = "RENEW_LIST";
		switch (SWITCH) {
		case "RENEW_LIST":// 更新主表所有数据
			controller.mainService();
			break;
		case "UPDATE_URL":// 更新URL地址
			controller.updateUrl();
			break;
		case "RENEW_TAG":// 从本地更新序列、标签
			controller.renewTag();
			break;
		case "RENEW_FILENAME":// 从本地更新序列、标签
			controller.renewFile();
			break;
		case "DOWNLOAD":// 下载
			controller.startDownload();
			break;
		case "MERGE":// 合并文件
			controller.mergeFiles();
			break;
		}
//		try {
//			Thread.currentThread().join();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		context.close();
	}
}
