package com.limon.bean;

import java.io.Serializable;

public class ContactInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1702690658430590262L;
	public String Uid;
	public String Username;
	public String Contactid;
	public String Friendname;
	public String Friendphone;

	public String getUid() {
		return Uid;
	}

	public void setUid(String uid) {
		Uid = uid;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getContactid() {
		return Contactid;
	}

	public void setContactid(String contactid) {
		Contactid = contactid;
	}

	public String getFriendname() {
		return Friendname;
	}

	public void setFriendname(String friendname) {
		Friendname = friendname;
	}

	public String getFriendphone() {
		return Friendphone;
	}

	public void setFriendphone(String friendphone) {
		Friendphone = friendphone;
	}
}
