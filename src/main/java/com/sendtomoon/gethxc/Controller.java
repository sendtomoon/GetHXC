package com.sendtomoon.gethxc;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sendtomoon.gethxc.config.Config;
import com.sendtomoon.gethxc.dto.GetListByTagReqDTO;
import com.sendtomoon.gethxc.dto.GetListByTagRespDTO;
import com.sendtomoon.gethxc.dto.VideoDTO;
import com.sendtomoon.gethxc.dto.VideoTagsDTO;
import com.sendtomoon.gethxc.dto.OrdertextDTO;
import com.sendtomoon.gethxc.utils.DateUtils;
import com.sendtomoon.gethxc.utils.HeaderUtils;
import com.sendtomoon.gethxc.utils.HttpUtils;

@Component
public class Controller {

	@Autowired
	DAO dao;

	public void updateUrl() {
		List<VideoDTO> list = dao.getUrlNull();
		if (CollectionUtils.isNotEmpty(list)) {
			for (VideoDTO dataDTO : list) {
				String url = this.getClient(String.valueOf(dataDTO.getID()));
				dataDTO.setUrl(url);
				dao.update(dataDTO);
				System.err.println(DateUtils.date() + "更新一条成功：" + dataDTO.getID());
			}
		}
	}

	public void mainService() throws Exception {
		GetListByTagRespDTO glbr = this.request(99999);// 获取列表
		List<VideoDTO> list = glbr.getData();
		if (CollectionUtils.isNotEmpty(list)) {
			this.insert(list);
		}
	}

	private void insert(List<VideoDTO> list) throws Exception {
		System.err.println("总数：" + list.size());
		for (VideoDTO dataDTO : list) {
			VideoDTO result = dao.getDTOById(dataDTO.getID());
			if (null == result) {
				dataDTO.setSeq(dao.nextValue());
				dao.add(dataDTO);
				System.err.println("插入一条成功：" + dataDTO.getID());
			} else {
				String url = this.getClient(String.valueOf(dataDTO.getID()));
				dataDTO.setUrl(url);
				dao.update(dataDTO);
				System.err.println("更新一条成功：" + dataDTO.getID());
			}
			this.tags(dataDTO);
		}
	}

	private String getClient(String videoId) {
		try {
			String str = HttpUtils.post(Config.value("getClient"),
					"{\"videoID\":\"" + videoId + "\",\"userID\":2210195,\"ClientType\":5}", "127.0.0.1:1080",
					HeaderUtils.getClientHeader());
			Thread.sleep(1000);
			JSONObject jo = JSON.parseObject(str).getJSONObject("data");
			return jo.getString("Url");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private GetListByTagRespDTO request(int rows) {
		GetListByTagReqDTO glbt = new GetListByTagReqDTO(1, 99999, null, 0, new OrdertextDTO("AddTime", "desc"));
		String str = null;
		try {
			str = HttpUtils.post(Config.value("getListByTag"), JSON.toJSONString(glbt), "127.0.0.1:1080",
					HeaderUtils.getListHeader());
		} catch (Exception e) {
			e.printStackTrace();
		}
		GetListByTagRespDTO glbr = JSON.parseObject(str, GetListByTagRespDTO.class);
		return glbr;
	}

	private void tags(VideoDTO dto) {
		if (StringUtils.isBlank(dto.getTags())) {
			return;
		}
		String[] tagsArr = dto.getTags().split(",");
		dao.delTagRelated(String.valueOf(dto.getID()));
		for (String tagName : tagsArr) {
			String tag = dao.getTagId(tagName);
			Integer tagId = new Integer(0);
			if (StringUtils.isBlank(tag)) {
				dao.insertTag(tagName);
				tagId = Integer.valueOf(dao.getTagId(tagName));
			} else {
				tagId = Integer.valueOf(tag);
			}
			VideoTagsDTO vtdto = new VideoTagsDTO(dto.getID(), tagId);
			dao.insertTagRelate(vtdto);
		}
	}

}
