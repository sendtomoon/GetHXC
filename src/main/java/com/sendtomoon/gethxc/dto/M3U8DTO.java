package com.sendtomoon.gethxc.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class M3U8DTO extends BaseDTO {

	private static final long serialVersionUID = -2040713370081106737L;
	private String basepath;
	private List<Ts> tsList = new ArrayList<Ts>();
	private long startTime;
	private long endTime;
	private long startDownloadTime;
	private long endDownloadTime;

	public String getBasepath() {
		return basepath;
	}

	public void setBasepath(String basepath) {
		this.basepath = basepath;
	}

	public List<Ts> getTsList() {
		return tsList;
	}

	public void setTsList(List<Ts> tsList) {
		this.tsList = tsList;
	}

	public void addTs(Ts ts) {
		this.tsList.add(ts);
	}

	public long getStartDownloadTime() {
		return startDownloadTime;
	}

	public void setStartDownloadTime(long startDownloadTime) {
		this.startDownloadTime = startDownloadTime;
	}

	public long getEndDownloadTime() {
		return endDownloadTime;
	}

	public void setEndDownloadTime(long endDownloadTime) {
		this.endDownloadTime = endDownloadTime;
	}

	public long getStartTime() {
		if (tsList.size() > 0) {
			Collections.sort(tsList);
			startTime = tsList.get(0).getLongDate();
			return startTime;
		}
		return 0;
	}

	public long getEndTime() {
		if (tsList.size() > 0) {
			Ts m3U8Ts = tsList.get(tsList.size() - 1);
			endTime = m3U8Ts.getLongDate() + (long) (m3U8Ts.getSeconds() * 1000);
			return endTime;
		}
		return 0;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("basepath: " + basepath);
		for (Ts ts : tsList) {
			sb.append("\nts_file_name = " + ts);
		}
		sb.append("\n\nstartTime = " + startTime);
		sb.append("\n\nendTime = " + endTime);
		sb.append("\n\nstartDownloadTime = " + startDownloadTime);
		sb.append("\n\nendDownloadTime = " + endDownloadTime);
		return sb.toString();
	}

	public static class Ts implements Comparable<Ts> {
		private String file;
		private float seconds;

		public Ts(String file, float seconds) {
			this.file = file;
			this.seconds = seconds;
		}

		public String getFile() {
			return file;
		}

		public void setFile(String file) {
			this.file = file;
		}

		public float getSeconds() {
			return seconds;
		}

		public void setSeconds(float seconds) {
			this.seconds = seconds;
		}

		@Override
		public String toString() {
			return file + " (" + seconds + "sec)";
		}

		public long getLongDate() {
			try {
				return Long.parseLong(file.substring(0, file.lastIndexOf(".")));
			} catch (Exception e) {
				return 0;
			}
		}

		@Override
		public int compareTo(Ts o) {
			return file.compareTo(o.file);
		}
	}

}
