package com.sendtomoon.gethxc.dto;

import java.util.List;

public class GetListByTagReqDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5156602300789456834L;

	private Integer ClientType;
	private Integer length;
	private Integer searchtext;
	private Integer start;
	private Integer IsVIP = 1;
	private List<OrdertextDTO> ordertext;

	public GetListByTagReqDTO() {

	}

	public GetListByTagReqDTO(Integer ClientType, Integer length, Integer searchtext, Integer start,
			List<OrdertextDTO> ordertext) {
		this.ClientType = ClientType;
		this.length = length;
		this.searchtext = searchtext;
		this.start = start;
		this.ordertext = ordertext;
	}

	public Integer getClientType() {
		return ClientType;
	}

	public void setClientType(Integer clientType) {
		ClientType = clientType;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getSearchtext() {
		return searchtext;
	}

	public void setSearchtext(Integer searchtext) {
		this.searchtext = searchtext;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public void setOrdertext(List<OrdertextDTO> ordertext) {
		this.ordertext = ordertext;
	}

	public List<OrdertextDTO> getOrdertext() {
		return ordertext;
	}

	public Integer getIsVIP() {
		return IsVIP;
	}

	public void setIsVIP(Integer isVIP) {
		IsVIP = isVIP;
	}

}
