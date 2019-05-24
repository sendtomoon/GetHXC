package com.sendtomoon.gethxc;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sendtomoon.gethxc.dto.GetListByTagRespDataDTO;

//@Component
public interface HXCDAO {
	public int add(GetListByTagRespDataDTO dto);

	public List<GetListByTagRespDataDTO> getWaitDown();

	public int update(GetListByTagRespDataDTO dto);

}
