package com.peer.bean;

import java.io.Serializable;

/**
 * 登录返回结果BEAN
 * 
 * @author zhangzg
 * 
 */

public class LoginResultBean implements Serializable {

	private String pic_server;
	private String sys_time;
	private String code;

	private UserBean userBean;

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

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

}
