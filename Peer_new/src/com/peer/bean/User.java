package com.peer.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
	private String email;
	private String username;
	private String userid;
	private String password;
	private String image;
	private String sex;
	private String birthday;
	private String city;
	private String client_id;
	private boolean is_friends;
	private List<String> labels=new ArrayList<String>();
	//好友请求专用属性
	private String reason;	
	private String invitionid;
	public User() {
	}
    
	public User(Parcel source) {
		// TODO Auto-generated constructor stub
		readFromParcel(source);
	}    
    
	public void readFromParcel(Parcel source) { 
		userid = source.readString();
		email=source.readString();
		username = source.readString();
		image = source.readString();
		sex = source.readString();
		birthday = source.readString();
		city = source.readString();
		password=source.readString();
		reason=source.readString();
		invitionid=source.readString();
		client_id=source.readString();
		labels=new ArrayList<String>();
		source.readList(labels, getClass().getClassLoader());
	}  	
	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {  
		  
        @Override  
        public User createFromParcel(Parcel source) {  
            return new User(source);  
        }  
  
        @Override  
        public User[] newArray(int size) {  
            return new User[size];  
        }  
    };  	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(email);
		 dest.writeString(userid);
		 dest.writeString(password);
	        dest.writeString(username);
	        dest.writeString(image);
	        dest.writeString(sex);
	        dest.writeString(birthday);
	        dest.writeString(city);
	        dest.writeList(labels);
	        dest.writeString(reason);
	        dest.writeString(invitionid);
	        dest.writeString(client_id);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getInvitionid() {
		return invitionid;
	}

	public void setInvitionid(String invitionid) {
		this.invitionid = invitionid;
	}

	public String getClient_Id() {
		return client_id;
	}

	public void setClient_Id(String client_id) {
		this.client_id = client_id;
	}

	public boolean isIs_friends() {
		return is_friends;
	}

	public void setIs_friends(boolean is_friends) {
		this.is_friends = is_friends;
	}
}
