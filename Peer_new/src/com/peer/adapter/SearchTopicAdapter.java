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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.peer.activity.R;
import com.peer.base.pBaseAdapter;
import com.peer.bean.TopicBean;
import com.peer.bean.UserBean;
import com.peer.utils.ViewHolder;

public class SearchTopicAdapter extends pBaseAdapter {
	private Context mContext;
	private List<TopicBean> mlist;
	public SearchTopicAdapter(Context mContext,List<TopicBean> list){
		super(mContext);
		this.mContext=mContext;
		this.mlist=list;
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
		LinearLayout ll_click = ViewHolder.get(convertView, R.id.ll_click);
//		Map plist = (Map) mlist.get(position);
		tv_time.setText(topicbean.getCreated_at());
		tv_skill.setText(topicbean.getLabel_name());
		tv_topic.setText(topicbean.getSubject());
		
		return convertView;
	}
}
