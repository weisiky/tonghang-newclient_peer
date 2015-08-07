package com.peer.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.peer.R;
import com.peer.activity.MultiChatRoomActivity;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.base.pBaseAdapter;
import com.peer.bean.ChatRoomBean;
import com.peer.bean.TopicBean;
import com.peer.bean.UserBean;
import com.peer.utils.ImageLoaderUtil;
import com.peer.utils.ViewHolder;

public class SearchTopicAdapter extends pBaseAdapter {
	private Context mContext;
	private List<TopicBean> mlist;
	private String pic_server;
	public SearchTopicAdapter(Context mContext,List<TopicBean> list,String pic_server){
		super(mContext);
		this.mContext=mContext;
		this.mlist=list;
		this.pic_server = pic_server;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.size();
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
		final TopicBean topicbean = mlist.get(position);
		convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_come_listtopic,null,false);
		TextView tv_time=ViewHolder.get(convertView, R.id.tv_time);
		TextView tv_skill=ViewHolder.get(convertView, R.id.tv_skill);			
		TextView tv_topic=ViewHolder.get(convertView, R.id.tv_topic);
		ImageView head = ViewHolder.get(convertView, R.id.head);
		// ImageLoader加载图片
				ImageLoaderUtil.getInstance().showHttpImage(
						pic_server + topicbean.getImage(), head,
						R.drawable.mini_avatar_shadow);
				
		LinearLayout ll_click = ViewHolder.get(convertView, R.id.ll_click);
		tv_time.setText(topicbean.getCreated_at());
		tv_skill.setText(topicbean.getLabel_name());
		tv_topic.setText(topicbean.getSubject());
		ll_click.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(((pBaseActivity)mContext).isNetworkAvailable){
					ChatRoomBean.getInstance().setChatroomtype(Constant.MULTICHAT);
					ChatRoomBean.getInstance().setTopicBean(topicbean);
					String ownerid=null;
					ownerid =((pBaseActivity)mContext).mShareFileUtils.getString(Constant.CLIENT_ID, "") ;	
					if(topicbean.getUser_id().equals(ownerid)){
						ChatRoomBean.getInstance().setIsowner(true);
					}else{
						ChatRoomBean.getInstance().setIsowner(false);
					}	
					Intent intent=new Intent(mContext,MultiChatRoomActivity.class);
					mContext.startActivity(intent);
				}else{
					((pBaseActivity)mContext).showToast(mContext.getResources()
							.getString(R.string.Broken_network_prompt), Toast.LENGTH_SHORT, false);
				}
				
			}
		});
		
		return convertView;
	}
}
