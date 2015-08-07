package com.peer.bean;

/**
 * 聊天msg Bean
 * 
 * @author way
 * 
 */
public class ChatMsgEntityBean {
	private String userId;
	private String image;
	private String name;//消息来自
	private String date;//消息日期
	private String message;//消息内容
	private int isComMeg;//收到哪种消息  1为我，0为其他人
	private UserBean userbean;
	
	

	private static ChatMsgEntityBean chatmsgentitybean;
	
	public static ChatMsgEntityBean getInstance(){
		if(chatmsgentitybean==null){
			chatmsgentitybean=new ChatMsgEntityBean();
		}
		return chatmsgentitybean;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getMsgType() {
		return isComMeg;
	}

	public void setMsgType(int isComMsg) {
		isComMeg = isComMsg;
	}

	public ChatMsgEntityBean() {
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public UserBean getUserbean() {
		return userbean;
	}

	public void setUserbean(UserBean userbean) {
		this.userbean = userbean;
	}

	public ChatMsgEntityBean(String name, String date, String text, int isComMsg,String userId) {
		super();
		this.name = name;
		this.date = date;
		this.message = text;
		this.isComMeg = isComMsg;
		this.userId=userId;
	}

}
