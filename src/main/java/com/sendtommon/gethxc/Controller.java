package com.sendtommon.gethxc;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.sendtommon.gethxc.config.Config;
import com.sendtommon.gethxc.dto.GetListByTagReqDTO;
import com.sendtommon.gethxc.dto.GetListByTagRespDTO;
import com.sendtommon.gethxc.dto.GetListByTagRespDataDTO;
import com.sendtommon.gethxc.dto.OrdertextDTO;
import com.sendtommon.gethxc.mongo.MongoDAO;
import com.sendtommon.gethxc.utils.HeaderUtils;
import com.sendtommon.gethxc.utils.HttpUtils;

/**
 */
public class Controller {

	public void mainService() {
		// 一次性获取所有结果
		GetListByTagRespDTO glbr = this.request(99999);
		List<GetListByTagRespDataDTO> list = glbr.getData();
		if (CollectionUtils.isNotEmpty(list)) {
			this.insert(list);// 结果不为空则插入数据
		}
	}

	/**
	 * 插入数据
	 * 
	 * @param list
	 */
	private void insert(List<GetListByTagRespDataDTO> list) {
		System.err.println("总记录数" + list.size());
		for (GetListByTagRespDataDTO dataDTO : list) {
			GetListByTagRespDataDTO result = MongoDAO.isExistOfId(dataDTO.getID());// 通过id获取对象，判断对象是否已经存在
			// 如果记录不存在，则新增一条，如果记录已经存在，则更新阅读数
			if (null == result) {
				dataDTO.setSeq(MongoDAO.nextvalue());
				MongoDAO.insertOne(dataDTO);
				System.err.println("插入一条成功");
			} else {
				this.updateSeeCount(dataDTO);
				System.err.println("更新一条成功");
			}
		}
	}

	/**
	 * 请求数据并解析json
	 * 
	 * @param rows
	 * @return
	 */
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

	/**
	 * 修改观看人数
	 * 
	 * @param dataDTO
	 */
	private void updateSeeCount(GetListByTagRespDataDTO dataDTO) {
		MongoDAO.updateSeeCount(dataDTO.getID(), dataDTO.getSeeCount(), dataDTO.getUrl());
	}

}
