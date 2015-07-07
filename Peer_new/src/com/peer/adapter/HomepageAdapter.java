package com.peer.adapter;

import java.util.List;
import java.util.Map;

import com.peer.activity.PersonalPageActivity;
import com.peer.activity.R;
import com.peer.activity.Recommend_topic;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.base.pBaseAdapter;
import com.peer.base.pBaseFragment;
import com.peer.bean.UserBean;
import com.peer.utils.ViewHolder;
import com.tencent.mm.sdk.modelmsg.ShowMessageFromWX;

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
	private boolean hasMesure = false;
	private int maxLines;
	private Context mContext;
	private List<Object> mList;
	int i;
	private pBaseFragment baseFragment;

	// List<String> pre_labels ; //获取登陆者label
	public HomepageAdapter(Context mContext, List<Object> mList) {

		super(mContext);

		this.mContext = mContext;
		this.mList = mList;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size() + 1;
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
		if (position == 0) {

			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.adapter_top_topic, null, false);
			LinearLayout click = ViewHolder.get(convertView, R.id.ll_clike);

		} else {
			position = position - 1;
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.adapter_recommend_person, null, false);
			ImageView im_headpic = ViewHolder.get(convertView, R.id.im_headpic);
			TextView tv_nikename = ViewHolder.get(convertView, R.id.tv_nikename);
			TextView tv_descripe = ViewHolder.get(convertView, R.id.tv_descripe);
			LinearLayout ll_clike = ViewHolder.get(convertView, R.id.ll_clike);
			List plist = (List) mList.get(position);
			Map pmap = (Map) plist.get(0);
			tv_nikename.setText((String) pmap.get("username"));
			List<String> plabels = (List<String>) plist.get(1);
			String labels = "";

//			if (baseFragment != null) {
//				headpic.setOnClickListener((OnClickListener) baseFragment);
//			} else {
//				headpic.setOnClickListener((OnClickListener) mContext);
//			}

			for (String s : plabels) {
				if (labels.equals("")) {
					labels = s;
				} else {
					labels = labels + " | " + s;
				}
			}
			tv_descripe.setText(labels);
		}

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
