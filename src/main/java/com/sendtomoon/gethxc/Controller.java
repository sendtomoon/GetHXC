package com.sendtomoon.gethxc;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.sendtomoon.gethxc.config.Config;
import com.sendtomoon.gethxc.dto.GetListByTagReqDTO;
import com.sendtomoon.gethxc.dto.GetListByTagRespDTO;
import com.sendtomoon.gethxc.dto.GetListByTagRespDataDTO;
import com.sendtomoon.gethxc.dto.OrdertextDTO;
import com.sendtomoon.gethxc.mongo.MongoDAO;
import com.sendtomoon.gethxc.utils.HeaderUtils;
import com.sendtomoon.gethxc.utils.HttpUtils;

@Component
public class Controller {

	@Autowired
	DAO dao;

	public void mainService() {
		GetListByTagRespDTO glbr = this.request(99999);// 获取列表
		List<GetListByTagRespDataDTO> list = glbr.getData();
		if (CollectionUtils.isNotEmpty(list)) {
			this.insert(list);
		}
	}

	private void insert(List<GetListByTagRespDataDTO> list) {
		System.err.println("总数：" + list.size());
		for (GetListByTagRespDataDTO dataDTO : list) {
			GetListByTagRespDataDTO result = dao.getDTOById(dataDTO.getID());
			if (null == result) {
				dataDTO.setSeq(MongoDAO.nextvalue());
				MongoDAO.insertOne(dataDTO);
				System.err.println("插入一条成功" + dataDTO.getID());
			} else {
				this.updateSeeCount(dataDTO);
				System.err.println("锟斤拷锟斤拷一锟斤拷锟缴癸拷" + dataDTO.getID());
			}
		}
	}

	private GetListByTagRespDTO request(int rows) {
		GetListByTagReqDTO glbt = new GetListByTagReqDTO(1, 99999, null, 0, new OrdertextDTO("AddTime", "desc"));
		String str = null;
		try {
			str = HttpUtils.post(Config.value("getListByTag"), JSON.toJSONString(glbt), "127.0.0.1:1080",
					HeaderUtils.getHeader());
		} catch (Exception e) {
			e.printStackTrace();
		}
		GetListByTagRespDTO glbr = JSON.parseObject(str, GetListByTagRespDTO.class);
		return glbr;
	}

	private void updateSeeCount(GetListByTagRespDataDTO dataDTO) {
		MongoDAO.updateSeeCount(dataDTO.getID(), dataDTO.getSeeCount(), dataDTO.getUrl());
	}

}
