package com.peer.bean;

import java.io.Serializable;

/**
 * 登录返回结果BEAN
 * 
 * @author zhangzg
 * 
 */

public class LoginBean implements Serializable {

	// 用户昵称
	private String userName;
	// 用户邮箱
	private String email;
	// 用户密码
	private String password;
	
	// 用户年龄
	private String age;
	// 用户城市
	private String city;
	// 用户头像
	private String image;
	// 用户性别
	private String sex;
	
	public LoginBean(){
		
	}
	
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	

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
