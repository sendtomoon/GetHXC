package com.sendtomoon.gethxc;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sendtomoon.gethxc.dto.VideoDTO;
import com.sendtomoon.gethxc.dto.VideoTagsDTO;

@Repository
public interface DAO {
	public int add(VideoDTO dto);

	public List<VideoDTO> getWaitDown();
	
	public List<VideoDTO> getHxcVideo();

	public int update(VideoDTO dto);
	
	public int updateURL(VideoDTO dto);
	
	public int updateSeq(VideoDTO dto);

	public VideoDTO getDTOById(int id);

	public int nextValue();

	public String getTagId(String tag);

	public void insertTag(String tag);

	public void insertTagRelate(VideoTagsDTO fto);
	
	public void delTagRelated(String videoId);
	
	public List<VideoDTO> getUrlNull();
}
