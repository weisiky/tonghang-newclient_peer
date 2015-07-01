package com.peer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.peer.activity.MyAcountActivity;
import com.peer.activity.MySkillActivity;
import com.peer.activity.PersonalMessageActivity;
import com.peer.activity.R;
import com.peer.activity.SettingActivity;
import com.peer.base.pBaseFragment;


/**
 * ‘我的’页 fragment
 * 
 */
public class MyFragment extends pBaseFragment{
	private LinearLayout ll_top_my,linearLayout1,linearLayout2,ll_personmessage_my,ll_mytag_my,ll_setting_my;
	private RelativeLayout rl_ponseralpage,rl_myacount_my;
	private ImageView im_headpic;
	private TextView tv_nikename,tv_email;
	
	public static final int UPDATESUCESS=9;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_my, container, false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
			init();
	}
	
	
	


	private void init() {
		// TODO Auto-generated method stub	
			
		/*
		 * 初始化控件
		 * */
		rl_ponseralpage=(RelativeLayout)getView().findViewById(R.id.rl_ponseralpage);		
		rl_myacount_my=(RelativeLayout)getView().findViewById(R.id.rl_myacount_my);
		ll_personmessage_my=(LinearLayout)getView().findViewById(R.id.ll_personmessage_my);
		ll_mytag_my=(LinearLayout)getView().findViewById(R.id.ll_mytag_my);
		ll_setting_my=(LinearLayout)getView().findViewById(R.id.ll_setting_my);
		im_headpic=(ImageView)getView().findViewById(R.id.im_headpic);
		tv_nikename=(TextView)getView().findViewById(R.id.tv_nikename);
		tv_email=(TextView)getView().findViewById(R.id.tv_email);
		
		/*
		 * 设置控件监听
		 * */
		rl_ponseralpage.setOnClickListener(this);
		rl_myacount_my.setOnClickListener(this);
		ll_personmessage_my.setOnClickListener(this);
		ll_mytag_my.setOnClickListener(this);
		ll_setting_my.setOnClickListener(this);
		
		getlocalMsg();	
	}
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getlocalMsg();
	}
	
	/*
	 * 自定义方法
	 * 设置显示当前用户的头像，昵称，邮箱。
	 * 测试阶段，使用静态默认值。
	 * */
	public void getlocalMsg(){
		im_headpic.setBackgroundDrawable(getResources().getDrawable(R.drawable.mini_avatar_shadow));
		tv_nikename.setText("weisiky");
		tv_email.setText("395296797@qq.com");	
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {				
		case R.id.rl_myacount_my:
			Intent myacount=new Intent(getActivity(),MyAcountActivity.class);
			startActivity(myacount);
			break;
		case R.id.ll_personmessage_my:
			Intent personmessage=new Intent(getActivity(),PersonalMessageActivity.class);
			startActivityForResult(personmessage, UPDATESUCESS);
			break;
		case R.id.ll_mytag_my:
			Intent mytag=new Intent(getActivity(),MySkillActivity.class);
			startActivity(mytag);
			break;
		case R.id.ll_setting_my:
			Intent setting=new Intent(getActivity(),SettingActivity.class);
			startActivity(setting);
			break;
		case R.id.rl_ponseralpage:
			/*
			 * 跳转至个人主页
			 * 暂无
			 * */
//			ToMypersonalpage();	
		
			break;
		default:
			break;	
		}
	}
}
