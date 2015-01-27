package com.limon.bean;

import java.io.Serializable;

public class FriendInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6595732607859639023L;
	public String id;
	public int Sex;
	public String name;
	public String HeadImg;
	public String lon;
	public String lat;
	public String ispublic;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	// public String getTinyurl() {
	// return tinyurl;
	// }
	//
	// public void setTinyurl(String tinyurl) {
	// this.tinyurl = tinyurl;
	// }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getIspublic() {
		return ispublic;
	}

	public void setIspublic(String ispublic) {
		this.ispublic = ispublic;
	}

	public int getSex() {
		return Sex;
	}

	public void setSex(int sex) {
		Sex = sex;
	}

	public String getHeadImg() {
		return HeadImg;
	}

	public void setHeadImg(String headImg) {
		HeadImg = headImg;
	}

}
