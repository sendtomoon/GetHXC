package com.sendtomoon.gethxc.utils;

import java.time.LocalDateTime;

public class DateUtils {
	public static String date() {
		String str = LocalDateTime.now().toString();
		return str.replace("T", " ");
	}

	public static void main(String[] args) {
		DateUtils.date();
	}
}
