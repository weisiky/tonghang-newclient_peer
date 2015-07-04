package com.peer.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.peer.activity.PersonalPageActivity;
import com.peer.activity.R;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.base.pBaseAdapter;
import com.peer.bean.ChatMsgEntity;
import com.peer.bean.User;
import com.peer.utils.ViewHolder;
import com.peer.utils.pShareFileUtils;

/**
 *聊天室信息 Adapter
 * 
 */
public class ChatMsgViewAdapter extends pBaseAdapter {
	private List<User> list;
	TextView tvSendTime;
	TextView nick;
	TextView tvContent;
	ImageView heapic;
	
	public static interface IMsgViewType {
		int IMVT_COM_MSG = 1;
		int IMVT_TO_MSG = 0;
		int IMVT_OTH_MSG =2;
	}
	private Context context;
	private static final int ITEMCOUNT = 3;
	private List<ChatMsgEntity> coll;
	private LayoutInflater mInflater;
	
	public ChatMsgViewAdapter(Context context, List<ChatMsgEntity> coll) {
		super(context);
		this.coll = coll;
		this.context=context;
		mInflater = LayoutInflater.from(context);
//		LoadImageUtil.initImageLoader(context);
	}
	public int getCount() {
		return coll.size();
	}

	public Object getItem(int position) {
		return coll.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	
	public int getItemViewType(int position) {
		ChatMsgEntity entity = coll.get(position);

		if (entity.getMsgType()==1) {
			return IMsgViewType.IMVT_COM_MSG;
		} else if(entity.getMsgType()==0){
			return IMsgViewType.IMVT_TO_MSG;
		}else{  
			return IMsgViewType.IMVT_OTH_MSG;
		}
	}

	public int getViewTypeCount() {
		return ITEMCOUNT;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ChatMsgEntity entity = coll.get(position);
		final int isComMsg = entity.getMsgType();
		if (convertView == null) {
			if (isComMsg==0) {
				convertView = mInflater.inflate(
						R.layout.chatting_item_msg_text_left, null);
				tvSendTime = ViewHolder.get(convertView,R.id.tv_sendtime);
				nick=ViewHolder.get(convertView,R.id.nickname);
				tvContent = ViewHolder.get(convertView,R.id.tv_chatcontent);
				tvContent.setTextColor(context.getResources().getColor(R.color.white));
				heapic=ViewHolder.get(convertView,R.id.iv_ownerhead);
				
			} else if(isComMsg==1){
				convertView = mInflater.inflate(
						R.layout.chatting_item_msg_text_right, null);
				tvSendTime = ViewHolder.get(convertView,R.id.tv_sendtime);
				nick=ViewHolder.get(convertView,R.id.nickname);
				nick.setVisibility(View.GONE);
				tvContent = ViewHolder.get(convertView,R.id.tv_chatcontent);
				heapic=ViewHolder.get(convertView,R.id.iv_userhead);
			}
		} else {
			convertView.getTag();
		}
		
		/** 在第一次加载item时取得自己的好友列表（避免多次从server获取），以后用来判断某个用户是不是自己的好友 **/
		if(position==1){
//			FriendsTask task=new FriendsTask();
//			task.execute();
		}
		
//			LoadImageUtil.imageLoader.displayImage(entity.getImage(), viewHolder.heapic, LoadImageUtil.options);
			tvSendTime.setText(entity.getDate());			
			tvContent.setText(entity.getMessage());
			if(entity.getName()!=null){
				nick.setVisibility(View.VISIBLE);
				nick.setText(entity.getName());
			}else{
				nick.setVisibility(View.GONE);
			}
			final String userId=entity.getUserId();
			heapic.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(checkNetworkState()){
						if(isComMsg==0){
							if(list==null||list.isEmpty()){
								User user=new User();
								user.setUserid(userId);
								user.setIs_friends(false);
//								PersonpageUtil.getInstance().setUser(user);
							}else{
								for(int i=0;i<list.size();i++){
									if(userId.equals(list.get(i).getUserid())){
										 list.get(i).setIs_friends(true);					 
//										 PersonpageUtil.getInstance().setUser(list.get(i));
										 break;
									}else{
										User user=new User();
										user.setUserid(userId);
										user.setIs_friends(false);
//										PersonpageUtil.getInstance().setUser(user);
									}
								}
//								PersonpageUtil.getInstance().setShouldRefresh(true);
								Intent topersonalpage=new Intent(context,PersonalPageActivity.class);
								context.startActivity(topersonalpage);
							}
							
						}else{
//							ToMypersonalpage(context);
						}	
					}else{
						Toast.makeText(context, context.getResources().getString(R.string.Broken_network_prompt), 0).show();
					}
						
				}
			});			
		return convertView;
	}
//	private class FriendsTask extends AsyncTask<Void, Void, List<User>>{
//
//		@Override
//		protected List<User> doInBackground(Void... arg0) {
//			// TODO Auto-generated method stub       
//            try {
////            	list=PeerUI.getInstance().getISessionManager().myFriends();
//			} catch (RemoteException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}           
//			return list;
//		}
//		@Override
//		protected void onPostExecute(List<User> result) {
//			// TODO Auto-generated method stub
//		}
//	}
//	private void ToMypersonalpage(Context context) {
//		// TODO Auto-generated method stub
//		String userid=null,client_id=null;
//		List<String> labels=null;
//		try {
//			
//			client_id=pShareFileUtils.getString("client_id", "");
//	//次处未获取到标签。		labels=pShareFileUtils.getString("labels", "");
//		} catch (RemoteException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
///*
////		PersonpageUtil.getInstance().setPersonpagetype(Constant.OWNPAGE);
////		PersonpageUtil.getInstance().setPersonid(userid);
//*/			
//		LocalStorage.getString(context, Constant.EMAIL);
//		UserDao userdao=new UserDao(context);
//		UserBean userbean=userdao.findOne(LocalStorage.getString(context, Constant.EMAIL));
//		User user=new User();
//		user.setEmail(userbean.getEmail());
//		user.setBirthday(userbean.getAge());
//		user.setCity(userbean.getCity());
//		user.setSex(userbean.getSex());
//		user.setImage(userbean.getImage());
//		user.setUsername(userbean.getNikename());
//		user.setUserid(userid);
//		user.setClient_Id(client_id);
//		user.setLabels(labels);
//		PersonpageUtil.getInstance().setUser(user);
//		Intent topersonalpage=new Intent(context,PersonalPageActivity.class);
//		context.startActivity(topersonalpage);
//	}

}
