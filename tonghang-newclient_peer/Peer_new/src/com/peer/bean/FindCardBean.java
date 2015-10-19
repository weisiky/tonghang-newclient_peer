package com.peer.bean;

import java.io.Serializable;

public class FindCardBean implements Serializable{
	private String pic_server;
	private String sys_time;
	private String code;
	private String message;
	
	public CardBean card = new CardBean();
	public UserBean user = new UserBean();
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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
