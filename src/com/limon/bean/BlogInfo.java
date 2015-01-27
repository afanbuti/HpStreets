package com.limon.bean;

import java.io.Serializable;

public class BlogInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9037362164030491009L;
	public String Blogid;
	public String Title;
	public String Content;
	public String Uid;
	public String Username;
	public String Pic;
	public String From;
	public String Mid;

	public String getBlogid() {
		return Blogid;
	}

	public void setBlogid(String blogid) {
		Blogid = blogid;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

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

	public String getPic() {
		return Pic;
	}

	public void setPic(String pic) {
		Pic = pic;
	}

	public String getFrom() {
		return From;
	}

	public void setFrom(String from) {
		From = from;
	}

	public String getMid() {
		return Mid;
	}

	public void setMid(String mid) {
		Mid = mid;
	}
}
