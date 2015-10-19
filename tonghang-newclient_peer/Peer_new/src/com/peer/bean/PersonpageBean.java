package com.peer.bean;

import java.io.Serializable;

public class PersonpageBean implements Serializable{
	
	private static PersonpageBean personpagebean;	
	private UserBean user;
	private String client_id;  //用于群聊界面查看用户信息扩展字段。
	
	private PersonpageBean(){
		
	}
	public static PersonpageBean getInstance(){
		if(personpagebean==null){
			personpagebean=new PersonpageBean();
		}		
		return personpagebean;
	}
	
	public UserBean getUser() {
		return user;
	}

	public void setUser(UserBean user) {
		this.user = user;
	}
	
	public String getClient_id() {
		return client_id;
	}
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

}
