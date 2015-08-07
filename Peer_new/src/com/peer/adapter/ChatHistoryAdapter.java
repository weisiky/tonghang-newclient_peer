package com.peer.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.peer.R;
import com.peer.activity.SingleChatRoomActivity;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.base.pBaseAdapter;
import com.peer.bean.ChatRoomBean;
import com.peer.bean.User;
import com.peer.bean.UserBean;
import com.peer.utils.ImageLoaderUtil;
import com.peer.utils.ViewHolder;
import com.readystatesoftware.viewbadger.BadgeView;
import com.tencent.mm.sdk.modelmsg.ShowMessageFromWX;

public class ChatHistoryAdapter extends pBaseAdapter {
	private Context mContext;
	private List<UserBean> userlist;
	public ChatHistoryAdapter(Context mContext,List<UserBean> mlist){
		super(mContext);
		this.mContext=mContext;
		this.userlist=mlist;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return userlist.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parentgroup) {
		// TODO Auto-generated method stub
		final UserBean user = userlist.get(position);
		System.out.println("是否为好友："+user.getIs_friend());
		EMConversation conversation = EMChatManager.getInstance()
				.getConversation(user.getClient_id());				
		convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_come_listperson,null,false);				
		ImageView im_headpic=(ImageView)convertView.findViewById(R.id.im_headpic);	
		TextView tv_nikename=(TextView)convertView.findViewById(R.id.tv_nikename);			
		TextView tv_descripe=(TextView)convertView.findViewById(R.id.tv_descripe);
		LinearLayout ll_clike=(LinearLayout)convertView.findViewById(R.id.ll_clike);
		ImageLoaderUtil.getInstance().showHttpImage(
				((pBaseActivity)mContext).mShareFileUtils.getString(Constant.PIC_SERVER, "")
				+user.getImage(),im_headpic,
				R.drawable.mini_avatar_shadow);
		tv_nikename.setText(user.getUsername());	
		EMMessage lastMessage = conversation.getLastMessage();
		TextMessageBody body=(TextMessageBody) lastMessage.getBody();
		tv_descripe.setText(body.getMessage());
		final BadgeView bd=new BadgeView(mContext, ll_clike);
		if (conversation.getUnreadMsgCount() > 0) {				
			bd.setText(String.valueOf(conversation.getUnreadMsgCount()));
			bd.show();
		}else{
			bd.hide();
		}
		ll_clike.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!((pBaseActivity)mContext).isNetworkAvailable){
					((pBaseActivity)mContext).showToast(mContext.getResources().getString(R.string.Broken_network_prompt), Toast.LENGTH_SHORT, false);
				}else{	
					bd.hide();					
					ChatRoomBean.getInstance().setChatroomtype(Constant.SINGLECHAT);
//					ChatRoomBean.getInstance().setUserBean(user);
					Intent intent=new Intent(mContext,SingleChatRoomActivity.class);
					intent.putExtra("userbean", user);
					mContext.startActivity(intent);
				}
			}
		});
		
		return convertView;
	}
}
