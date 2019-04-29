package com.sendtommon.gethxc;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class DocUtils {
	public static Document getDoc(String url) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url).proxy("127.0.0.1", 1080).post();
		} catch (IOException e) {
			e.printStackTrace();
			return doc;
		}
		return doc;
	}
}
