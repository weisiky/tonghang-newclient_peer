package com.peer.utils;

import com.peer.bean.TopicBean;
import com.peer.bean.User;
import com.peer.bean.UserBean;


/**
 * chatroom type util 
 * @author Concoon-break
 *
 */
public class ChatRoomTypeUtil {
	private int chatroomtype;
	/*
	private String topicId;
	private String title;
	private String userId;
	private String huanxingId;
	private String Image;
	private String theme;
	private String nike;
	*/
	private UserBean user;
	private TopicBean topic;
	private boolean isowner;
	private static ChatRoomTypeUtil chatroomtypeutil;
	private ChatRoomTypeUtil(){}
	public static ChatRoomTypeUtil getInstance(){
		if(chatroomtypeutil==null){
			chatroomtypeutil=new ChatRoomTypeUtil();
		}
		return chatroomtypeutil;
	}
	public int getChatroomtype() {
		return chatroomtype;
	}
	public void setChatroomtype(int chatroomtype) {
		this.chatroomtype = chatroomtype;
	}
	/*
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getImage() {
		return Image;
	}
	public void setImage(String image) {
		Image = image;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	public String getNike() {
		return nike;
	}
	public void setNike(String nike) {
		this.nike = nike;
	}
	
	public String getHuanxingId() {
		return huanxingId;
	}
	public void setHuanxingId(String huanxingId) {
		this.huanxingId = huanxingId;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTopicId() {
		return topicId;
	}
	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}
	*/
	public UserBean getUser() {
		return user;
	}
	public void setUser(UserBean user) {
		this.user = user;
	}
	public boolean isIsowner() {
		return isowner;
	}
	public void setIsowner(boolean isowner) {
		this.isowner = isowner;
	}
	public TopicBean getTopic() {
		return topic;
	}
	public void setTopic(TopicBean topic) {
		this.topic = topic;
	}

}
