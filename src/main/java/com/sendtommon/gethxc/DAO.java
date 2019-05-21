package com.sendtommon.gethxc;

import java.util.List;

import com.sendtommon.gethxc.dto.GetListByTagRespDataDTO;

public interface DAO {
	void add(GetListByTagRespDataDTO dto);
	List<GetListByTagRespDataDTO> getWaitDown();
}
