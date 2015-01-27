package com.limon.bean;

import java.io.Serializable;

public class UserInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2281315196823450717L;
	public String Id;
	public String Uname;
	public String Email;
	public String Psw;
	public String Headimg;
	public String Isfriend;
	public int Credit;
	public int Experience;
	public int Sex;
	public String lon;
	public String lat;
	public String weibouid;
	public String token;
	public String tsecret;
	public String weibotype;
	public String Phone;

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getUname() {
		return Uname;
	}

	public void setUname(String uname) {
		Uname = uname;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getPsw() {
		return Psw;
	}

	public void setPsw(String psw) {
		Psw = psw;
	}

	public String getHeadimg() {
		return Headimg;
	}

	public void setHeadimg(String headImg) {
		Headimg = headImg;
	}

	public int getCredit() {
		return Credit;
	}

	public void setCredit(int credit) {
		Credit = credit;
	}

	public int getExperience() {
		return Experience;
	}

	public void setExperience(int experience) {
		Experience = experience;
	}

	public int getSex() {
		return Sex;
	}

	public void setSex(int sex) {
		Sex = sex;
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

	// private int badgeCount;
	// private String bindFlag;
	// private String email;
	// private int friendRequestCount;
	// private long gold;
	// private int houseCount;
	// private int houseCountMax;
	// private String information;
	// private int itemCount;
	// private String largeImageUrl;
	// private long lastLevelPoints;
	// private String latestVisit;
	// private int level;
	// private String middleImageUrl;
	// private long nextLevelPoints;
	// private String phone;
	// private int propertyCount;
	// private Role role;
	// private long totalPoints;
	// private int unReadPmCount;
	// private UserType userType;
	// private int venueCount;
	// private int vipCount;
	public String getIsfriend() {
		return Isfriend;
	}

	public void setIsfriend(String isfriend) {
		Isfriend = isfriend;
	}

	public String getWeibouid() {
		return weibouid;
	}

	public void setWeibouid(String weibouid) {
		this.weibouid = weibouid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTsecret() {
		return tsecret;
	}

	public void setTsecret(String tsecret) {
		this.tsecret = tsecret;
	}

	public String getWeibotype() {
		return weibotype;
	}

	public void setWeibotype(String weibotype) {
		this.weibotype = weibotype;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}
}