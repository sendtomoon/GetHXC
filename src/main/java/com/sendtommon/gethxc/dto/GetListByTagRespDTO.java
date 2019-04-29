package com.sendtommon.gethxc.dto;

import java.util.List;

public class GetListByTagRespDTO extends BaseDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4879241331059026775L;
	private Integer code;
	private Integer drow;
	private String message;
	private Integer recordsFiltered;
	private Integer recordsSum;
	private Integer recordsSum2;
	private Integer recordsTotal;
	private List<GetListByTagRespDataDTO> data;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Integer getDrow() {
		return drow;
	}

	public void setDrow(Integer drow) {
		this.drow = drow;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(Integer recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public Integer getRecordsSum() {
		return recordsSum;
	}

	public void setRecordsSum(Integer recordsSum) {
		this.recordsSum = recordsSum;
	}

	public Integer getRecordsSum2() {
		return recordsSum2;
	}

	public void setRecordsSum2(Integer recordsSum2) {
		this.recordsSum2 = recordsSum2;
	}

	public Integer getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(Integer recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public List<GetListByTagRespDataDTO> getData() {
		return data;
	}

	public void setData(List<GetListByTagRespDataDTO> data) {
		this.data = data;
	}

}
