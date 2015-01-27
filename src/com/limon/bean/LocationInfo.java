package com.limon.bean;

import java.io.Serializable;

public class LocationInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5367754098471100238L;
	public String Guid;
	public String Addr;
	public String Lat;
	public String Lon;
	public String Tel;
	public String Name;
	public String Dist;
	public String Cate;
	public String Uid;

	public String getGuid() {
		return Guid;
	}

	public void setGuid(String guid) {
		Guid = guid;
	}

	public String getAddr() {
		return Addr;
	}

	public void setAddr(String addr) {
		Addr = addr;
	}

	public String getLat() {
		return Lat;
	}

	public void setLat(String lat) {
		Lat = lat;
	}

	public String getLon() {
		return Lon;
	}

	public void setLon(String lon) {
		Lon = lon;
	}

	public String getTel() {
		return Tel;
	}

	public void setTel(String tel) {
		Tel = tel;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getDist() {
		return Dist;
	}

	public void setDist(String dist) {
		Dist = dist;
	}

	public String getCate() {
		return Cate;
	}

	public void setCate(String cate) {
		Cate = cate;
	}

	public String getUid() {
		return Uid;
	}

	public void setUid(String uid) {
		Uid = uid;
	}

}
