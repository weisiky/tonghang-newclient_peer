package com.peer.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.peer.activity.R;
import com.peer.activity.SearchUserActivity;
import com.peer.adapter.HomepageAdapter;
import com.peer.base.Constant;
import com.peer.base.pBaseFragment;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.pLog;



/**
 *主fragment类（推荐用户） 
 */
public class HomeFragment extends pBaseFragment{
	
	HomepageAdapter adapter;
	
		private LinearLayout ll_search;
		List<Object> list = new ArrayList<Object>();
		private PullToRefreshListView pull_refresh_homepage;
		public LinearLayout base_neterror_item;
		private TextView tv_connect_errormsg;
		/** 分页参数 **/
		int page = 1;
	

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
		try {
			RecommendTask(Constant.CLIENT_ID,page);
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	private void RefreshListner() {
		// TODO Auto-generated method stub
		pull_refresh_homepage.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub					
					String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
							DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
					refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
					try {
						page = 1;
						RecommendTask(Constant.CLIENT_ID, page);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub				
				String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);				
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				try {
					RecommendTask(Constant.CLIENT_ID, ++page);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
		});
	}

	/**
	 * 首页用户推荐请求接口
	 * 
	 * @param client_id
	 * @param page
	 * @throws UnsupportedEncodingException
	 */
	private void RecommendTask(String client_id , int page) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		List<BasicNameValuePair> baseParams=new ArrayList<BasicNameValuePair>();
		baseParams.add(new BasicNameValuePair("client_id", client_id));
		String params=PeerParamsUtils.ParamsToJsonString(baseParams);
		
		HttpEntity entity = new StringEntity(params, "utf-8");
		
		HttpUtil.post(getActivity(), Constant.USER_RECOMMEND_IN_URL+"?page="+page, entity, "application/json",
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub

						pLog.i("test", "onFailure+statusCode:" + statusCode
								+ "headers:" + headers.toString()
								+ "responseString:" + responseString);

						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						// TODO Auto-generated method stub
				
						pLog.i("test", "onFailure+statusCode:" + statusCode
								+ "headers:" + headers.toString()
								+ "errorResponse:" + errorResponse.toString());
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						
						pLog.i("test", "onFailure:statusCode:" + statusCode);
						pLog.i("test", "throwable:" + throwable.toString());
						pLog.i("test", "headers:" + headers.toString());
						pLog.i("test",
								"errorResponse:" + errorResponse.toString());
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONArray response) {
						// TODO Auto-generated method stub
						
						pLog.i("test", "onSuccess+statusCode:" + statusCode
								+ "headers:" + headers.toString() + "response:"
								+ response.toString());
						super.onSuccess(statusCode, headers, response);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						
						pLog.i("test", "onSuccess:statusCode:" + statusCode
								+ "headers:" + headers.toString() + "response:"
								+ response.toString());
						
						try {
						JSONObject success = response.getJSONObject("success");	
						JSONArray user = (JSONArray) success.get("user");
						for(int index=0;index<user.length();index++){
							ArrayList<Object> userlist = new ArrayList<Object>();
							JSONObject person = user.getJSONObject(index);
							List<String> labelnames = new ArrayList<String>();
							Map<String,Object> userMsg = new HashMap<String, Object>();
							userMsg.put("email", person.getString("email"));
							userMsg.put("sex", person.getString("sex"));
							userMsg.put("city", person.getString("city"));
							userMsg.put("username", person.getString("username"));
							userMsg.put("client_id", person.getString("client_id"));
							userMsg.put("image", person.getString("image"));
							userMsg.put("created_at", person.getString("created_at"));
							userMsg.put("birth", person.getString("birth"));
							userlist.add(userMsg);
							JSONArray lab = (JSONArray) person.get("labels");
							for(int i=0;i<lab.length();i++){
								labelnames.add(lab.getString(i));
							}
							userlist.add(labelnames);
							list.add(userlist);
						}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						adapter=new HomepageAdapter(getActivity(),list);
						pull_refresh_homepage.setAdapter(adapter);
						
						super.onSuccess(statusCode, headers, response);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String responseString) {
						// TODO Auto-generated method stub
						
						pLog.i("test", "onSuccess:statusCode:" + statusCode
								+ "headers:" + headers.toString()
								+ "responseString:" + responseString.toString());
						super.onSuccess(statusCode, headers, responseString);
					}

				});
		
	}

	private void init() {
		// TODO Auto-generated method stub
		base_neterror_item =  (LinearLayout) getView().findViewById(R.id.base_neterror_item);
		tv_connect_errormsg = (TextView) base_neterror_item.findViewById(R.id.tv_connect_errormsg);
		
		ll_search=(LinearLayout)getView().findViewById(R.id.ll_search);		
//		createtopic=(TextView)getView().findViewById(R.id.tv_createtopic);
		
//		createtopic.setOnClickListener(this);		
		ll_search.setOnClickListener(this);
			
		pull_refresh_homepage=(PullToRefreshListView)getView().findViewById(R.id.pull_refresh_homepage);
		
		
		RefreshListner();
		
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
