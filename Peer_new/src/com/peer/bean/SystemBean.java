package com.peer.bean;

import java.io.Serializable;

/**
 * 服务器返回BEAN
 * 
 * @author zhangzg
 * 
 */

public class SystemBean implements Serializable {

	private String sys_time;
	private String etag;
	private String status;
	private String pic_server;
	private String code;
	private String token;
	
	public String getSys_time() {
		return sys_time;
	}
	public void setSys_time(String sys_time) {
		this.sys_time = sys_time;
	}
	public String getEtag() {
		return etag;
	}
	public void setEtag(String etag) {
		this.etag = etag;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPic_server() {
		return pic_server;
	}
	public void setPic_server(String pic_server) {
		this.pic_server = pic_server;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	

}
