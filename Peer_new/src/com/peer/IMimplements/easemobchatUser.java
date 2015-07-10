package com.peer.IMimplements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

public class easemobchatUser implements Parcelable {
	
	private List<Map> easemobchatusers=new ArrayList<Map>();
	
	
	public easemobchatUser() {
	}
    
	public easemobchatUser(Parcel source) {
		// TODO Auto-generated constructor stub
		readFromParcel(source);
	}    
    
	public void readFromParcel(Parcel source) { 
		
		easemobchatusers=new ArrayList<Map>();
		source.readList(easemobchatusers, getClass().getClassLoader());
	}  	
	public static final Parcelable.Creator<easemobchatUser> CREATOR = new Parcelable.Creator<easemobchatUser>() {  
		  
        @Override  
        public easemobchatUser createFromParcel(Parcel source) {  
            return new easemobchatUser(source);  
        }  
  
        @Override  
        public easemobchatUser[] newArray(int size) {  
            return new easemobchatUser[size];  
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
		
	        dest.writeList(easemobchatusers);
	        
	}

	public List<Map> getEasemobchatusers() {
		return easemobchatusers;
	}

	public void setEasemobchatusers(List<Map> easemobchatusers) {
		this.easemobchatusers = easemobchatusers;
	}

}
