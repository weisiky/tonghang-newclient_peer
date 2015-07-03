package com.peer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.peer.base.pBaseActivity;
import com.peer.utils.pViewBox;


/**
 * 推荐话题类
 * 
 */
public class Recommend_topic extends pBaseActivity{
	
	private PullToRefreshListView pull_refresh_topic;
	
	class PageViewList {
		private LinearLayout ll_back,ll_topic;
		private TextView tv_title;
		private ImageView im_search;
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
		pageViewaList.tv_title.setText("话题");
		pull_refresh_topic = (PullToRefreshListView) findViewById(R.id.pull_refresh_topic);
		pageViewaList.ll_topic.setVisibility(View.VISIBLE);
		pageViewaList.im_search.setVisibility(View.GONE);
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
		pageViewaList.ll_topic.setOnClickListener(this);
		pageViewaList.im_search.setOnClickListener(this);
	}

	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub

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
		return getLayoutInflater().inflate(R.layout.activity_recommend_topic, null);
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
		switch (v.getId()) {
		case R.id.im_search:
			Intent searchtopic = new Intent(this,SearchTopicActivity.class);
			startActivity(searchtopic);
			break;
		case R.id.ll_topic:
			Intent createtopic = new Intent(this,CreatTopicActivity.class);
			startActivity(createtopic);
			break;

		default:
			break;
		}
		
	}
}
