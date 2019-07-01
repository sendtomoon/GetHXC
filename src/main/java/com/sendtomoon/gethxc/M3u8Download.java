package com.sendtomoon.gethxc;

public class M3u8Download {
	public static void main(String[] args) {
		try {
			Download.download(
					"https://world-vod.dchdns.net/hlss/dch/51006-1/h264_LOW_THREE.mp4/index-v1-a1.m3u8",
					"ff.mp4");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
