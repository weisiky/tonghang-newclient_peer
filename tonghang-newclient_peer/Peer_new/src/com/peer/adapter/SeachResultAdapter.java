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

import com.peer.R;
import com.peer.activity.OtherPageActivity;
import com.peer.activity.PersonalPageActivity;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.base.pBaseAdapter;
import com.peer.bean.PersonpageBean;
import com.peer.bean.UserBean;
import com.peer.utils.ImageLoaderUtil;
import com.peer.utils.ViewHolder;

public class SeachResultAdapter extends pBaseAdapter {
	private List<UserBean> mlist;
	private Context mContext;
	private String pic_server;
	public SeachResultAdapter(Context mContext,List<UserBean> list,String pic_server){
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
		// ImageLoader加载图片
		ImageLoaderUtil.getInstance().showHttpImage(mContext,
				pic_server + userbean.getImage(), im_headpic,
				R.drawable.mini_avatar_shadow);
		
		TextView tv_nikename = ViewHolder.get(convertView, R.id.tv_nikename);
		TextView tv_descripe = ViewHolder.get(convertView, R.id.tv_descripe);
		LinearLayout ll_clike = ViewHolder.get(convertView, R.id.ll_clike);
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
				String client_id = ((pBaseActivity)mContext).mShareFileUtils.getString(Constant.CLIENT_ID, "");
				PersonpageBean.getInstance().setUser(userbean);
				if(userbean.getClient_id().equals(client_id)){
						Intent intent = new Intent(mContext,
								PersonalPageActivity.class);
						mContext.startActivity(intent);
					}else{
						Intent intent = new Intent(mContext,
								OtherPageActivity.class);
						intent.putExtra("client_id", userbean.getClient_id());
						mContext.startActivity(intent);
					}
				}
		});
		return convertView;
	}

}
