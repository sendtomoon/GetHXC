package com.sendtommon.gethxc;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.sendtommon.gethxc.dto.GetListByTagRespDataDTO;
import com.sendtommon.gethxc.mongo.MongoDAO;

public class DBSession {

	private static ThreadLocal<SqlSession> threadLocal = new ThreadLocal<>();
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
	 * 获取SqlSession
	 * 
	 * @return
	 */
	public static SqlSession getSqlSession() {
		SqlSession sqlSession = threadLocal.get(); // 从当前线程获取
		if (sqlSession == null) {
			sqlSession = factory.openSession(true);
			threadLocal.set(sqlSession); // 将sqlSession与当前线程绑定
		}
		return sqlSession;
	}

	/**
	 * 关闭SqlSession
	 */
	public static void close() {
		SqlSession sqlSession = threadLocal.get(); // 从当前线程获取
		if (sqlSession != null) {
			sqlSession.close();
			threadLocal.remove();
		}
	}

}
