package com.sendtomoon.gethxc;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sendtomoon.gethxc.dto.VideoDTO;

@Repository
public interface DAO {
	public int add(VideoDTO dto);

	public List<VideoDTO> getWaitDown();

	public int update(VideoDTO dto);

	public VideoDTO getDTOById(int id);

	public int nextValue();

}
