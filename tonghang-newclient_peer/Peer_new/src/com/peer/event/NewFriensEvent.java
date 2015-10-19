package com.peer.event;

public class NewFriensEvent {
	private int position;
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public NewFriensEvent(){
		
	}
	public NewFriensEvent(int position){
		this.position=position;
	}
	
}
