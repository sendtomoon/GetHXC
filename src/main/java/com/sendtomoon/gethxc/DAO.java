package com.sendtomoon.gethxc;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sendtomoon.gethxc.dto.GetListByTagRespDataDTO;

@Repository
public interface DAO {
	public int add(GetListByTagRespDataDTO dto);

	public List<GetListByTagRespDataDTO> getWaitDown();

	public int update(GetListByTagRespDataDTO dto);

}
