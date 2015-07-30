package com.peer.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.peer.activity.PersonalPageActivity;
import com.peer.activity.R;
import com.peer.base.pBaseActivity;
import com.peer.base.pBaseAdapter;
import com.peer.bean.PersonpageBean;
import com.peer.bean.UserBean;
import com.peer.utils.ViewHolder;

public class SeachResultAdapter extends pBaseAdapter {
	private List<UserBean> mlist;
	private Context mContext;
	public SeachResultAdapter(Context mContext,List<UserBean> list){
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
		convertView = LayoutInflater.from(mContext).inflate(
				R.layout.adapter_listnike_friends, null, false);
		ImageView im_headpic = ViewHolder.get(convertView, R.id.im_headpic);
		TextView tv_nikename = ViewHolder.get(convertView, R.id.tv_nikename);
		TextView tv_descripe = ViewHolder.get(convertView, R.id.tv_descripe);
		LinearLayout ll_clike = ViewHolder.get(convertView, R.id.ll_clike);
//		List plist = (List) mlist.get(position);
//		Map pmap = (Map) plist.get(0);
		tv_nikename.setText(userbean.getUsername());
		List<String> plabels = userbean.getLabels();
		String labels = "";

		for (String s : plabels) {
			if (labels.equals("")) {
				labels = s;
			} else {
				labels = labels + " | " + s;
			}
		}
		tv_descripe.setText(labels);
		ll_clike.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				PersonpageBean.getInstance().setUser(userbean);
				Intent intent=new Intent();
				((pBaseActivity)mContext).startActivityForLeft(PersonalPageActivity.class, intent, false);
			}
		});
		return convertView;
	}

}
