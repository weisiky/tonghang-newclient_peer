package com.peer.IMinterface;

import java.io.File;

/**
 *环信 
 * 
 */
public interface IM {
	
	public void login(String email,String password);
	
	public void logout();
	
	public void setAppInited();
	
	public void sendMessage(String content,int chattype,String targetId,String imageUrl);
	
	public void sendImgMessage(File newfile,String targetId,String imageUrl);
	
	public void clearConversation(String targetname);
	
	public void loadConversationsandGroups();
	
	public int getUnreadMesTotal();
	
	public void joingroup(String groupid);
	
	public void exitgroup(String groupid);
}
