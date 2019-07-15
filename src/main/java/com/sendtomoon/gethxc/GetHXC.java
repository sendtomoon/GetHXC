package com.sendtomoon.gethxc;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GetHXC {
	public static void main(String[] args) {
		AbstractApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
		context.start();
		Controller controller = context.getBean(Controller.class);
		String SWITCH = "";
		switch (SWITCH) {
		case "RENEW_LIST":// 更新主表所有数据
			controller.mainService();
		case "UPDATE_URL":// 更新URL地址
			controller.updateUrl();
		case "RENEW_SEQ":// 从本地更新序列、标签
			controller.renewFile();
		}
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
