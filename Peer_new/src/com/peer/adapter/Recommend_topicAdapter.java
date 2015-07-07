package com.peer.adapter;

import java.util.List;
import java.util.Map;

import com.peer.activity.ChatRoomActivity;
import com.peer.activity.R;
import com.peer.activity.Recommend_topic;
import com.peer.base.pBaseActivity;
import com.peer.base.pBaseAdapter;
import com.peer.utils.ViewHolder;

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
	private List<Map> mList;
//	List<String> pre_labels ;   //获取登陆者label
	public Recommend_topicAdapter( Context mContext,List<Map> mList){
		
		super(mContext);
//		try {
//			pre_labels = PeerUI.getInstance().getISessionManager().getLabels();
//		} catch (RemoteException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		this.mContext=mContext;
		this.mList=mList;
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
					Map plist = (Map) mList.get(position);
					tv_time.setText((String) plist.get("sys_time"));
					tv_skill.setText((String) plist.get("label_name"));
					tv_topic.setText((String) plist.get("subject"));
					ll_click.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent intent=new Intent();
							((pBaseActivity)mContext).startActivityForLeft(ChatRoomActivity.class, intent, false);
							
						}
					});
		return convertView;
	}
	

}

