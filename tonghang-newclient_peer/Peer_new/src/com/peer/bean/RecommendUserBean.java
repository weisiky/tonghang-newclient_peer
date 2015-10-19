package com.peer.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RecommendUserBean implements Serializable{
	private String pic_server;
	private String sys_time;
	private String code;
	public List<UserBean> users = new ArrayList<UserBean>();
	private ArrayList<String> self_labels = new ArrayList<String>();
	
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
	
	public ArrayList<String> getSelf_labels() {
		return self_labels;
	}

	public void setSelf_labels(ArrayList<String> self_labels) {
		this.self_labels = self_labels;
	}
}
