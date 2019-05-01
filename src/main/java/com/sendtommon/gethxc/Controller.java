package com.sendtommon.gethxc;

import java.util.Map.Entry;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections4.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.sendtommon.gethxc.config.Config;
import com.sendtommon.gethxc.dto.GetListByTagReqDTO;
import com.sendtommon.gethxc.dto.GetListByTagRespDTO;
import com.sendtommon.gethxc.dto.GetListByTagRespDataDTO;
import com.sendtommon.gethxc.dto.OrdertextDTO;
import com.sendtommon.gethxc.mongo.MongoUtils;

public class Controller {

	public void mainService() {
		// 循环控制分页
		for (int i = 0; i <= 99999; i++) {
			// 每页行数
			int rows = i * 20;
			// 获取结果
			GetListByTagRespDTO glbr = this.request(rows);
			List<GetListByTagRespDataDTO> list = glbr.getData();
			// 判断结果是否为空
			if (CollectionUtils.isNotEmpty(list)) {
				this.insert(list);// 结果不为空则插入数据
			} else {
				break;
			}
		}
	}

	/**
	 * 插入数据
	 * 
	 * 
	 * @param list
	 */
	private void insert(List<GetListByTagRespDataDTO> list) {
		for (GetListByTagRespDataDTO dataDTO : list) {
			GetListByTagRespDataDTO result = MongoUtils.first("iD", dataDTO.getID());
			if (null == result) {
				MongoUtils.insertOne(dataDTO);
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
		GetListByTagReqDTO glbt = new GetListByTagReqDTO(1, 20, null, rows, new OrdertextDTO("AddTime", "desc"));
		String str = null;
		try {
			str = HttpUtils.post(Config.value("getListByTag"), JSON.toJSONString(glbt), null, this.getHeader());
		} catch (Exception e) {
			e.printStackTrace();
		}
		GetListByTagRespDTO glbr = JSON.parseObject(str, GetListByTagRespDTO.class);
		return glbr;
	}

	/**
	 * post请求头信息
	 * 
	 * @return
	 */
	private Map<String, String> getHeader() {
		Properties properties = Config.getHeader();
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (Entry<Object, Object> entry : properties.entrySet()) {
			map.put((String) entry.getKey(), (String) entry.getValue());
		}
		return map;
	}
}
