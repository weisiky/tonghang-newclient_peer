package com.peer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.peer.activity.R;
import com.peer.base.pBaseFragment;

public class FriendsFragment extends pBaseFragment{
	
	private LinearLayout ll_head_come;
	private RelativeLayout rl_newfriends;
	private TextView tv_newfriends;
	private ListView lv_friends;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_friends, container, false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		init();
			
	}
	
	



	private void init() {
		// TODO Auto-generated method stub
		lv_friends=(ListView)getView().findViewById(R.id.lv_friends);				
		rl_newfriends=(RelativeLayout)getView().findViewById(R.id.rl_newfriends);
		tv_newfriends=(TextView)getView().findViewById(R.id.tv_newfriends);
		
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
}
