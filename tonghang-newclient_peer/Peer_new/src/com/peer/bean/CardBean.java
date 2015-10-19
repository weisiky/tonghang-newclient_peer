package com.peer.bean;

import java.io.Serializable;

public class CardBean implements Serializable{
	private String companyname;
	private String position;
	private String work_date;
	private String realname;
	private String email;
	private String phone;
	private String schoolname;
	private String major;
	private String diploma;
	private String school_date;
	private String exchange_times;
	private String chat_times;
	
	
	
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getExchange_times() {
		return exchange_times;
	}
	public void setExchange_times(String exchange_times) {
		this.exchange_times = exchange_times;
	}
	public String getChat_times() {
		return chat_times;
	}
	public void setChat_times(String chat_times) {
		this.chat_times = chat_times;
	}
	
	
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getWork_date() {
		return work_date;
	}
	public void setWork_date(String work_date) {
		this.work_date = work_date;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getSchoolname() {
		return schoolname;
	}
	public void setSchoolname(String schoolname) {
		this.schoolname = schoolname;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getDiploma() {
		return diploma;
	}
	public void setDiploma(String diploma) {
		this.diploma = diploma;
	}
	public String getSchool_date() {
		return school_date;
	}
	public void setSchool_date(String school_date) {
		this.school_date = school_date;
	}
	
	

}
