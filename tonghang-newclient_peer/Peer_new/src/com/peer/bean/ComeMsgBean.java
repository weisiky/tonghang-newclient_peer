package com.peer.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.easemob.chat.EMConversation;

public class ComeMsgBean implements Serializable{
	private List<EMConversation> emCoversationlist;

	public List<EMConversation> getEmCoversationlist() {
		return emCoversationlist;
	}

	public void setEmCoversationlist(List<EMConversation> emCoversationlist) {
		this.emCoversationlist = emCoversationlist;
	}
	
//	private ArrayList<GongzhognzhanghaoBean> gongList;
	

}
