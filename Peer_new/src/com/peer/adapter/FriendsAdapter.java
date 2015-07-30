package com.peer.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.peer.activity.PersonalPageActivity;
import com.peer.activity.R;
import com.peer.base.pBaseActivity;
import com.peer.base.pBaseAdapter;
import com.peer.base.pBaseFragment;
import com.peer.bean.PersonpageBean;
import com.peer.bean.UserBean;
import com.peer.utils.pLog;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class FriendsAdapter extends pBaseAdapter {
	private Context mContext;
	private List<UserBean> mlist;
	private pBaseFragment baseFragment;
	public FriendsAdapter(Context mContext,List<UserBean> list){
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
		return mlist.get(arg0);
	}
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parentgroup) {
		// TODO Auto-generated method stub
			final UserBean userbean = mlist.get(position);
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_listnike_friends,null,false);
			ImageView im_headpic=(ImageView)convertView.findViewById(R.id.im_headpic);
			TextView tv_nikename=(TextView)convertView.findViewById(R.id.tv_nikename);			
			TextView tv_descripe=(TextView)convertView.findViewById(R.id.tv_descripe);
			LinearLayout ll_clike=(LinearLayout)convertView.findViewById(R.id.ll_clike);
			
			tv_nikename.setText(userbean.getUsername());
			List<String>list = userbean.getLabels();
			String labels="";
			for(String s:list){
				if(labels.equals("")){
					labels=s;	
				}else{
					labels=labels+" | "+s;
				}			
		}
			tv_descripe.setText(labels);
			ll_clike.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!((pBaseActivity)mContext).isNetworkAvailable) {
					((pBaseActivity)mContext).showToast(((pBaseActivity)mContext).getResources().getString(
							R.string.Broken_network_prompt), Toast.LENGTH_LONG, false);
				} else {
					PersonpageBean.getInstance().setUser(userbean);
					Intent intent=new Intent(mContext,PersonalPageActivity.class);
					mContext.startActivity(intent);	
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
