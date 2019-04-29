package com.sendtommon.gethxc.dto;

public class OrdertextDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2014625941660391391L;
	private String column;
	private String desc;

	public OrdertextDTO(String column, String desc) {
		this.column = column;
		this.desc = desc;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
