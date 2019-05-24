package com.sendtomoon.gethxc;

import java.io.IOException;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringTest {
	public static void main(String[] args) throws IOException {
		AbstractApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
		context.start();
		Download md = context.getBean(Download.class);
		md.mainDown();
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
