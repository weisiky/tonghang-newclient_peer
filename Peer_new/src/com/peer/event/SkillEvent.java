package com.peer.event;

public class SkillEvent {
	private int position;
	private String label;
	private boolean isdelete;
	public SkillEvent(){
		
	}
	public SkillEvent(int position,String label,boolean isdelete){
		this.position=position;
		this.label=label;
		this.isdelete=isdelete;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public boolean isIsdelete() {
		return isdelete;
	}
	public void setIsdelete(boolean isdelete) {
		this.isdelete = isdelete;
	}
	
}
