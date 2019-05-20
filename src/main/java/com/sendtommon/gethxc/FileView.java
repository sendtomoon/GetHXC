package com.sendtommon.gethxc;

import java.io.File;
import java.util.HashMap;

public class FileView {
	public static void main(String[] args) {
		File file = new File("e:/hxc");
		File file2 = new File("e:/hxc");
		HashMap<String, String> map1 = new HashMap<String, String>();
		for (File str : file.listFiles()) {
			map1.put(str.getName().split("_")[0], "cccc");
		}
		for (File str : file2.listFiles()) {
			System.err.println(map1.get(str.getName().split("_")[0]));
		}
	}
}
 