package com.peer.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.peer.R;
import com.peer.activity.OtherPageActivity;
import com.peer.activity.PersonalPageActivity;
import com.peer.activity.Recommend_topic;
import com.peer.base.pBaseActivity;
import com.peer.base.pBaseAdapter;
import com.peer.base.pBaseFragment;
import com.peer.bean.PersonpageBean;
import com.peer.bean.UserBean;
import com.peer.utils.ImageLoaderUtil;
import com.peer.utils.ViewHolder;
import com.peer.utils.pLog;

public class HomepageAdapter extends pBaseAdapter {
	private boolean hasMesure = false;
	private int maxLines;
	private Context mContext;
	private List<UserBean> users = new ArrayList<UserBean>();
	int i;
	private pBaseFragment baseFragment;
	private String pic_server;

	// List<String> pre_labels ; //获取登陆者label
	public HomepageAdapter(Context mContext, List<UserBean> users,
			String pic_server) {

		super(mContext);

		this.mContext = mContext;
		this.users = users;
		this.pic_server = pic_server;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
//		return users.size()+1;
		return users.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return users.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parentgroup) {
		// TODO Auto-generated method stub
//		if (position == 0) {
//
//			convertView = LayoutInflater.from(mContext).inflate(
//					R.layout.adapter_top_topic, null, false);
//			LinearLayout click = ViewHolder.get(convertView, R.id.ll_clike);
//			click.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					if (!((pBaseActivity) mContext).isNetworkAvailable) {
//						((pBaseActivity) mContext).showToast(
//								mContext.getResources().getString(
//										R.string.Broken_network_prompt),
//								Toast.LENGTH_SHORT, false);
//					} else {
//						Intent intent = new Intent(mContext,Recommend_topic.class);
//						mContext.startActivity(intent);
//					}
//				}
//			});
//
//		} else {
//			position = position - 1;
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.adapter_recommend_person, null, false);
			ImageView im_headpic = ViewHolder.get(convertView, R.id.im_headpic);

			
			
			TextView tv_nikename = ViewHolder
					.get(convertView, R.id.tv_nikename);
			TextView tv_descripe = ViewHolder
					.get(convertView, R.id.tv_descripe);
			LinearLayout ll_clike = ViewHolder.get(convertView, R.id.ll_clike);
			final UserBean userbean = users.get(position);
			
			// ImageLoader加载图片
			ImageLoaderUtil.getInstance().showHttpImage(mContext,
					pic_server + userbean.getImage(), im_headpic,
					R.drawable.mini_avatar_shadow);

			tv_nikename.setText(userbean.getUsername());
			ArrayList<String> plabels = userbean.getLabels();
			String labels = "";
//			int count
			for (String s : plabels) {
				if(s.contains("\t\t")){
					
					pLog.i("label",s+"高亮标签"+s.length());
					
					s=replaceBlank(s);
					pLog.i("label",s+"高亮标签"+s.length());
					
					if (labels.equals("")) {
						labels ="<font color=#f55c2e>"+s+"</font>";
//						labels =s;
					} else {
						labels = labels + " | " + "<font color=#f55c2e>"+s+"</font>";
//						labels = labels + " | " + s;
					}
				}else{
					if (labels.equals("")) {
						labels = s;
					} else {
						labels = labels + " | " + s;
					}
					
					
				}
			}
			/** 文字加高亮色 **/
			SpannableStringBuilder builder = new SpannableStringBuilder(
					labels);
			ForegroundColorSpan colorspan = new ForegroundColorSpan(mContext.getResources()
					.getColor(R.color.highlight));
			builder.setSpan(colorspan, 0, labels.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//			tv_descripe.setText(Html.fromHtml("<font color=#FF0000>"+awdfadsf+"</font>"));
			tv_descripe.setText(Html.fromHtml(labels));
			
			ll_clike.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
						PersonpageBean.getInstance().setUser(userbean);
						Intent intent = new Intent(mContext,
								OtherPageActivity.class);
						intent.putExtra("client_id", userbean.getClient_id());
						mContext.startActivity(intent);

				}
			});
//		}

		return convertView;
	}
	
	/** 正则表达式去制表符 **/
	 public static String replaceBlank(String str) {
		  String dest = "";
		  if (str!=null) {
		   Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		   Matcher m = p.matcher(str);
		   dest = m.replaceAll("");
		  }
		  return dest;
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
