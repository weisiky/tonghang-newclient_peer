package com.peer.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class CardChangeBean implements Serializable{

	private UserBean userbean;

	private String type;
	private String date;
	private String name;
	private String pushid;
	private String image;
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPushid() {
		return pushid;
	}

	public void setPushid(String pushid) {
		this.pushid = pushid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public UserBean getUserbean() {
		return userbean;
	}

	public void setUserbean(UserBean userbean) {
		this.userbean = userbean;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


}
