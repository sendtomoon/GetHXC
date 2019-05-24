package com.sendtomoon.gethxc;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.sendtomoon.gethxc.dto.GetListByTagRespDataDTO;

@Repository
public interface DAO {
	public int add(GetListByTagRespDataDTO dto);

	public List<GetListByTagRespDataDTO> getWaitDown();

	public int update(GetListByTagRespDataDTO dto);

//	public int add(GetListByTagRespDataDTO dto) {
//		return getSqlSession().insert("hxc.video.add", dto);
//	}
//
//	public List<GetListByTagRespDataDTO> getWaitDown() {
//		return getSqlSession().selectList("hxc.video.getWaitDown", null);
//	}
//
//	public int update(GetListByTagRespDataDTO dto) {
//		return getSqlSession().update("hxc.video.update", dto);
//	}

}
