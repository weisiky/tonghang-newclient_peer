package com.peer.IMimplements;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.peer.IMinterface.IM;
import com.peer.base.Constant;
import com.peer.utils.pLog;


/**
 * Encapsulation ring letter implementation method
 * 封装环信通信
 * */
public class easemobchatImp implements IM{
	private static easemobchatImp ringletter=null;
	private easemobchatImp(){
		
	}
	public static easemobchatImp getInstance(){
		if(ringletter==null){
			synchronized (easemobchatImp.class) {
				if(ringletter==null){
					ringletter=new easemobchatImp();
				}
			 }
		}		 
		return ringletter;
	}

	@Override
	public void login(String email, String password) {
		// TODO Auto-generated method stub
		
		pLog.i("test", "email:"+email);
		pLog.i("test", "password:"+password);
		
		EMChatManager.getInstance().login(email,password,new EMCallBack() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				 System.out.println("环信登录成功");
				
			}
			
			@Override
			public void onProgress(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
				pLog.i("test", "arg0:"+arg0);
				pLog.i("test", "arg1:"+arg1);
				
				System.out.println("环信登录错误---->");
			}
		});
	}
	@Override
	public void sendMessage(String content, int chattype,String targetId,String imageUrl,String userid) {
		// TODO Auto-generated method stub
		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
		// 如果是群聊，设置chattype,默认是单�?
		if (chattype == Constant.MULTICHAT)
			message.setChatType(ChatType.GroupChat);
		TextMessageBody txtBody = new TextMessageBody(content);
		// 设置消息body
		message.addBody(txtBody);
		//自定义扩展消息，用于头像
		message.setAttribute(Constant.IMAGEURL, imageUrl);
		//自定义扩展消息，用于携带用户Id
		message.setAttribute(Constant.USERID, userid);
		
		// 设置要发给谁,用户username或�?群聊groupid
		message.setReceipt(targetId);		
		try {
		    //发�?消息
			EMChatManager.getInstance().sendMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void setAppInited() {
		// TODO Auto-generated method stub
		// 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast�?
		EMChat.getInstance().setAppInited();
	}
	@Override
	public void loadConversationsandGroups() {
		// TODO Auto-generated method stub
		EMGroupManager.getInstance().loadAllGroups();
		EMChatManager.getInstance().loadAllConversations();
	}
	@Override
	public void clearConversation(String targetname) {
		// TODO Auto-generated method stub
		EMChatManager.getInstance().clearConversation(targetname);
	}
	@Override
	public int getUnreadMesTotal() {
		// TODO Auto-generated method stub
		int unreadMsgCountTotal = 0;
		unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
		return unreadMsgCountTotal;
	}
	@Override
	public void joingroup(String groupid) {
		// TODO Auto-generated method stub
		try {
			EMGroupManager.getInstance().joinGroup(groupid);
		} catch (EaseMobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void logout() {
		// TODO Auto-generated method stub
		EMChatManager.getInstance().logout();
	}
	@Override
	public void exitgroup(String groupid) {
		// TODO Auto-generated method stub
		try {
			EMGroupManager.getInstance().exitFromGroup(groupid);
		} catch (EaseMobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
