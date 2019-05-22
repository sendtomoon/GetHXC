package com.sendtommon.gethxc;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.sendtommon.gethxc.dto.GetListByTagRespDataDTO;

public class DAO {
	public static int add(GetListByTagRespDataDTO dto) {
		SqlSession session = DBSession.getSqlSession();
		return session.insert("hxc.video.add", dto);
	}

	List<GetListByTagRespDataDTO> getWaitDown() {
		SqlSession session = DBSession.getSqlSession();
		return session.selectList("hxc.video.getWaitDown", null);
	}

	public static int update(GetListByTagRespDataDTO dto) {
		SqlSession session = DBSession.getSqlSession();
		return session.update("hxc.video.update", dto);
	}
}
