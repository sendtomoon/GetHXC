package com.sendtomoon.gethxc;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GetHXC {
	public static void main(String[] args) {
		AbstractApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
		context.start();
		Controller controller = context.getBean(Controller.class);
		try {
//			controller.mainService();
//			controller.updateUrl();
			controller.renewFile();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
