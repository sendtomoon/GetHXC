package com.sendtomoon.gethxc;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;

import com.sendtomoon.gethxc.dto.GetListByTagRespDataDTO;

public class DAO extends SqlSessionDaoSupport {

	public int add(GetListByTagRespDataDTO dto) {
		return getSqlSession().insert("hxc.video.add", dto);
	}

	public List<GetListByTagRespDataDTO> getWaitDown() {
		return getSqlSession().selectList("hxc.video.getWaitDown", null);
	}

	public int update(GetListByTagRespDataDTO dto) {
		return getSqlSession().update("hxc.video.update", dto);
	}

}
