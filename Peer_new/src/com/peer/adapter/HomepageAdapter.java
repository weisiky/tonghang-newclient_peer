package com.peer.adapter;

import java.util.List;
import java.util.Map;

import com.peer.activity.PersonalPageActivity;
import com.peer.activity.R;
import com.peer.activity.Recommend_topic;
import com.peer.base.Constant;
import com.peer.base.pBaseAdapter;
import com.peer.bean.UserBean;
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
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HomepageAdapter extends pBaseAdapter {
	ViewHolder viewHolder=null;
	private boolean hasMesure = false;
	private int maxLines;
	private Context mContext;
	private List<Object> mList;
	int i;
//	List<String> pre_labels ;   //获取登陆者label
	public HomepageAdapter( Context mContext,List<Object> mList){
		
		super(mContext);

		this.mContext=mContext;
		this.mList=mList;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size()+1;
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
	 if(position==0){
			
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_top_topic,null,false);
			LinearLayout click = ViewHolder.get(convertView, R.id.ll_clike);
			click.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(!checkNetworkState()){
						Toast.makeText(mContext, mContext.getResources().getString(R.string.Broken_network_prompt), 0).show();
					}else{
						mContext.startActivity(new Intent(mContext,Recommend_topic.class));	
					}
					}
			});
			
		}else{	
				position=position-1;
					convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_recommend_person,null,false);				
					ImageView headpic=ViewHolder.get(convertView, R.id.im_headpic);
					TextView nikename=ViewHolder.get(convertView, R.id.tv_nikename);
					TextView descripe=ViewHolder.get(convertView, R.id.tv_descripe);
					LinearLayout click=ViewHolder.get(convertView, R.id.ll_clike);
					List plist = (List)mList.get(position);
					Map pmap = (Map) plist.get(0);
					nikename.setText((String)pmap.get("username"));
					List<String> plabels=(List<String>) plist.get(1);
					String labels="";
					for(String s:plabels){	
						if(labels.equals("")){			
							labels=s;	
						}else{
							labels=labels+" | "+s;
						}
					}
					descripe.setText(labels);
					click.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub	
							if(!checkNetworkState()){
								Toast.makeText(mContext, mContext.getResources().getString(R.string.Broken_network_prompt), 0).show();
							}else{
								
								Intent intent=new Intent(mContext,PersonalPageActivity.class);
								mContext.startActivity(intent);	
							}
						}
					});
		}
		
		return convertView;
	}
	
	
}
