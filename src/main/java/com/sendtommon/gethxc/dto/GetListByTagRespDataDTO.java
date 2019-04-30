package com.sendtommon.gethxc.dto;

import java.util.Date;

import org.bson.types.ObjectId;

public class GetListByTagRespDataDTO extends BaseDTO {

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	private static final long serialVersionUID = 6538968052211907858L;
	private ObjectId id;
	private Date AddTime;
	private Integer CollectionCount;
	private String CoverImgUrl;
	private Integer Disabled;
	private Integer ID;
	private Integer IsHot;
	private Integer IsNeedLogin;
	private Integer IsRecomend;
	private Integer IsVip;
	private Integer Length;
	private String Name;
	private String No;
	private Integer Point;
	private Integer SeeCount;
	private String Tags;
	private Integer TypeID;
	private String TypeName;
	private String Url;
	/** 是否已下载过 0否 1是 */
	private Integer downloaded = 0;
	private String fileName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getDownloaded() {
		return downloaded;
	}

	public void setDownloaded(Integer downloaded) {
		this.downloaded = downloaded;
	}

	public Date getAddTime() {
		return AddTime;
	}

	public void setAddTime(Date addTime) {
		AddTime = addTime;
	}

	public Integer getCollectionCount() {
		return CollectionCount;
	}

	public void setCollectionCount(Integer collectionCount) {
		CollectionCount = collectionCount;
	}

	public String getCoverImgUrl() {
		return CoverImgUrl;
	}

	public void setCoverImgUrl(String coverImgUrl) {
		CoverImgUrl = coverImgUrl;
	}

	public Integer getDisabled() {
		return Disabled;
	}

	public void setDisabled(Integer disabled) {
		Disabled = disabled;
	}

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public Integer getIsHot() {
		return IsHot;
	}

	public void setIsHot(Integer isHot) {
		IsHot = isHot;
	}

	public Integer getIsNeedLogin() {
		return IsNeedLogin;
	}

	public void setIsNeedLogin(Integer isNeedLogin) {
		IsNeedLogin = isNeedLogin;
	}

	public Integer getIsRecomend() {
		return IsRecomend;
	}

	public void setIsRecomend(Integer isRecomend) {
		IsRecomend = isRecomend;
	}

	public Integer getIsVip() {
		return IsVip;
	}

	public void setIsVip(Integer isVip) {
		IsVip = isVip;
	}

	public Integer getLength() {
		return Length;
	}

	public void setLength(Integer length) {
		Length = length;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getNo() {
		return No;
	}

	public void setNo(String no) {
		No = no;
	}

	public Integer getPoint() {
		return Point;
	}

	public void setPoint(Integer point) {
		Point = point;
	}

	public Integer getSeeCount() {
		return SeeCount;
	}

	public void setSeeCount(Integer seeCount) {
		SeeCount = seeCount;
	}

	public String getTags() {
		return Tags;
	}

	public void setTags(String tags) {
		Tags = tags;
	}

	public Integer getTypeID() {
		return TypeID;
	}

	public void setTypeID(Integer typeID) {
		TypeID = typeID;
	}

	public String getTypeName() {
		return TypeName;
	}

	public void setTypeName(String typeName) {
		TypeName = typeName;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

}
