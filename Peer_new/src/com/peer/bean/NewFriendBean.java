package com.peer.bean;

import java.io.Serializable;
import java.util.List;

public class NewFriendBean implements Serializable{
	private String pic_server;
	private String sys_time;
	private String code;
	public List<InvitationBean> invitation;
	
	public static NewFriendBean newfriendbean;
	
	public static NewFriendBean getInstance(){
		if(newfriendbean==null){
			newfriendbean=new NewFriendBean();
		}		
		return newfriendbean;
	}
	
	public List<InvitationBean> getInvitationbean() {
		return invitation;
	}

	public void setInvitationbean(List<InvitationBean> invitation) {
		this.invitation = invitation;
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
