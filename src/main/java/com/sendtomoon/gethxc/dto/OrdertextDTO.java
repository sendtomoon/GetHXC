package com.sendtomoon.gethxc.dto;

public class OrdertextDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2014625941660391391L;
	private String column;
	private String dir;

	public OrdertextDTO(String column, String dir) {
		this.column = column;
		this.dir = dir;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}
}
