package com.peer.fragment;

import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.handmark.pulltorefresh.library.PullToRefreshBase;
//import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
//import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.peer.activity.R;
import com.peer.activity.SearchUserActivity;
import com.peer.base.pBaseFragment;
import com.peer.utils.pViewBox;

public class HomeFragment extends pBaseFragment{
	
	
	
		private LinearLayout ll_search;
		
//		private PullToRefreshListView pull_refresh_homepage;
		private ListView pull_refresh_homepage;
		public LinearLayout base_neterror_item;
		private TextView tv_connect_errormsg;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_home, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		init();
//		RecommendTask task=new RecommendTask();
//		task.execute();
	}
	
	/*
	 * 开线程刷新推荐用户数据
	 * 
	private class RefreshTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	*/
	
	/*
	 * 开线程，获取推荐用户（首次）
	 * 
	private class RecommendTask extends AsyncTask<Void, Void, List>{

		@Override
		protected List doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			return null;
		}
	}*/
	
	


	/*private void RefreshListner() {
		// TODO Auto-generated method stub
		pageViewaList.pull_refresh_homepage.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub					
					String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
							DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
					refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
					RefreshTask task=new RefreshTask();
					task.execute("DownToRefresh");								
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub				
				String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);				
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				RefreshTask task=new RefreshTask();
				task.execute("UpToRefresh");
			}			
		});
	}*/


	private void init() {
		// TODO Auto-generated method stub
		base_neterror_item =  (LinearLayout) getView().findViewById(R.id.base_neterror_item);
		tv_connect_errormsg = (TextView) base_neterror_item.findViewById(R.id.tv_connect_errormsg);
		
		ll_search=(LinearLayout)getView().findViewById(R.id.ll_search);		
//		createtopic=(TextView)getView().findViewById(R.id.tv_createtopic);
		
//		createtopic.setOnClickListener(this);		
		ll_search.setOnClickListener(this);
			
//		mPullrefreshlistview=(PullToRefreshListView)getView().findViewById(R.id.pull_refresh_homepage);
		
		
//		RefreshListner();
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_search:
			Intent search = new Intent(getActivity(),SearchUserActivity.class);
			startActivity(search);
			
			break;

		default:
			break;
		}
		
	}
	
}
