package com.peer.bean;

import java.io.Serializable;

public class JoinTopicBean implements Serializable {
	private String pic_server;
	private String sys_time;
	private String code;

	private UserBean userbean;
	private String username;
	private String image;
	
	private String created_at;
	/** 创建话题者的client_id **/
	private String user_id;
	private String label_name;
	private String subject;
	private String topic_id;

	private boolean Isower;

	public static JoinTopicBean jointopicbean;

	public static JoinTopicBean getInstance() {
		if (jointopicbean != null) {
			jointopicbean = new JoinTopicBean();
		}

		return jointopicbean;

	}

	public boolean pdower() {
		if (userbean.getClient_id().equals(user_id)) {
			Isower = true;
		} else {
			Isower = false;
		}

		return Isower;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}


	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getLabel_name() {
		return label_name;
	}

	public void setLabel_name(String label_name) {
		this.label_name = label_name;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTopic_id() {
		return topic_id;
	}

	public void setTopic_id(String topic_id) {
		this.topic_id = topic_id;
	}

	public UserBean getUserbean() {
		return userbean;
	}

	public void setUserbean(UserBean userbean) {
		this.userbean = userbean;
	}

	public boolean getIsower() {
		return pdower();
	}

	public String getPic_server() {
		return pic_server;
	}

	public void setPic_server(String pic_server) {
		this.pic_server = pic_server;
	}

	public String getSys_time() {
		return sys_time;
	}

	public void setSys_time(String sys_time) {
		this.sys_time = sys_time;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
