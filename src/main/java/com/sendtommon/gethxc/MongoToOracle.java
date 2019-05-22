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

public class MongoToOracle {

	private static DAO dao = null;
	static {
		try {
			String resource = "mybatis-config.xml";
			InputStream inputStream;
			inputStream = Resources.getResourceAsStream(resource);
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
			SqlSession sqlSession = sqlSessionFactory.openSession(true);
			dao = sqlSession.getMapper(DAO.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		MongoToOracle ex = new MongoToOracle();
		ex.move();
	}

	public static List<GetListByTagRespDataDTO> getWaitDown() {
		return dao.getWaitDown();
	}

	public static int update(GetListByTagRespDataDTO dto) {
		return dao.update(dto);
	}

	public void move() {
		List<GetListByTagRespDataDTO> list = MongoDAO.getAll();
		for (GetListByTagRespDataDTO dto : list) {
			dao.add(dto);
		}
	}

}
