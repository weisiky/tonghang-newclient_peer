package com.peer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.peer.activity.R;
import com.peer.base.pBaseFragment;

import com.peer.utils.pViewBox;

public class ComeMsgFragment extends pBaseFragment{

	

		private ListView lv_come;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_comemsg, container, false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
			init();
	}
	
	
	


	private void init() {
		// TODO Auto-generated method stub	
			
		lv_come=(ListView)getView().findViewById(R.id.lv_come);	
			
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}