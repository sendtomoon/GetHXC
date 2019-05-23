package com.sendtomoon.gethxc;

import org.apache.ibatis.session.SqlSession;

public class Testclass {
	public void test1() {
		SqlSession session = DBSession.getSqlSession();
		String str = session.selectOne("hxc.video.test", null);
		System.err.println(str);
	}
}
