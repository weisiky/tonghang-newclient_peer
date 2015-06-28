package com.peer.bean;

import java.io.Serializable;

/**
 * 登录返回结果BEAN
 * 
 * @author zhangzg
 * 
 */

public class LoginBean implements Serializable {

	// 用户名
	private String userName;
	// 用户邮箱
	private String email;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
