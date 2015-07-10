package com.peer.bean;

import java.io.Serializable;

public class CreateToipcBean implements Serializable{

	private String pic_server;
	private String sys_time;
	private String code;
	public TopicBean topics;
	
	private static CreateToipcBean createtopicbean;	
	
	public static CreateToipcBean getInstance(){
		if(createtopicbean==null){
			createtopicbean=new CreateToipcBean();
		}		
		return createtopicbean;
	}
	
	public TopicBean getTopic() {
		return topics;
	}

	public void setTopic(TopicBean topics) {
		this.topics = topics;
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
