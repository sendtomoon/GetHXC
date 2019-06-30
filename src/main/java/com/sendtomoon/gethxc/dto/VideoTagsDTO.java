package com.sendtomoon.gethxc.dto;

import java.util.Date;

import org.apache.ibatis.type.Alias;

@Alias("videoTagsDTO")
public class VideoTagsDTO extends BaseDTO {

	public VideoTagsDTO(int videoId, int tagId) {
		this.videoId = videoId;
		this.tagId = tagId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVideoId() {
		return videoId;
	}

	public void setVideoId(int videoId) {
		this.videoId = videoId;
	}

	public int getTagId() {
		return tagId;
	}

	public void setTagId(int tagId) {
		this.tagId = tagId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	private static final long serialVersionUID = 2940317053560492537L;

	private int id;

	private int videoId;

	private int tagId;

	private Date createdDate;
}
