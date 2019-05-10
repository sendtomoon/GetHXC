package com.sendtommon.gethxc;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.sendtommon.gethxc.config.Config;
import com.sendtommon.gethxc.dto.GetListByTagReqDTO;
import com.sendtommon.gethxc.dto.GetListByTagRespDTO;
import com.sendtommon.gethxc.dto.GetListByTagRespDataDTO;
import com.sendtommon.gethxc.dto.OrdertextDTO;
import com.sendtommon.gethxc.mongo.MongoUtils;
import com.sendtommon.gethxc.utils.HeaderUtils;

/**
 * @author lbt42
 *
 */
public class Controller {

	public void mainService() {
		// һ���Ի�ȡ���н��
		GetListByTagRespDTO glbr = this.request(99999);
		List<GetListByTagRespDataDTO> list = glbr.getData();
		if (CollectionUtils.isNotEmpty(list)) {
			this.insert(list);// �����Ϊ�����������
		}
	}

	/**
	 * ��������
	 * 
	 * @param list
	 */
	private void insert(List<GetListByTagRespDataDTO> list) {
		System.err.println("�ܼ�¼��" + list.size());
		for (GetListByTagRespDataDTO dataDTO : list) {
			GetListByTagRespDataDTO result = MongoUtils.isExistOfId(dataDTO.getID());// ͨ��id��ȡ�����ж϶����Ƿ��Ѿ�����
			// �����¼�����ڣ�������һ���������¼�Ѿ����ڣ�������Ķ���
			if (null == result) {
				MongoUtils.insertOne(dataDTO);
				System.err.println("����һ���ɹ�");
			} else {
				this.updateSeeCount(dataDTO);
				System.err.println("����һ���ɹ�");
			}
		}
	}

	/**
	 * �������ݲ�����json
	 * 
	 * @param rows
	 * @return
	 */
	private GetListByTagRespDTO request(int rows) {
		GetListByTagReqDTO glbt = new GetListByTagReqDTO(1, 20, null, rows, new OrdertextDTO("AddTime", "desc"));
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
	 * �޸Ĺۿ�����
	 * 
	 * @param dataDTO
	 */
	private void updateSeeCount(GetListByTagRespDataDTO dataDTO) {
		MongoUtils.updateSeeCount(dataDTO.getID(), dataDTO.getSeeCount());
	}

}
