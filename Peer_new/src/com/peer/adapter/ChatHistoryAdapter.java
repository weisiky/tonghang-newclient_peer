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
import com.peer.activity.MultiChatRoomActivity;
import com.peer.activity.SingleChatRoomActivity;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.base.pBaseAdapter;
import com.peer.bean.ChatRoomBean;
import com.peer.bean.JoinTopicBean;
import com.peer.bean.TopicBean;
import com.peer.bean.User;
import com.peer.bean.UserBean;
import com.peer.utils.ImageLoaderUtil;
import com.peer.utils.ViewHolder;
import com.readystatesoftware.viewbadger.BadgeView;
import com.tencent.mm.sdk.modelmsg.ShowMessageFromWX;

public class ChatHistoryAdapter extends pBaseAdapter {
	private Context mContext;
	private List<Object> userlist;
	public ChatHistoryAdapter(Context mContext,List<Object> mlist){
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
		Map map = (Map) userlist.get(position);
		if(map.get("type").equals(Constant.SINGLECHAT)){
			final UserBean user = (UserBean) map.get("bean");
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
					bd.hide();					
					ChatRoomBean.getInstance().setChatroomtype(Constant.SINGLECHAT);
					Intent intent=new Intent(mContext,SingleChatRoomActivity.class);
					intent.putExtra("userbean", user);
					mContext.startActivity(intent);
				}
			});
			
		}else{
			final JoinTopicBean comMesTopicBean = (JoinTopicBean) map.get("bean");
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_come_listtopic,null,false);
			TextView tv_skill=(TextView)convertView.findViewById(R.id.tv_skill);			
			TextView tv_topic=(TextView)convertView.findViewById(R.id.tv_topic);
			TextView tv_time=(TextView)convertView.findViewById(R.id.tv_time);				
			LinearLayout ll_clike=(LinearLayout)convertView.findViewById(R.id.ll_clike);
			EMConversation conversation = EMChatManager.getInstance().getConversation(comMesTopicBean.getTopic_id());
			tv_time.setText(comMesTopicBean.getCreated_at());
			tv_topic.setText(comMesTopicBean.getLabel_name());
			tv_topic.setText(comMesTopicBean.getSubject());	
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
					if(((pBaseActivity)mContext).isNetworkAvailable){
						((pBaseActivity)mContext).showToast(mContext.getResources()
								.getString(R.string.Broken_network_prompt),Toast.LENGTH_SHORT, false);
					}else{							
						ChatRoomBean.getInstance().setChatroomtype(Constant.MULTICHAT);	
						UserBean userbean = comMesTopicBean.getUserbean();
						String ownerid=((pBaseActivity)mContext)
								.mShareFileUtils.getString(Constant.CLIENT_ID, "");
						if(userbean.getClient_id().equals(ownerid)){
							ChatRoomBean.getInstance().setIsowner(true);
						}else{
							ChatRoomBean.getInstance().setIsowner(false);
						}
						TopicBean topicbean = new TopicBean();
						topicbean.setCreated_at(comMesTopicBean.getCreated_at());
						topicbean.setImage(comMesTopicBean.getUserbean().getImage());
						topicbean.setLabel_name(comMesTopicBean.getLabel_name());
						topicbean.setSubject(comMesTopicBean.getSubject());
						topicbean.setUser_id(comMesTopicBean.getClient_id());
						topicbean.setUsername(comMesTopicBean.getUserbean().getUsername());
						ChatRoomBean.getInstance().setTopicBean(topicbean);
						Intent intent=new Intent(mContext,MultiChatRoomActivity.class);
						mContext.startActivity(intent);
					}
				}
			});
		}
		
		return convertView;
	}
}
