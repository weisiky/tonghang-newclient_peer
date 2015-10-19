package com.peer.bean;

import java.io.Serializable;

/**
 * 登录返回结果BEAN
 * 
 * @author zhangzg
 * 
 */

public class LoginBean implements Serializable {

	private String pic_server;
	private String sys_time;
	private String code;
	private static LoginBean loginbean;
	public UserBean user = new UserBean();
	
	public static LoginBean getInstance(){
		if(loginbean==null){
			loginbean=new LoginBean();
		}		
		return loginbean;
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
