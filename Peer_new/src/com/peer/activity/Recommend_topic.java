package com.peer.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.R;
import com.peer.adapter.Recommend_topicAdapter;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.bean.RecommendTopicBean;
import com.peer.bean.TopicBean;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pIOUitls;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;

/**
 * 话题推荐
 * 
 */
public class Recommend_topic extends pBaseActivity {

	private PullToRefreshListView pull_refresh_topic;
	private List<TopicBean> list = new ArrayList<TopicBean>();
	private int page = 1;
	public static int pageindex;
	Recommend_topicAdapter adapter;

	class PageViewList {
		private LinearLayout ll_back, ll_topic,ll_search;
		private TextView tv_title;
	}

	private PageViewList pageViewaList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		pageViewaList = new PageViewList();
		pViewBox.viewBox(this, pageViewaList);
		pageViewaList.tv_title.setText("话题");
		pull_refresh_topic = (PullToRefreshListView) findViewById(R.id.pull_refresh_topic);
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
		pageViewaList.ll_topic.setOnClickListener(this);
		pageViewaList.ll_search.setOnClickListener(this);
		
		RefreshListner();

	}
	
	private void RefreshListner() {
		// TODO Auto-generated method stub
		pull_refresh_topic
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@SuppressWarnings("static-access")
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						String label = DateUtils.formatDateTime(Recommend_topic.this
								.getApplicationContext(), System
								.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);
						if(isNetworkAvailable){
							try {
								page = 1;
								sendRecommendtopic(mShareFileUtils.getString(
										Constant.CLIENT_ID, ""), page);
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else{
							showToast(getResources().getString(R.string.Broken_network_prompt)
									, Toast.LENGTH_SHORT, false);
							pull_refresh_topic.onRefreshComplete();
						}
						
					}

					@SuppressWarnings("static-access")
					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						String label = DateUtils.formatDateTime(Recommend_topic.this
								.getApplicationContext(), System
								.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);
						if(isNetworkAvailable){
							try {
								
								if(page==pageindex){
									pLog.i("test", "page:"+page);
									pLog.i("test", "pageindex:"+pageindex);
									
									sendRecommendtopic(mShareFileUtils.getString(
											Constant.CLIENT_ID, ""), ++page);
								}else{
									pLog.i("test", "page:"+page);
									pLog.i("test", "pageindex:"+pageindex);
									if(pageindex == 0){
										page = 1;
									}
									sendRecommendtopic(mShareFileUtils.getString(
											Constant.CLIENT_ID, ""), page);
								}
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else{
							showToast(getResources().getString(R.string.Broken_network_prompt)
									, Toast.LENGTH_SHORT, false);
							pull_refresh_topic.onRefreshComplete();
						}
						
					}
				});
	}
	

	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub
		try {
			sendRecommendtopic(
					mShareFileUtils.getString(Constant.CLIENT_ID, ""), page);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected View loadTopLayout() {
		// TODO Auto-generated method stub
		// return getLayoutInflater().inflate(R.layout.top_layout, null);
		return getLayoutInflater().inflate(R.layout.topic_sealayout_title, null);
	}

	@Override
	protected View loadContentLayout() {
		// TODO Auto-generated method stub
		return getLayoutInflater().inflate(R.layout.activity_recommend_topic,
				null);
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
		case R.id.ll_search:
			Intent searchtopic = new Intent(this, SearchTopicActivity.class);
			startActivity(searchtopic);
			break;
		case R.id.ll_topic:
			Intent createtopic = new Intent(this, CreatTopicActivity.class);
			startActivity(createtopic);
			break;

		default:
			break;
		}

	}

	@Override
	public void onNetworkOn() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNetWorkOff() {
		// TODO Auto-generated method stub

	}

	/**
	 * 推荐话题请求
	 * 
	 * @param client_id
	 * @param page
	 * @throws UnsupportedEncodingException
	 */
	private void sendRecommendtopic(String client_id, final int page)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub

		// HttpEntity entity = null;
		// try {
		// entity = PeerParamsUtils.getRemTopicParams(this, client_id, page);
		// } catch (Exception e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }

		RequestParams params = null;
		try {
			params = PeerParamsUtils.getRemTopicParams(this, client_id, page);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		HttpUtil.post(this, HttpConfig.TOPIC_RECOMMEND_IN_URL, params,
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub
						hideLoading();
						pLog.i("test","statusCode:"+statusCode);
						pLog.i("test","headers:"+headers);
						pLog.i("test","throwable:"+throwable);
						pLog.i("test","throwable:"+throwable);
						showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						// TODO Auto-generated method stub
						hideLoading();
						pLog.i("test","statusCode:"+statusCode);
						pLog.i("test","headers:"+headers);
						pLog.i("test","throwable:"+throwable);
						pLog.i("test","errorResponse:"+errorResponse);
						showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						hideLoading();
						pLog.i("test","statusCode:"+statusCode);
						pLog.i("test","headers:"+headers);
						pLog.i("test","throwable:"+throwable);
						pLog.i("test","errorResponse:"+errorResponse);
						showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						hideLoading();

						pLog.i("test", "response:" + response.toString());

						try {
							JSONObject result = response.getJSONObject("success");

							String code = result.getString("code");
							pLog.i("test", "code:"+code);
							if(code.equals("200")){
								RecommendTopicBean recommendtopicbean = JsonDocHelper
										.toJSONObject(
												response.getJSONObject("success")
												.toString(),
												RecommendTopicBean.class);
								pageindex = page;
								if (recommendtopicbean != null) {
									pLog.i("test", "user1:"
											+ recommendtopicbean.topics.get(0)
											.getSubject().toString());
									if (page == 1) {
										list.clear();
									}
									list.addAll(recommendtopicbean.topics);
									if (adapter == null) {
										adapter = new Recommend_topicAdapter(
												Recommend_topic.this, list
												,recommendtopicbean.getPic_server());
										pull_refresh_topic.setAdapter(adapter);
									}
									
									
								}
							}else if(code.equals("500")){
								
							}else{
								if(pageindex == 0){
									
									list.clear();
								}
								if(page == 1){
									list.clear();
								}
								if (adapter == null) {
									adapter = new Recommend_topicAdapter(
											Recommend_topic.this, list
											,mShareFileUtils.getString(Constant.PIC_SERVER, ""));
									pull_refresh_topic.setAdapter(adapter);
								}
								String message = result.getString("message");
								showToast(message, Toast.LENGTH_SHORT, false);
							}
						} catch (Exception e1) {
							pLog.i("test", "Exception:" + e1.toString());
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						refresh();
						super.onSuccess(statusCode, headers, response);
					}


				});

	}

	private void refresh() {

		if (adapter != null) {
			adapter.notifyDataSetChanged();
			pull_refresh_topic.postDelayed(new Runnable() {

	            @Override
	            public void run() {
	            	pull_refresh_topic.onRefreshComplete();
	            }
	        }, 1000);

		}

	}

}
