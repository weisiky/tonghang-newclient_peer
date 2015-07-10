package com.peer.bean;

import java.io.Serializable;

public class PersonpageBean implements Serializable{
	
	private static PersonpageBean personpagebean;	
	public UserBean user;
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
	

}
