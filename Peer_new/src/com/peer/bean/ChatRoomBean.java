package com.peer.bean;



/**
 * chat room bean
 *
 */
public class ChatRoomBean {
	private int chatroomtype;
	
	private String topicId;
	private String title;
	private String userId;
	private String client_id;
	private String Image;
	private String theme;
	private String nike;
	
	private User user;
	private Topic topic;
	private boolean isowner;
	private static ChatRoomBean chatroomtypeutil;
	private ChatRoomBean(){}
	public static ChatRoomBean getInstance(){
		if(chatroomtypeutil==null){
			chatroomtypeutil=new ChatRoomBean();
		}
		return chatroomtypeutil;
	}
	public int getChatroomtype() {
		return chatroomtype;
	}
	public void setChatroomtype(int chatroomtype) {
		this.chatroomtype = chatroomtype;
	}

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
	
	public String getClient_Id() {
		return client_id;
	}
	public void setClient_Id(String client_id) {
		this.client_id = client_id;
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

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public boolean isIsowner() {
		return isowner;
	}
	public void setIsowner(boolean isowner) {
		this.isowner = isowner;
	}
	public Topic getTopic() {
		return topic;
	}
	public void setTopic(Topic topic) {
		this.topic = topic;
	}

}
