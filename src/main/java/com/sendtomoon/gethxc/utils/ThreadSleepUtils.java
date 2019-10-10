package com.sendtomoon.gethxc.utils;

import org.springframework.stereotype.Component;

@Component
public class ThreadSleepUtils {
	public void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void sleep() {
		try {
			Thread.sleep((long) (Math.random() * 1358));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		for (int i = 0; i <= 100; i++)
			System.err.println((long) (Math.random() * 600 + Math.random() * 600));
	}
}
