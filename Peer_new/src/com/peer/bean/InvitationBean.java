package com.peer.bean;

import java.io.Serializable;

public class InvitationBean implements Serializable{
	private String created_at;
	private String reason;
	private String invitee_id;
	private String inviter_id;
	public UserBean invitor;
	
	
	
	
	
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getInvitee_id() {
		return invitee_id;
	}
	public void setInvitee_id(String invitee_id) {
		this.invitee_id = invitee_id;
	}
	public String getInviter_id() {
		return inviter_id;
	}
	public void setInviter_id(String inviter_id) {
		this.inviter_id = inviter_id;
	}
	public UserBean getUserbean() {
		return invitor;
	}
	public void setUserbean(UserBean invitor) {
		this.invitor = invitor;
	}
	
}
