package com.sendtomoon.gethxc.servie;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sendtomoon.gethxc.DAO;
import com.sendtomoon.gethxc.dto.GetListByTagReqDTO;
import com.sendtomoon.gethxc.dto.GetListByTagRespDTO;
import com.sendtomoon.gethxc.dto.OrdertextDTO;
import com.sendtomoon.gethxc.dto.VideoDTO;
import com.sendtomoon.gethxc.dto.VideoTagsDTO;
import com.sendtomoon.gethxc.utils.DateUtils;
import com.sendtomoon.gethxc.utils.HeaderUtils;
import com.sendtomoon.gethxc.utils.HttpUtils;
import com.sendtomoon.gethxc.utils.LoginHxcUtils;

@Service
public class HXCInfoService {
	@Autowired
	private DAO dao;

	@Autowired
	private LoginHxcUtils login;

	@Value("${getListByTag}")
	String getListByTag;

	@Value("${proxyURL}")
	String proxyURL;

	@Value("${getClient}")
	String getClient;

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
			String token = login.getToken();
			System.err.println("总数：" + list.size());
			for (VideoDTO dataDTO : list) {
				String url = this.getClient(String.valueOf(dataDTO.getID()), token);
				if (url == null) {
					continue;
				}
				dataDTO.setUrl(url);
				dao.updateURL(dataDTO);
				System.err.println(DateUtils.date() + "：更新一条成功");
			}
		}
	}

	public void mainService() {
		for (int i = 0; i < 1; i++) {
			GetListByTagRespDTO glbr = null;
			try {
				glbr = this.request(29999, 0);
			} catch (Exception e1) {
				e1.printStackTrace();
			} // 获取列表
			if (glbr == null) {
				break;
			}
			List<VideoDTO> list = glbr.getData();
			if (CollectionUtils.isNotEmpty(list)) {
				try {
					this.insert(list);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				break;
			}
			try {
				Thread.sleep(1579);
			} catch (InterruptedException e) {
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
			if (this.hxcVideoExist(dataDTO.getID())) {
//				dataDTO.setSeq(dao.nextValue() + 1);
				dao.add(dataDTO);
				System.err.println("插入一条成功：" + dataDTO.getID());
			} else {
				dao.renewVideo(dataDTO);
				System.err.println("更新一条成功：" + dataDTO.getID());
			}
		}
	}

	private boolean hxcVideoExist(Integer id) {
		return dao.videoExist(String.valueOf(id)) == 0 ? true : false;
	}

	private String getClient(String videoId, String token) {
		try {
			Map<String, String> map = HeaderUtils.getClientHeader();
			map.put("Token", token);
			String str = HttpUtils.post(getClient,
					"{\"videoID\":\"" + videoId + "\",\"userID\":2210195,\"ClientType\":5}", proxyURL, map);
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

	private GetListByTagRespDTO request(int rows, int startNum) throws Exception {
		Map<String, String> map = HeaderUtils.getListHeader();
		map.put("Token", login.getToken());
		GetListByTagReqDTO glbt = new GetListByTagReqDTO(1, rows, null, startNum, this.getQueryParam());
		String str = HttpUtils.post(getListByTag, JSON.toJSONString(glbt), proxyURL, map);
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
