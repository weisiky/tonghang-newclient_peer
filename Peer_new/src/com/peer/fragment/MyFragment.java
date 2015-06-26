package com.peer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.peer.activity.R;
import com.peer.base.pBaseFragment;

public class MyFragment extends pBaseFragment{
	private LinearLayout ll_top_my,linearLayout1,linearLayout2,ll_personmessage_my,ll_mytag_my,ll_setting_my;
	private RelativeLayout rl_ponseralpage,rl_myacount_my;
	private ImageView im_headpic;
	private TextView tv_nikename,tv_email;


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
			
		rl_ponseralpage=(RelativeLayout)getView().findViewById(R.id.rl_ponseralpage);		
		rl_myacount_my=(RelativeLayout)getView().findViewById(R.id.rl_myacount_my);
		ll_personmessage_my=(LinearLayout)getView().findViewById(R.id.ll_personmessage_my);
		ll_mytag_my=(LinearLayout)getView().findViewById(R.id.ll_mytag_my);
		ll_setting_my=(LinearLayout)getView().findViewById(R.id.ll_setting_my);
		im_headpic=(ImageView)getView().findViewById(R.id.im_headpic);
		tv_nikename=(TextView)getView().findViewById(R.id.tv_nikename);
		tv_email=(TextView)getView().findViewById(R.id.tv_email);	
			
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
