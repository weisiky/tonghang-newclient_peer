package com.peer.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.peer.activity.PersonalPageActivity;
import com.peer.activity.R;
import com.peer.base.pBaseActivity;
import com.peer.base.pBaseAdapter;
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
	private List<Object> mlist;
	public FriendsAdapter(Context mContext,List<Object> list){
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_listnike_friends,null,false);
			ImageView im_headpic=(ImageView)convertView.findViewById(R.id.im_headpic);
			TextView tv_nikename=(TextView)convertView.findViewById(R.id.tv_nikename);			
			TextView tv_descripe=(TextView)convertView.findViewById(R.id.tv_descripe);
			LinearLayout ll_clike=(LinearLayout)convertView.findViewById(R.id.ll_clike);
			List plist = (List) mlist.get(position);
			Map pmap = (Map) plist.get(0);
			
			final UserBean userbean = new UserBean();
			userbean.setBirth((String)pmap.get("birth"));
			userbean.setCity((String)pmap.get("city"));
			userbean.setClient_id((String)pmap.get("client_id"));
			userbean.setCreated_at((String)pmap.get("created_at"));
			userbean.setEmail((String)pmap.get("email"));
			userbean.setUsername((String)pmap.get("username"));
			userbean.setImage((String)pmap.get("image"));
			userbean.setLabels((ArrayList<String>)plist.get(1));
			userbean.setPhone((String)pmap.get("username"));
			userbean.setSex((String)pmap.get("sex"));
			
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
}
