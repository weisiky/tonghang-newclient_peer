package com.peer.fragment;

import java.util.List;

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
import com.peer.base.pBaseFragment;
import com.peer.utils.pViewBox;

public class HomeFragment extends pBaseFragment{
	private PageViewList pageViewaList;
	
	class PageViewList {
		private LinearLayout ll_search;
//		private PullToRefreshListView pull_refresh_homepage;
		private ListView pull_refresh_homepage;
		public LinearLayout base_neterror_item;
		

	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	

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
	
	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		pageViewaList = new PageViewList();
		pViewBox.viewBox(getActivity(), pageViewaList);

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_search.setOnClickListener(this);
		
		/*
		 * 监听上拉下拉刷新
		 * */
//		RefreshListner();

	}

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


	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub

	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
}
