package com.peer.bean;

import java.util.ArrayList;

public class JpushBean {

	// {"type":"0","id":"1a3c580c0766f357b907852e8601a0f20f7248ff","name":"nightmare"}
//	jpush:{"id":"0a0f59b6b6032185c38542e6925f684aeba1dc58","type":"0","message":"rpgmakervx请求加好友","name":"rpgmakervx"}
// jpush:{"id":"2cb690a880964a54be8986af55331f8c","type":"3","message":"sin请求加好友","name":"sin"}

	private String type;
	private String id;
	private String name;
	private String message;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
