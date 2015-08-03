package com.peer.adapter;

import java.util.List;
import java.util.Map;

import com.peer.activity.ChatRoomActivity;
import com.peer.activity.R;
import com.peer.activity.Recommend_topic;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.base.pBaseAdapter;
import com.peer.base.pBaseFragment;
import com.peer.bean.ChatRoomBean;
import com.peer.bean.TopicBean;
import com.peer.utils.ImageLoaderUtil;
import com.peer.utils.ViewHolder;
import com.peer.utils.pLog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.RemoteException;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Recommend_topicAdapter extends pBaseAdapter {
	private Context mContext;
	private List<TopicBean> mList;
	private String pic_server;
	private pBaseFragment baseFragment;
	public Recommend_topicAdapter( Context mContext,List<TopicBean> mList,String pic_server){
		
		super(mContext);
		this.mContext=mContext;
		this.mList=mList;
		this.pic_server = pic_server;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}	
	@Override
	public View getView(int position, View convertView, ViewGroup parentgroup) {
		
		// TODO Auto-generated method stub
					
		
					convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_come_listtopic,null,false);
					TextView tv_time=ViewHolder.get(convertView, R.id.tv_time);
					TextView tv_skill=ViewHolder.get(convertView, R.id.tv_skill);			
					TextView tv_topic=ViewHolder.get(convertView, R.id.tv_topic);
					ImageView head = ViewHolder.get(convertView, R.id.head);
					
					LinearLayout ll_click = ViewHolder.get(convertView, R.id.ll_click);
					final TopicBean topicbean =  mList.get(position);
					// ImageLoader加载图片
					ImageLoaderUtil.getInstance().showHttpImage(
							pic_server + topicbean.getImage(), head,
							R.drawable.mini_avatar_shadow);
					
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
								Intent intent=new Intent(mContext,ChatRoomActivity.class);
								mContext.startActivity(intent);
							}else{
								((pBaseActivity)mContext).showToast(mContext.getResources()
										.getString(R.string.Broken_network_prompt), Toast.LENGTH_SHORT, false);
							}
							
						}
					});
		return convertView;
	}
	
	/**
	 * 设置页面fragment
	 * 
	 * @param baseFragment
	 */
	public void setBaseFragment(pBaseFragment baseFragment) {
		this.baseFragment = baseFragment;
	}
}

