package com.sendtomoon.gethxc;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class DBSession {

	private static ThreadLocal<SqlSession> threadLocal = new ThreadLocal<SqlSession>();
	private static SqlSessionFactory factory = null;

	static {
		try {
			String resource = "mybatis-config.xml";
			InputStream inputStream;
			inputStream = Resources.getResourceAsStream(resource);
			factory = new SqlSessionFactoryBuilder().build(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 鑾峰彇SqlSession
	 * 
	 * @return
	 */
	public static SqlSession getSqlSession() {
		SqlSession sqlSession = threadLocal.get(); // 浠庡綋鍓嶇嚎绋嬭幏鍙�
		if (sqlSession == null) {
			sqlSession = factory.openSession(true);
			threadLocal.set(sqlSession); // 灏唖qlSession涓庡綋鍓嶇嚎绋嬬粦瀹�
		}
		return sqlSession;
	}

	/**
	 * 鍏抽棴SqlSession
	 */
	public static void close() {
		SqlSession sqlSession = threadLocal.get(); // 浠庡綋鍓嶇嚎绋嬭幏鍙�
		if (sqlSession != null) {
			sqlSession.close();
			threadLocal.remove();
		}
	}

}
