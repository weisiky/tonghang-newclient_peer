package com.peer.bean;



/**
 * chat room bean
 *
 */
public class ChatRoomBean {
	private int chatroomtype;
	private boolean isowner;	
	
	private UserBean user;
	private TopicBean topic;
	
	private static ChatRoomBean chatroombean;
	private ChatRoomBean(){}
	public static ChatRoomBean getInstance(){
		if(chatroombean==null){
			chatroombean=new ChatRoomBean();
		}
		return chatroombean;
	}
	public int getChatroomtype() {
		return chatroomtype;
	}
	public void setChatroomtype(int chatroomtype) {
		this.chatroomtype = chatroomtype;
	}


	public UserBean getUserBean() {
		return user;
	}
	public void setUserBean(UserBean user) {
		this.user = user;
	}
	public boolean isIsowner() {
		return isowner;
	}
	public void setIsowner(boolean isowner) {
		this.isowner = isowner;
	}
	public TopicBean getTopicBean() {
		return topic;
	}
	public void setTopicBean(TopicBean topic) {
		this.topic = topic;
	}

}
