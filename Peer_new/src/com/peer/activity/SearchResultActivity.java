package com.peer.activity;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.bean.SearchBean;
import com.peer.utils.pViewBox;


/*
 * 搜索结果类
 * */
public class SearchResultActivity extends pBaseActivity{
	
	
//	private PullToRefreshListView lv_searchresult;
	private int page=1;
	
	class PageViewList {
		private LinearLayout ll_back;
		private TextView tv_title;
		
	}

	private PageViewList pageViewaList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		pageViewaList = new PageViewList();
		pViewBox.viewBox(this, pageViewaList);
		
		String searchname=SearchBean.getInstance().getSearchname();
		String searchaccuratetarget = SearchBean.getInstance().getCallbacklabel();
		
//		lv_searchresult=(PullToRefreshListView)findViewById(R.id.lv_searchresult);//自定义控件，自己find
		pageViewaList.tv_title.setText(searchname);
		
		
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
		
//		RefreshListner();//上拉刷新监听
	}

	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub
		if(SearchBean.getInstance().getSearchtype().equals(Constant.TOPICBYTOPIC)){
//			SearchTask task=new SearchTask();
//			task.execute(searchaccuratetarget);		
			showToast("搜索TOPICBYTOPIC", Toast.LENGTH_SHORT, false);
		}else if(SearchBean.getInstance().getSearchtype().equals(Constant.USERBYNIKE)){
//			SearchTask task=new SearchTask();
//			task.execute(searchaccuratetarget);		
			showToast("搜索USERBYNIKE", Toast.LENGTH_SHORT, false);
		}else if(SearchBean.getInstance().getSearchtype().equals(Constant.TOPICBYLABEL)){
//			SearchTask task=new SearchTask();
//			task.execute(searchaccuratetarget);
			showToast("搜索TOPICBYLABEL", Toast.LENGTH_SHORT, false);
		}else if(SearchBean.getInstance().getSearchtype().equals(Constant.USERBYLABEL)){
//			SearchTask task=new SearchTask();
//			task.execute(searchaccuratetarget);
			showToast("搜索USERBYLABEL", Toast.LENGTH_SHORT, false);
		}

	}

	@Override
	protected View loadTopLayout() {
		// TODO Auto-generated method stub
		// return getLayoutInflater().inflate(R.layout.top_layout, null);
		return getLayoutInflater().inflate(R.layout.base_toplayout_title, null);
	}

	@Override
	protected View loadContentLayout() {
		// TODO Auto-generated method stub
		return getLayoutInflater().inflate(R.layout.activity_searchresult, null);
	}
	
	@Override
	protected View loadBottomLayout() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		
	}
	
	
	/*
	 * 上拉，下拉刷新方法
	 * 
	private void RefreshListner() {
		// TODO Auto-generated method stub
		lv_searchresult.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub					
				String label = DateUtils.formatDateTime(SearchResultActivity.this.getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);				
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
//				downRefreshTask refresh=new downRefreshTask();
//				refresh.execute();
				showToast("下拉刷新成功", Toast.LENGTH_SHORT, false);
			}
			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub				
				String label = DateUtils.formatDateTime(SearchResultActivity.this.getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);				
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				page=+1;
//				RefreshTask task=new RefreshTask();
//				task.execute(SearchUtil.getInstance().getSearchname());
				showToast("上拉刷新成功", Toast.LENGTH_SHORT, false);
			}			
		});
	}*/
}
