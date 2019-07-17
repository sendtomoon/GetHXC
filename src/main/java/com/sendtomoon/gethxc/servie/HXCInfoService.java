package com.sendtomoon.gethxc.servie;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sendtomoon.gethxc.DAO;
import com.sendtomoon.gethxc.config.Config;
import com.sendtomoon.gethxc.dto.GetListByTagReqDTO;
import com.sendtomoon.gethxc.dto.GetListByTagRespDTO;
import com.sendtomoon.gethxc.dto.OrdertextDTO;
import com.sendtomoon.gethxc.dto.VideoDTO;
import com.sendtomoon.gethxc.dto.VideoTagsDTO;
import com.sendtomoon.gethxc.utils.DateUtils;
import com.sendtomoon.gethxc.utils.HeaderUtils;
import com.sendtomoon.gethxc.utils.HttpUtils;

@Service
public class HXCInfoService {
	@Autowired
	private DAO dao;

	public void renewFile() {
		List<VideoDTO> list = dao.getHxcVideo();
		for (int i = 0; i < list.size(); i++) {
			VideoDTO dataDTO = list.get(i);
			String baseName = this.zero(dataDTO.getSeq()) + "_" + dataDTO.getName().replace(" ", "_").replace("\"", "")
					.replace("“", "").replace("”", "").replace("\\", "").replace("'", "").replace("\\.", "");
			dataDTO.setFileName(baseName);
			System.err.println(baseName);
			dao.updateSeq(dataDTO);
		}
	}

	public void renewTag() {
		List<VideoDTO> list = dao.getHxcVideo();
		for (int i = 0; i < list.size(); i++) {
			VideoDTO dataDTO = list.get(i);
			this.tags(dataDTO);
		}
	}

	public void updateUrl() {
		List<VideoDTO> list = dao.getUrlNull();
		if (CollectionUtils.isNotEmpty(list)) {
			for (VideoDTO dataDTO : list) {
				String url = this.getClient(String.valueOf(dataDTO.getID()));
				if (url == null) {
					continue;
				}
				dataDTO.setUrl(url);
				dao.updateURL(dataDTO);
				System.err.println(DateUtils.date() + "更新一条成功：" + dataDTO.getID());
			}
		}
	}

	public void mainService() {
		GetListByTagRespDTO glbr = null;
		try {
			glbr = this.request(99999);
		} catch (Exception e1) {
			e1.printStackTrace();
		} // 获取列表
		List<VideoDTO> list = glbr.getData();
		if (CollectionUtils.isNotEmpty(list)) {
			try {
				this.insert(list);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private String zero(Integer startNo) {
		String no = String.valueOf(startNo);
		while (no.length() < 5) {
			no = "0" + no;
		}
		return no;
	}

	private void insert(List<VideoDTO> list) throws Exception {
		System.err.println("总数：" + list.size());
		for (int i = 0; i < list.size(); i++) {
			VideoDTO dataDTO = list.get(i);
//			dataDTO.setSeq(i + 1);
			if (this.hxcVideoExist(dataDTO.getID())) {
				dataDTO.setSeq(dao.nextValue() + 1);
				dao.add(dataDTO);
				System.err.println("插入一条成功：" + dataDTO.getID());
			} else {
				dao.update(dataDTO);
				System.err.println("更新一条成功：" + dataDTO.getID());
			}
		}
	}

	private boolean hxcVideoExist(Integer id) {
		return dao.videoExist(String.valueOf(id)) == 0 ? true : false;
	}

	private String getClient(String videoId) {
		try {
			String str = HttpUtils.post(Config.value("getClient"),
					"{\"videoID\":\"" + videoId + "\",\"userID\":2210195,\"ClientType\":5}", "127.0.0.1:1080",
					HeaderUtils.getClientHeader());
			Thread.sleep(1000);
			if (!JSON.isValid(str)) {
				return null;
			}
			JSONObject jo = JSON.parseObject(str).getJSONObject("data");
			if (jo == null) {
				return null;
			}
			return jo.getString("Url");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private GetListByTagRespDTO request(int rows) throws Exception {
		GetListByTagReqDTO glbt = new GetListByTagReqDTO(1, 10000, null, 0, this.getQueryParam());
		String str = HttpUtils.post(Config.value("getListByTag"), JSON.toJSONString(glbt), "127.0.0.1:1080",
				HeaderUtils.getListHeader());
		if (!JSON.isValid(str)) {
			throw new Exception("JSON格式错误，获取失败");
		}
		GetListByTagRespDTO glbr = JSON.parseObject(str, GetListByTagRespDTO.class);
		return glbr;
	}

	private List<OrdertextDTO> getQueryParam() {
		List<OrdertextDTO> list = new ArrayList<>();
		list.add(new OrdertextDTO("SeeCount", "desc"));
		return list;
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
