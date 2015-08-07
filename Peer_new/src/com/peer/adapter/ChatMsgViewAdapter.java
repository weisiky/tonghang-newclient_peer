package com.peer.adapter;

import java.util.ArrayList;
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

import com.peer.R;
import com.peer.activity.OtherPageActivity;
import com.peer.activity.PersonalPageActivity;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.base.pBaseAdapter;
import com.peer.bean.ChatMsgEntityBean;
import com.peer.bean.ChatRoomBean;
import com.peer.bean.PersonpageBean;
import com.peer.bean.User;
import com.peer.bean.UserBean;
import com.peer.utils.ImageLoaderUtil;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.ViewHolder;
import com.peer.utils.pLog;

/**
 *聊天室信息 Adapter
 * 
 */
public class ChatMsgViewAdapter extends pBaseAdapter {
	private List<UserBean> list;
	TextView tv_sendtime;
	TextView nickname;
	TextView tv_chatcontent;
	ImageView iv_ownerhead,iv_userhead;
	
	public static interface IMsgViewType {
		int IMVT_COM_MSG = 1;
		int IMVT_TO_MSG = 0;
		int IMVT_OTH_MSG =2;
	}
	private Context context;
	private static final int ITEMCOUNT = 3;
	private List<ChatMsgEntityBean> coll;
	private LayoutInflater mInflater;
	
	public ChatMsgViewAdapter(Context context, List<ChatMsgEntityBean> coll) {
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
		ChatMsgEntityBean entity = coll.get(position);

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
		final ChatMsgEntityBean entity = coll.get(position);
		final int isComMsg = entity.getMsgType();
			if (isComMsg==0) {  //为他人的对话框时
				convertView = mInflater.inflate(
						R.layout.chatting_item_msg_text_left, null);
				
				 tv_sendtime = ViewHolder.get(convertView,R.id.tv_sendtime);
				 nickname=ViewHolder.get(convertView,R.id.nickname);
				 tv_chatcontent = ViewHolder.get(convertView,R.id.tv_chatcontent);
				 tv_chatcontent.setTextColor(context.getResources().getColor(R.color.white));
				 iv_ownerhead=ViewHolder.get(convertView,R.id.iv_ownerhead);
				 final String userId=entity.getUserId();
				// ImageLoader加载图片
					ImageLoaderUtil.getInstance().showHttpImage(
							((pBaseActivity)context).mShareFileUtils.getString(Constant.PIC_SERVER, "")
							+entity.getImage() 
							, iv_ownerhead,
							R.drawable.mini_avatar_shadow);
				 iv_ownerhead.setOnClickListener(new View.OnClickListener() {
					 @Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							if(((pBaseActivity)context).isNetworkAvailable){
								UserBean userbean = entity.getUserbean();
								if(userbean != null ){
									pLog.i("test","bean不为空"+userbean);
									PersonpageBean.getInstance().setUser(userbean);
									Intent topersonalpage=new Intent(context,PersonalPageActivity.class);
									context.startActivity(topersonalpage);
								}else{
									pLog.i("test","bean为空");
									pLog.i("test","client_id:"+ entity.getUserId());
									Intent topersonalpage=new Intent(context,OtherPageActivity.class);
									topersonalpage.putExtra("client_id", entity.getUserId());
									context.startActivity(topersonalpage);
								}
								
							}else{
								((pBaseActivity)context).showToast(context.getResources().getString(R.string.Broken_network_prompt), Toast.LENGTH_SHORT, false);
							}
								
						}
						
					});
				
			} else if(isComMsg==1){  //为自己的对话框时
				convertView = mInflater.inflate(
						R.layout.chatting_item_msg_text_right, null);
				 tv_sendtime = ViewHolder.get(convertView,R.id.tv_sendtime);
				 nickname=ViewHolder.get(convertView,R.id.nickname);
				 nickname.setVisibility(View.GONE);
				 tv_chatcontent = ViewHolder.get(convertView,R.id.tv_chatcontent);
				 iv_userhead=ViewHolder.get(convertView,R.id.iv_userhead);
				 final String userId=entity.getUserId();
				// ImageLoader加载图片
					ImageLoaderUtil.getInstance().showHttpImage(
							((pBaseActivity)context).mShareFileUtils.getString(Constant.PIC_SERVER, "")
							+entity.getImage() 
							, iv_userhead,
							R.drawable.mini_avatar_shadow);
				 iv_userhead.setOnClickListener(new View.OnClickListener() {
					 @Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							if(((pBaseActivity)context).isNetworkAvailable){
								UserBean user = new UserBean();
								ArrayList<String> labels = JsonDocHelper.toJSONArrary(
										((pBaseActivity)context).mShareFileUtils.getString(Constant.LABELS, ""), String.class);
								user.setBirth(((pBaseActivity)context).mShareFileUtils.getString(Constant.BIRTH, ""));
								user.setCity(((pBaseActivity)context).mShareFileUtils.getString(Constant.CITY, ""));
								user.setClient_id(((pBaseActivity)context).mShareFileUtils.getString(Constant.CLIENT_ID, ""));
								user.setCreated_at(((pBaseActivity)context).mShareFileUtils.getString(Constant.CREATED_AT, ""));
								user.setEmail(((pBaseActivity)context).mShareFileUtils.getString(Constant.EMAIL, ""));
								user.setImage(((pBaseActivity)context).mShareFileUtils.getString(Constant.IMAGE, ""));
								user.setLabels(labels);
								user.setSex(((pBaseActivity)context).mShareFileUtils.getString(Constant.SEX, ""));
								user.setUsername(((pBaseActivity)context).mShareFileUtils.getString(Constant.USERNAME, ""));
								PersonpageBean.getInstance().setUser(user);
								Intent topersonalpage=new Intent(context,PersonalPageActivity.class);
								context.startActivity(topersonalpage);
							}else{
								Toast.makeText(context, context.getResources().getString(R.string.Broken_network_prompt), 0).show();
							}
								
						}
						
					});
			}
		
		tv_sendtime.setText(entity.getDate());			
		tv_chatcontent.setText(entity.getMessage());
			if(entity.getName()!=null){
				nickname.setVisibility(View.VISIBLE);
				nickname.setText(entity.getName());
			}else{
				nickname.setVisibility(View.GONE);
			}
		return convertView;
	}

}
